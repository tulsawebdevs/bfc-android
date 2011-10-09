package com.bfc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

public class YouAreHereActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.you_are_here);
        Handler mHandler = new Handler();
        ListView list = (ListView) findViewById(R.id.imageList);
        QrimpHttpAdapter qha = new QrimpHttpAdapter(mHandler,this, 99, 99);
        list.setAdapter(qha);
        list.forceLayout();
    }
}
