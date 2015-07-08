package com.nullcognition.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ArtistTop10Activity extends AppCompatActivity{

	public static void startActivity(Context context, Bundle bundle){
		Intent intent = new Intent(context, ArtistTop10Activity.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_top10);
	}
	@Override
	protected void onStart(){
		super.onStart();
		ArtistTop10ActivityFragment fragment = (ArtistTop10ActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
		String artistId = getIntent().getStringExtra(IntentServiceArtistSearch.EXTRA_ARTIST_ID);
		fragment.getTopTracks(artistId);
	}

}
