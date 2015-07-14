package com.nullcognition.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import icepick.Icepick;
import icepick.Icicle;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;


@IntentBuilder
public class ActivityArtistDetail extends AppCompatActivity{

	// add @Nullable if you want it in the separate builder method
	@Extra
	@Icicle
	String title;

	@Icicle
	String c = "Var C Init";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ActivityArtistDetailIntentBuilder.inject(getIntent(), this);
		Icepick.restoreInstanceState(this, savedInstanceState); // order matters, ^ restore must come after
		setContentView(R.layout.activity_artist_detail);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if(savedInstanceState == null){
			Fragment fragment = new FragmentArtistDetailBuilder().build();

			getSupportFragmentManager().beginTransaction()
			                           .add(R.id.artist_detail_container, fragment)
			                           .commit();
		}

		Toast.makeText(ActivityArtistDetail.this, "Title: " + c + "-" + title, Toast.LENGTH_LONG).show();

		title = "Title Saved";
		c = "Var C Saved";
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
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

}
