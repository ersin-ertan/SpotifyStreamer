package com.nullcognition.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.MainFragToActivity{

	private boolean twoPane;
	private static final String TOP_TEN = "topTen";
	private static String lastItemClickedId = null;
	private ArtistTop10ActivityFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(findViewById(R.id.frame_detail_container) != null){
			twoPane = true;
			if(savedInstanceState == null){
				getSupportFragmentManager().beginTransaction()
				                           .replace(R.id.frame_detail_container, new ArtistTop10ActivityFragment(), TOP_TEN)
				                           .commit();
			}
		}
		else{ twoPane = false;}
	}

	@Override
	protected void onResume(){
		super.onResume();
		if(twoPane){
			fragment = (ArtistTop10ActivityFragment) getSupportFragmentManager().findFragmentByTag(TOP_TEN);
		}
	}

	public void listItemClicked(final String id){
		if(twoPane){
			lastItemClickedId = id;
			fragment.getTopTracks(id);
		}
		else{
			lastItemClickedId = null;
			Bundle bundle = new Bundle();
			bundle.putString(IntentServiceArtistSearch.EXTRA_ARTIST_ID, id);
			ArtistTop10Activity.startActivity(this, bundle);
		}
	}
}
