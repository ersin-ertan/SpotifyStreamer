package com.nullcognition.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


public class ArtistListActivity extends FragmentActivity
		implements FragmentArtistList.OnRecyclerViewItemClick{

	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_list);

		if(findViewById(R.id.artist_detail_container) != null){
			mTwoPane = true;
		}

	}

	@Override
	public void positionClicked(final int position){
		if(mTwoPane){
			Fragment fragment = new RecyclerViewFragmentBuilder(true).build(); // should this recyclerview be in an artist detail container to replace its own container on each click
			getSupportFragmentManager().beginTransaction()
			                           .replace(R.id.artist_detail_container, fragment)
			                           .commit();
			// if was .add() then could not garbage collector
			// single screen ArtistDetailActivity only add()'s once, rotation does not add more
			// must be taken care of by the fragment manager

		}
		else{
			Intent detailIntent = new Intent(this, ArtistDetailActivity.class);
			startActivity(detailIntent);
		}

	}
}
