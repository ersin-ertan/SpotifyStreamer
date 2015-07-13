package com.nullcognition.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


public class ArtistListActivity extends FragmentActivity
		implements ArtistListFragment.Callbacks{

	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_list);

		if(findViewById(R.id.artist_detail_container) != null){
			mTwoPane = true;

			((ArtistListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.artist_list))
					.setActivateOnItemClick(true);
		}

	}

	@Override
	public void onItemSelected(String id){
		if(mTwoPane){
			Fragment fragment = new ArtistDetailFragmentBuilder(id).build();

			getSupportFragmentManager().beginTransaction()
			                           .replace(R.id.artist_detail_container, fragment)
			                           .commit();

		}
		else{
			Intent detailIntent = new Intent(this, ArtistDetailActivity.class);
			detailIntent.putExtra(ArtistDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
