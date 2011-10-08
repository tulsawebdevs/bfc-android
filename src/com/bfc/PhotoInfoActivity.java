package com.bfc;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class PhotoInfoActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_info);
        TextView textView = (TextView)findViewById(R.id.pictureInfo);
    	textView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
	
}
