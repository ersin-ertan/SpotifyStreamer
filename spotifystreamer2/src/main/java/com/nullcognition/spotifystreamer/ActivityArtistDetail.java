package com.nullcognition.spotifystreamer;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.List;

import io.paperdb.Paper;
import kaaes.spotify.webapi.android.models.Track;
import se.emilsjolander.intentbuilder.IntentBuilder;


@IntentBuilder
public class ActivityArtistDetail extends AppCompatActivity implements FragmentArtistDetail.SubtitleListener{

	// @Extra String s; add @Nullable if you want it in the separate builder method
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ActivityArtistDetailIntentBuilder.inject(getIntent(), this);
//		Icepick.restoreInstanceState(this, savedInstanceState); // order matters, ^ restore must come after
		setContentView(R.layout.activity_artist_detail);

		if(getSupportActionBar() != null){ getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

		if(savedInstanceState == null){
			Fragment fragment = new FragmentArtistDetailBuilder(false).build();
			getSupportFragmentManager().beginTransaction()
			                           .add(R.id.artist_detail_container, fragment)
			                           .commit();
		}
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
	@Override
	public void changeSubtitle(){

		android.support.v7.app.ActionBar ab = getSupportActionBar();
		if(ab != null){
			List<Track> tracks1 = ((List<Track>) Paper.get(PaperProducts.TRACK_LIST));
			if(!tracks1.isEmpty()){
				ab.setSubtitle(tracks1.get(0).artists.get(0).name);
			}
		}
	}
}
