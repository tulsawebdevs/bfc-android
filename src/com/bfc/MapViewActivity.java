package com.bfc;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MapViewActivity extends MapActivity {
	private int currentLat;
	private int currentLong;
	private boolean firstRun = true;
	private LocationManager myLocationManager;
	private LocationListener myLocationListener;

	private MapView myMapView;
	private MapController myMapController;

	private void CenterLocation(GeoPoint centerGeoPoint) {
		myMapController.animateTo(centerGeoPoint);
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		myMapView = (MapView) findViewById(R.id.mapview);
		myMapController = myMapView.getController();
		myMapController.setZoom(20); // Fixed Zoom Level
		// Bundle bundle = this.getIntent().getExtras();
		int Mode = 1;// bundle.getInt("Mode");
		myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		myLocationListener = new MyLocationListener();

		myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				0, 0, myLocationListener);
		if (Mode == 1) {
			// int intLatitude = bundle.getInt("Latitude");
			// int intLongitude = bundle.getInt("Longitude");
			// GeoPoint initGeoPoint = new GeoPoint(intLatitude, intLongitude);
			// CenterLocation(initGeoPoint);

			// marker.setBounds(0, 0, marker.getIntrinsicWidth(),
			// marker.getIntrinsicHeight());

		}
	}

	private class MyLocationListener implements LocationListener {
		public static final int Threshold = 5000;

		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			int updateLat = (int) (argLocation.getLatitude() * 1000000);
			int updateLong = (int) (argLocation.getLongitude() * 1000000);
			if (firstRun || Math.abs(updateLat - currentLat) > Threshold
					|| Math.abs(updateLong - currentLat) > Threshold) {
				currentLat = updateLat;
				currentLong = updateLong;
				GeoPoint myGeoPoint = new GeoPoint(updateLat, updateLong);
				myMapView.getOverlays().clear();
				
				if (firstRun) {
					CenterLocation(myGeoPoint);
					firstRun = false;
				}
				Drawable marker = getResources().getDrawable(
						android.R.drawable.ic_menu_myplaces);
				myMapView.getOverlays().add(
						new InterestingLocations(marker, currentLat,
								currentLong));
				myMapView.invalidate();

			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	class InterestingLocations extends ItemizedOverlay<OverlayItem> {

		private List<OverlayItem> locations = new ArrayList<OverlayItem>();
		private Drawable marker;

		public InterestingLocations(Drawable defaultMarker, int LatitudeE6,
				int LongitudeE6) {
			super(defaultMarker);
			// TODO Auto-generated constructor stub
			this.marker = defaultMarker;
			// create locations of interest
			GeoPoint myPlace = new GeoPoint(LatitudeE6, LongitudeE6);
			locations.add(new OverlayItem(myPlace, "My Place", "My Place"));
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return locations.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return locations.size();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			super.draw(canvas, mapView, shadow);

			boundCenterBottom(marker);
		}
	}
}
