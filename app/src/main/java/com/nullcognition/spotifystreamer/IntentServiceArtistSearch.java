package com.nullcognition.spotifystreamer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Tracks;

public class IntentServiceArtistSearch extends IntentService{

	public static final String EXTRA_ARTIST_NAME = "com.nullcognition.spotifystreamer.extra.ARTIST_NAME";
	public static final String EXTRA_ARTIST_ID = "com.nullcognition.spotifystreamer.extra.ARTIST_ID";
	private static final String ACTION_SEARCH_ARTIST_NAME = "com.nullcognition.spotifystreamer.action.SEARCH_ARTIST_NAME";
	private static final String ACTION_SEARCH_ARTIST_TOP_10 = "com.nullcognition.spotifystreamer.action.SEARCH_ARTIST_TOP_10";
	private SpotifyService spotifyService;

	public IntentServiceArtistSearch(){ super("IntentServiceArtistSearch");}
	public static void searchByArtistName(Context context, String artistName){
		if(checkInternetConnectivity(context)){
			Intent intent = new Intent(context, IntentServiceArtistSearch.class);
			intent.setAction(ACTION_SEARCH_ARTIST_NAME);
			intent.putExtra(EXTRA_ARTIST_NAME, artistName);
			context.startService(intent);
		}
	}
	public static void searchArtistTop10(Context context, String artistId){
		if(checkInternetConnectivity(context)){
			Intent intent = new Intent(context, IntentServiceArtistSearch.class);
			intent.setAction(ACTION_SEARCH_ARTIST_TOP_10);
			intent.putExtra(EXTRA_ARTIST_ID, artistId);
			context.startService(intent);
		}
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

	private static boolean checkInternetConnectivity(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		if(!isConnected){
			Toast.makeText(context, "Connect to the internet!", Toast.LENGTH_SHORT).show();
		}

		return isConnected;
	}
	private void searchArtistTop10(String artistId){
		if(artistId != null){
			HashMap<String, Object> countryCode = new HashMap<String, Object>();
			countryCode.put("country", "CA");
			try{
				Tracks tracks = spotifyService.getArtistTopTrack(artistId, countryCode);
				if(tracks != null){
					EventBus.getDefault().postSticky(tracks);
				}
			}
			catch(Exception e){
				Log.e("logErr", "Retrofit return error value from searchArtistTop10");
			}
		}
	}
	private void searchByArtistName(String artistName){
		if(artistName != null){
			try{
				ArtistsPager ap = spotifyService.searchArtists(artistName);
				if(ap != null){

					EventBus.getDefault().postSticky(ap);
				}
			}
			catch(Exception e){
				Log.e("logErr", "Retrofit return error value from searchArtistName");
			}
		}
	}
	private void initSpotify(){
		if(spotifyService == null){
			spotifyService = new SpotifyApi().getService();
		}
	}
}
