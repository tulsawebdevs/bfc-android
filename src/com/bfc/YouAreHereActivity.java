package com.bfc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class YouAreHereActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.you_are_here);
        
        ListView list = (ListView) findViewById(R.id.imageList);
        QrimpHttpAdapter qha = new QrimpHttpAdapter(this, 99, 99);
        list.setAdapter(qha);
        list.forceLayout();
    }
}
