package com.nullcognition.spotifystreamer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.MainFragToActivity{

	private boolean twoPane;
	private static final String TOP_TEN = "topTen";

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

	ArtistTop10ActivityFragment fragment;

	@Override
	protected void onStop(){
		super.onStop();
	}
	@Override
	public void onStart(){
		super.onStart();
		if(twoPane){
			fragment = (ArtistTop10ActivityFragment) getSupportFragmentManager().findFragmentByTag(TOP_TEN);
			// why are these null, because findFragmentById(R.id.fragment) was called instead of the dynamically added fragments tag
			// id is for statically introduced fragments, tags are for dynamic, unless you can give each other a tag/id
		}
	}

	private static String lastItemClickedId = null;
	@Override
	public void onConfigurationChanged(final Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		if(lastItemClickedId != null){
			if(fragment == null){
				fragment = (ArtistTop10ActivityFragment) getSupportFragmentManager().findFragmentByTag(TOP_TEN);
			}
			fragment.getTopTracks(lastItemClickedId);
		}
	}
	@Override
	protected void onResume(){
		super.onResume();
		if(twoPane){
			fragment = (ArtistTop10ActivityFragment) getSupportFragmentManager().findFragmentByTag(TOP_TEN);
		}
	}


	@Override
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
