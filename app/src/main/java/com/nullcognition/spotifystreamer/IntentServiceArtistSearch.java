package com.nullcognition.spotifystreamer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class IntentServiceArtistSearch extends IntentService{

	public static final String EXTRA_ARTIST_NAME = "com.nullcognition.spotifystreamer.extra.ARTIST_NAME";
	public static final String EXTRA_ARTIST_ID = "com.nullcognition.spotifystreamer.extra.ARTIST_ID";
	private static final String ACTION_SEARCH_ARTIST_NAME = "com.nullcognition.spotifystreamer.action.SEARCH_ARTIST_NAME";
	private static final String ACTION_SEARCH_ARTIST_TOP_10 = "com.nullcognition.spotifystreamer.action.SEARCH_ARTIST_TOP_10";
	private SpotifyService spotifyService;

	public IntentServiceArtistSearch(){ super("IntentServiceArtistSearch");}
	public static void searchByArtistName(Context context, String artistName){
		Intent intent = new Intent(context, IntentServiceArtistSearch.class);
		intent.setAction(ACTION_SEARCH_ARTIST_NAME);
		intent.putExtra(EXTRA_ARTIST_NAME, artistName);
		context.startService(intent);
	}
	public static void searchArtistTop10(Context context, String artistId){
		Intent intent = new Intent(context, IntentServiceArtistSearch.class);
		intent.setAction(ACTION_SEARCH_ARTIST_TOP_10);
		intent.putExtra(EXTRA_ARTIST_ID, artistId);
		context.startService(intent);
	}
	@Override
	protected void onHandleIntent(Intent intent){
		if(intent != null){
			final String action = intent.getAction();
			initSpotify();

			if(ACTION_SEARCH_ARTIST_NAME.equals(action)){
				final String artistName = intent.getStringExtra(EXTRA_ARTIST_NAME);
				searchByArtistName(artistName);
			}

			if(ACTION_SEARCH_ARTIST_TOP_10.equals(action)){
				final String artistId = intent.getStringExtra(EXTRA_ARTIST_ID);
				searchArtistTop10(artistId);
			}
		}
	}
	private void searchArtistTop10(String artistId){
		if(artistId != null){
			HashMap<String, Object> countryCode = new HashMap<String, Object>();
			countryCode.put("country", "CA");
			EventBus.getDefault().postSticky(spotifyService.getArtistTopTrack(artistId, countryCode));
		}
	}
	private void searchByArtistName(String artistName){
		if(artistName != null){
			EventBus.getDefault().postSticky(spotifyService.searchArtists(artistName));
		}
	}
	private void initSpotify(){
		if(spotifyService == null){
			spotifyService = new SpotifyApi().getService();
		}
	}
}
