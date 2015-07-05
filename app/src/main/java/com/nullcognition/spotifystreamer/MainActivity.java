package com.nullcognition.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}

	@Override
	public void onStart(){
		super.onStart();
	}

	@Override
	protected void onStop(){
		super.onStop();
	}

}
