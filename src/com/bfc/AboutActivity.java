package com.bfc;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class AboutActivity extends Activity {
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.about);
		 TextView textView = (TextView)findViewById(R.id.content);
	     textView.setMovementMethod(ScrollingMovementMethod.getInstance());
	 }
}
