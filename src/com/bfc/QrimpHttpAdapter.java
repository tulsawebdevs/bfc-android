package com.bfc;

import java.util.LinkedHashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.DataSetObserver;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class QrimpHttpAdapter extends BaseAdapter{

	private double lat,lng;
	private Context context;
	private static final int PAGESIZE=10;
	private static final int NUM_CACHED_PAGES=20;
	
	private int numPages=0;
	private int numRecords=0;
	
	private ExecutorService scheduler = Executors.newFixedThreadPool(2);
	
	private JSONArray[] chachedPages=new JSONArray[NUM_CACHED_PAGES];
	private int[]   chachedPageNumbers = new int[NUM_CACHED_PAGES];
	
	public QrimpHttpAdapter(Context context,double initLat,double initLong){
		lat=initLat;
		lng=initLong;
		this.context=context;
		scheduler.submit(new Runnable(){
			public void run() {
				JSONObject ct = HttpHandler.getPageInfo(lat, lng, PAGESIZE);
				//{"paginationInfo":{"pageSize":25,"numPages":549,"numRecords":13722,"currentPage":1}}
				try {
					JSONObject info = ct.getJSONObject("paginationInfo");
					numPages = info.getInt("numPages");
					numRecords=info.getInt("numRecords");
					QrimpHttpAdapter.this.notifyDataSetChanged();
				} catch (JSONException e) {}
			}
		});
		for(int i=0;i<chachedPageNumbers.length;i++)
			chachedPageNumbers[i]=-1;
	}
	
	public int getCount() {
		return numRecords;
	}

	public Object getItem(int arg0) {
		int page = arg0/PAGESIZE;
		int pageItem = arg0%PAGESIZE;
		if(chachedPageNumbers[page%PAGESIZE]==page){
			JSONArray pageJ = chachedPages[page%PAGESIZE];
			try {
				return pageJ.get(pageItem);
			} catch (JSONException e) {}
		}else{
			fetchPage(page);
		}
		return null;
	}

	public long getItemId(int position) {
		JSONObject item = (JSONObject) getItem(position);
		if(item == null)return -1;
		try {
			return item.getLong("id");
		} catch (JSONException e) {
			return -1;
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		JSONObject object = (JSONObject) getItem(position);
		if(object==null){
			return new View(context);
		}
		Tag tag;
		if(convertView==null||convertView.getTag()==null){
			convertView= View.inflate(context, R.layout.list_object, parent);
			tag = new Tag();
			convertView.setTag(tag);
			tag.distance=(TextView) convertView.findViewById(R.id.distance);
			tag.name=(TextView) convertView.findViewById(R.id.name);
			tag.photo=(ImageView) convertView.findViewById(R.id.photo);
		}else
			tag= (Tag) convertView.getTag();
		
		try {
			tag.name.setText(object.getString("title"));
			tag.distance.setText(object.getString("distance"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
	
	protected class Tag{
		public JSONObject object;
		public ImageView photo;
		public TextView name;
		public TextView distance;
	}
	
	private LinkedHashSet<Integer> orderedSet = new LinkedHashSet<Integer>();
	
	private void fetchPage(final int pageNumber){
		if(orderedSet.contains(pageNumber))return;
		orderedSet.add(pageNumber);
		scheduler.submit(new Runnable() {
			public void run() {
				/*{
   "Photos":[
      {
         "id":"1532",
         "title":"Aerial View of Tulsa - Skaggs Store on Southwest Corner 5th & Boulder",
         "subject":"Tulsa (Okla.) -- History",
         "verified":"",
         "relation":"Accession #D7388.",
         "contentdm_number":"1559",
         "latitude":"36.151060",
         "longitude":"-95.991269",
         "distance":"0.1018411"
      }]}
      */
				JSONObject obj = HttpHandler.getPage(lat, lng, PAGESIZE, pageNumber);
				try {
					chachedPages[pageNumber%NUM_CACHED_PAGES]=obj.getJSONArray("Photos");
					chachedPageNumbers[pageNumber%NUM_CACHED_PAGES]=pageNumber;
				} catch (JSONException e) {
				}
				orderedSet.remove(pageNumber);
				QrimpHttpAdapter.this.notifyDataSetChanged();
			}
		});
	}
}
