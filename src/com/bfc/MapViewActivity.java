package com.bfc;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class MapViewActivity extends MapActivity {
	private MapView myMapView;
	private MapController myMapController;
	private com.google.android.maps.MyLocationOverlay mlol;

	private void CenterLocation(GeoPoint centerGeoPoint) {
		myMapController.animateTo(centerGeoPoint);
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		myMapView = (MapView) findViewById(R.id.mapview);
		mlol = new MyLocationOverlay(this, myMapView);
		myMapController = myMapView.getController();
		myMapController.setZoom(20); // Fixed Zoom Level
		// Bundle bundle = this.getIntent().getExtras();
		// myLocationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// myLocationListener = new MyLocationListener();
		List<Overlay> list = myMapView.getOverlays();
		list.add(mlol);
		// myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 0, 0, myLocationListener);
		mlol.runOnFirstFix(new Runnable() {
			public void run() {
				CenterLocation(mlol.getMyLocation());
			}
		});
		Drawable marker = getResources().getDrawable(
				android.R.drawable.ic_menu_myplaces);
		Locations itemizedoverlay = new Locations(marker, this);
		GeoPoint point = new GeoPoint(19240000, -99120000);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!",
				"I'm in Mexico City!");
		itemizedoverlay.addOverlay(overlayitem);
		list.add(itemizedoverlay);
	}

	@Override
	public void onResume() {
		super.onResume();
		mlol.enableMyLocation();
		mlol.enableCompass();
	}

	@Override
	public void onPause() {
		super.onPause();
		mlol.disableMyLocation();
		mlol.disableCompass();
	}

	/*
	 * private class MyLocationListener implements LocationListener { public
	 * static final int Threshold = 500000; public int index;
	 * 
	 * public void onLocationChanged(Location argLocation) { // TODO
	 * Auto-generated method stub int updateLat = (int)
	 * (argLocation.getLatitude() * 1000000); int updateLong = (int)
	 * (argLocation.getLongitude() * 1000000); if (firstRun ||
	 * Math.abs(updateLat - currentLat) > Threshold || Math.abs(updateLong -
	 * currentLat) > Threshold) { currentLat = updateLat; currentLong =
	 * updateLong; GeoPoint myGeoPoint = new GeoPoint(updateLat, updateLong);
	 * Drawable marker = getResources().getDrawable(
	 * android.R.drawable.ic_menu_myplaces); if (firstRun) {
	 * CenterLocation(myGeoPoint); firstRun = false; } else {
	 * myMapView.getOverlays().remove(hereMarker); } hereMarker = new
	 * Locations(marker, currentLat, currentLong);
	 * myMapView.getOverlays().add(hereMarker); myMapView.invalidate();
	 * 
	 * } }
	 * 
	 * public void onProviderDisabled(String provider) { // TODO Auto-generated
	 * method stub }
	 * 
	 * public void onProviderEnabled(String provider) { // TODO Auto-generated
	 * method stub }
	 * 
	 * public void onStatusChanged(String provider, int status, Bundle extras) {
	 * // TODO Auto-generated method stub } }
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	class Locations extends ItemizedOverlay<OverlayItem> {

		private List<OverlayItem> LocaOverlays = new ArrayList<OverlayItem>();
		private Context mContext;

		public Locations(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mContext = context;
		}

		public void addOverlay(OverlayItem overlay) {
			LocaOverlays.add(overlay);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return LocaOverlays.get(i);
		}

		@Override
		public int size() {
			return LocaOverlays.size();
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = LocaOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
			return true;
		}
	}

}
