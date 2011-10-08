package com.bfc;

import android.app.SearchableInfo;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost tabHost= getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        
        intent=new Intent().setClass(this,YouAreHereActivity.class);
        spec=tabHost.newTabSpec("youAreHere")
        			.setIndicator("Here")
        			.setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this,SearchViewActivity.class);
        spec=tabHost.newTabSpec("search")
        			.setIndicator("Search")
        			.setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(this,AboutActivity.class);
        spec=tabHost.newTabSpec("about")
        			.setIndicator("About")
        			.setContent(intent);
        tabHost.addTab(spec);

        
    }
}