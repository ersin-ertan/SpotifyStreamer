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
		}

	}

	@Override
	public void onItemSelected(String id){
		if(mTwoPane){
			Fragment fragment = new RecyclerViewFragmentBuilder(30).build();

			// if was .add() then could not GC
			// single screen ArtistDetailActivity only add()'s once, rotation does not add more
			// must be taken care of by the FM
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

	@Override
	public void onBackPressed(){
		getFragmentManager().popBackStack();
//		super.onBackPressed();
	}
}
