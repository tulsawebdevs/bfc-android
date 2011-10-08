package com.bfc;

import com.google.android.maps.MapActivity;
import android.os.Bundle;

public class MapViewActivity extends MapActivity {
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.map_view);
	 }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
