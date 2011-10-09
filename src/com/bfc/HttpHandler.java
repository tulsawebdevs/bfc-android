package com.bfc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpHandler {

	private static final String QRIMP_COUNT_IMAGES_URL="http://tulsapics.qrimp.com/db.aspx?t=Photos&qid=getPhotosByDistance&lat=%1$f&long=%2$f&pagesize=%3$d&noheader=true&nofooter=true&_pagingInfo=json&vid=JSON";
	private static final String QRIMP_LIST_URL="http://tulsapics.qrimp.com/db.aspx?t=Photos&qid=getPhotosByDistance&lat=%1$f&long=%2$f&pagesize=%3$d&vid=json&gotopage=%4$d";
	
	private static final String IMAGE_URL="https://server16063.contentdm.oclc.org/cgi-bin/getimage.exe?CISOROOT=/p15020coll1&CISOPTR=%1&DMSCALE=100.00000&DMWIDTH=600&DMHEIGHT=600&DMX=152&DMY=0&DMTEXT=&REC=1&DMTHUMB=1&DMROTATE=0";
	
	private static String getQrimpCountUrl(double lat, double lng,int pageSize){
		return String.format(QRIMP_COUNT_IMAGES_URL, lat,lng,pageSize);
	}
	
	private static String getQrimpListUrl(double lat,double lng,int pageSize,int page){
		return String.format(QRIMP_LIST_URL, lat,lng,pageSize,page);
	}
	
	/*
	public static String getImage(){
		
	}*/
	
	public static JSONObject getPageInfo(double lat,double lng,int pageSize){
		String url = getQrimpCountUrl(lat, lng, pageSize);
		String res = getResponse(url);
		if(res==null||res.length()==0)
			return null;
		JSONObject jo=null;
		try {
			jo = new JSONObject(res);
		} catch (JSONException e) {}
		return jo;
	}
	
	public static JSONObject getPage(double lat,double lng,int pageSize,int page){
		String url = getQrimpListUrl(lat, lng, pageSize,page);
		String res = getResponse(url);
		if(res==null||res.length()==0)
			return null;
		JSONObject jo=null;
		try {
			jo = new JSONObject(res);
		} catch (JSONException e) {}
		return jo;
	}
	
	private static String getResponse(String url){
	    HttpClient client = new DefaultHttpClient();		
	    HttpRequestBase request;
	    try {
		    request = new HttpGet(url);
		    HttpResponse response= client.execute(request);
		    if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK){
		    	Log.e("HttpHandler", "Http Status: "+response.getStatusLine().getReasonPhrase());
		    }
		    HttpEntity entity = response.getEntity();
		    StringBuffer result = new StringBuffer();
			if(entity!=null){					
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				entity.writeTo(baos);					
				result.append(baos.toString());				
			}		    
		    return result.toString();
		} catch (IOException e) {
			Log.e("HttpHandler", "Error Processing HTTP Request",e);
		} catch (Exception e){
			Log.e("HttpHandler","Error Fetching Response",e);
		}
	    return null;
	}
	
}
