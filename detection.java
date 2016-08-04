package com.electronic_market.activity;

import com.electronic_market.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
public class detection extends Activity {
	private TextView text;
	private Button back;
	private String ss;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detection);
		text=(TextView)findViewById(R.id.textView1);
		back=(Button)findViewById(R.id.button1);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Intent intent = getIntent();   
		ss=intent.getStringExtra("String"); 
		text.setText(ss);
	}

}
