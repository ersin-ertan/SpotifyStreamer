package com.nullcognition.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import se.emilsjolander.intentbuilder.IntentBuilder;


@IntentBuilder
public class ActivityArtistList extends FragmentActivity
		implements FragmentArtistList.OnRecyclerViewItemClick{

	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_list);

		startService(new IntentServiceSpotifyDownloaderIntentBuilder("http://google.com").build(this));

		if(findViewById(R.id.artist_detail_container) != null){mTwoPane = true;}


	}

	@Override
	public void positionClicked(final int position){

		if(mTwoPane){
			Fragment fragment = new FragmentArtistDetailBuilder().build(); // should this recyclerview be in an artist detail container to replace its own container on each click
			getSupportFragmentManager().beginTransaction()
			                           .replace(R.id.artist_detail_container, fragment)
			                           .commit();
			// if was .add() then could not garbage collector
			// single screen ActivityArtistDetail only add()'s once, rotation does not add more
			// must be taken care of by the fragment manager

		}
		else{
			startActivity(new ActivityArtistDetailIntentBuilder("Title Passed In").build(this));
//			Intent detailIntent = new Intent(this, ActivityArtistDetail.class);
//			startActivity(detailIntent);
		}

	}
}
