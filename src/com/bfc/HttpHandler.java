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

	private static final String QRIMP_COUNT_IMAGES_URL="tulsapics.qrimp.com/db.aspx?t=Photos&qid=getPhotosByDistance&lat=%1$f&long=%2$f&pagesize=%3$d&noheader=true&nofooter=true&_pagingInfo=json&vid=JSON";
	private static final String QRIMP_LIST_URL="tulsapics.qrimp.com/db.aspx?t=Photos&qid=getPhotosByDistance&lat=%1$f&long=%2$f&pagesize=%3$d&vid=json&gotopage=%4$d";
	
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
		String res = "{\"paginationInfo\":{\"pageSize\":10,\"numPages\":2230,\"numRecords\":22298,\"currentPage\":1}}";//getResponse(url);
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
	
	private static String getResponse(String url){/*
		HttpParams httpParameters=new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParameters,15000);
	    HttpConnectionParams.setSoTimeout(httpParameters,15000);
	    HttpClient client = new DefaultHttpClient(httpParameters);		
	    HttpRequestBase request;

	    try {
		    request = new HttpGet(url);
		    request.setHeader("Content-Type", "application/json");
		    HttpResponse response=null;
		    response = client.execute(request);
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
		}
	    return null;*/
		//Sample Test String
		return "{\"Photos\":[{\"id\":\"1532\",\"title\":\"Aerial View of Tulsa - Skaggs Store on Southwest Corner 5th & Boulder\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #D7388.\",\"contentdm_number\":\"1559\",\"latitude\":\"36.151060\",\"longitude\":\"-95.991269\",\"distance\":\"0.1018411\"},{\"id\":\"7344\",\"title\":\"5th and Boulder, 1928\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"V\",\"relation\":\"Accession #A0562\",\"contentdm_number\":\"7371\",\"latitude\":\"36.151060\",\"longitude\":\"-95.991269\",\"distance\":\"0.1018411\"},{\"id\":\"197\",\"title\":\"5th & Cheyenne.\",\"subject\":\"Tulsa (Okla.) -- History; Buildings\",\"verified\":\"\",\"relation\":\"Accession #A0270.\",\"contentdm_number\":\"220\",\"latitude\":\"36.150650\",\"longitude\":\"-95.992429\",\"distance\":\"0.1429049\"},{\"id\":\"12109\",\"title\":\"5th and Cheyenne, 1915\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"V\",\"relation\":\"Accession #A0096\",\"contentdm_number\":\"12137\",\"latitude\":\"36.150650\",\"longitude\":\"-95.992429\",\"distance\":\"0.1429049\"},{\"id\":\"185\",\"title\":\"5th & Boston.\",\"subject\":\"Tulsa (Okla.) -- History; Buildings\",\"verified\":\"\",\"relation\":\"Accession #A1453.\",\"contentdm_number\":\"208\",\"latitude\":\"36.151900\",\"longitude\":\"-95.988949\",\"distance\":\"0.1435484\"},{\"id\":\"186\",\"title\":\"5th & Boston, 1955.\",\"subject\":\"Tulsa (Okla.) -- History; Buildings\",\"verified\":\"\",\"relation\":\"Accession #B1725.\",\"contentdm_number\":\"209\",\"latitude\":\"36.151900\",\"longitude\":\"-95.988949\",\"distance\":\"0.1435484\"},{\"id\":\"213\",\"title\":\"5th Street and Boston\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #C0430\",\"contentdm_number\":\"236\",\"latitude\":\"36.151900\",\"longitude\":\"-95.988949\",\"distance\":\"0.1435484\"},{\"id\":\"215\",\"title\":\"5th Street and Boston Avenue, 1930.\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #C0507\",\"contentdm_number\":\"238\",\"latitude\":\"36.151900\",\"longitude\":\"-95.988949\",\"distance\":\"0.1435484\"},{\"id\":\"2920\",\"title\":\"5th and Boston, 1919\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"V\",\"relation\":\"Accession #A2207\",\"contentdm_number\":\"2947\",\"latitude\":\"36.151900\",\"longitude\":\"-95.988949\",\"distance\":\"0.1435484\"},{\"id\":\"11812\",\"title\":\"Northwest Corner of 5th Street and Boston\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #D6474.\",\"contentdm_number\":\"11840\",\"latitude\":\"36.151900\",\"longitude\":\"-95.988949\",\"distance\":\"0.1435484\"},{\"id\":\"13106\",\"title\":\"Philtower Building at 5th and Boston in 1928\",\"subject\":\"Tulsa (Okla.) -- History; Buildings\",\"verified\":\"\",\"relation\":\"Accession #A2097.\",\"contentdm_number\":\"13134\",\"latitude\":\"36.151900\",\"longitude\":\"-95.988949\",\"distance\":\"0.1435484\"},{\"id\":\"137\",\"title\":\"4th & Boulder.\",\"subject\":\"\",\"verified\":\"\",\"relation\":\"Accession #A0075.\",\"contentdm_number\":\"160\",\"latitude\":\"36.152040\",\"longitude\":\"-95.991769\",\"distance\":\"0.1718378\"},{\"id\":\"138\",\"title\":\"4th & Boulder.\",\"subject\":\"\",\"verified\":\"\",\"relation\":\"Accession #A1458.\",\"contentdm_number\":\"161\",\"latitude\":\"36.152040\",\"longitude\":\"-95.991769\",\"distance\":\"0.1718378\"},{\"id\":\"139\",\"title\":\"4th & Boulder.\",\"subject\":\"\",\"verified\":\"\",\"relation\":\"Accession #B1911.\",\"contentdm_number\":\"162\",\"latitude\":\"36.152040\",\"longitude\":\"-95.991769\",\"distance\":\"0.1718378\"},{\"id\":\"4108\",\"title\":\"Christian Church on the southwest corner of 4th Street and Boulder in 1912\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #A3647.\",\"contentdm_number\":\"4135\",\"latitude\":\"36.152040\",\"longitude\":\"-95.991769\",\"distance\":\"0.1718378\"},{\"id\":\"160\",\"title\":\"4th & Main.\",\"subject\":\"\",\"verified\":\"\",\"relation\":\"Accession #A0112.\",\"contentdm_number\":\"183\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"161\",\"title\":\"4th & Main.\",\"subject\":\"\",\"verified\":\"\",\"relation\":\"Accession #A0219.\",\"contentdm_number\":\"184\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"162\",\"title\":\"4th & Main.\",\"subject\":\"\",\"verified\":\"\",\"relation\":\"Accession #A0886.\",\"contentdm_number\":\"185\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"163\",\"title\":\"4th & Main.\",\"subject\":\"\",\"verified\":\"\",\"relation\":\"Accession #A1129.\",\"contentdm_number\":\"186\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"164\",\"title\":\"4th & Main.\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #A1317.\",\"contentdm_number\":\"187\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"165\",\"title\":\"4th & Main.\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #A1473.\",\"contentdm_number\":\"188\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"166\",\"title\":\"4th & Main\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #A1483\",\"contentdm_number\":\"189\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"167\",\"title\":\"4th & Main, 1909.\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #A2259.\",\"contentdm_number\":\"190\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"180\",\"title\":\"4th St & Main St.\",\"subject\":\"Tulsa (Okla.) -- History; Buildings\",\"verified\":\"\",\"relation\":\"Accession #A0069.\",\"contentdm_number\":\"203\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"181\",\"title\":\"4th St & Main St.\",\"subject\":\"Tulsa (Okla.) -- History; Buildings\",\"verified\":\"\",\"relation\":\"Accession #A0070.\",\"contentdm_number\":\"204\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"6366\",\"title\":\"Fourth And Main\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #E1384.\",\"contentdm_number\":\"6393\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"10055\",\"title\":\"Looking Northwest At Fourth & Main\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #J0405.\",\"contentdm_number\":\"10082\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"13250\",\"title\":\"Planes flying North over downtown Tulsa at 4th & Main Street\",\"subject\":\"Tulsa (Okla.) -- History;Airports -- Oklahoma -- Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #B0016.\",\"contentdm_number\":\"13278\",\"latitude\":\"36.152450\",\"longitude\":\"-95.990649\",\"distance\":\"0.1729403\"},{\"id\":\"4\",\"title\":\"1/2 mile South of Highway 20 North 129 E. Avenue\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #D7202\",\"contentdm_number\":\"26\",\"latitude\":\"36.149745\",\"longitude\":\"-95.959251\",\"distance\":\"0.1867428\"},{\"id\":\"3\",\"title\":\"1/2 Mile South of Highway 20, 1966\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #D7196\",\"contentdm_number\":\"25\",\"latitude\":\"36.149745\",\"longitude\":\"-95.993334\",\"distance\":\"0.1867428\"},{\"id\":\"2\",\"title\":\"Harley Sales\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #D9877\",\"contentdm_number\":\"24\",\"latitude\":\"36.149745\",\"longitude\":\"-95.993334\",\"distance\":\"0.1867428\"},{\"id\":\"1\",\"title\":\"1.1 Miles South of Collinsville, 1960\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #B0798\",\"contentdm_number\":\"23\",\"latitude\":\"36.149745\",\"longitude\":\"-95.993334\",\"distance\":\"0.1867428\"},{\"id\":\"29\",\"title\":\"1st MC Basketball Team\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #C1354\",\"contentdm_number\":\"51\",\"latitude\":\"36.149745\",\"longitude\":\"-95.993334\",\"distance\":\"0.1867428\"},{\"id\":\"28\",\"title\":\"1st MC Basketball Team\",\"subject\":\"Tulsa (Okla.) -- History\",\"verified\":\"\",\"relation\":\"Accession #C0947\",\"contentdm_number\":\"50\",\"latitude\":\"36.149745\",\"longitude\":\"-95.993334\",\"distance\":\"0.1867428\"},{\"id\":\"31\",\"title\":\"1st National Bank after the 1897 fire\",\"subject\":\"Tulsa (Okla.) -- History Banks; Buildings; Fires; Indian Territory\",\"verified\":\"\",\"relation\":\"Accession #A3557.\",\"contentdm_number\":\"54\",\"latitude\":\"36.149745\",\"longitude\":\"-95.993334\",\"distance\":\"0.1867428\"},{\"id\":\"38\",\"title\":\"1st National Bank, 1912\",\"subject\":\"Tulsa (Okla.) -- History Banks; Buildings\",\"verified\":\"\",\"relation\":\"Accession #A3500.\",\"contentdm_number\":\"61\",\"latitude\":\"36.149745\",\"longitude\":\"-95.993334\",\"distance\":\"0.1867428\"}]}";
	}
	
}
