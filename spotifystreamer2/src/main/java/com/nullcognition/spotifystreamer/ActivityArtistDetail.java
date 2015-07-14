package com.nullcognition.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ActivityArtistDetail extends AppCompatActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_detail);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//		if(savedInstanceState == null){
//			Fragment fragment = new RecyclerViewFragmentBuilder(60).build();
//
//			getSupportFragmentManager().beginTransaction()
//			                           .add(R.id.artist_detail_container, fragment)
//			                           .commit();
//		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if(id == android.R.id.home){
			NavUtils.navigateUpTo(this, new Intent(this, ActivityArtistList.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
