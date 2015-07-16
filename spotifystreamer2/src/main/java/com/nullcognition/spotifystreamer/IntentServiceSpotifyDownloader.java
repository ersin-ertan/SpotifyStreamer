package com.nullcognition.spotifystreamer;// Created by ersin on 14/07/15

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import de.greenrobot.event.EventBus;
import io.paperdb.Paper;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Tracks;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class IntentServiceSpotifyDownloader extends IntentService{

	public static final String ACTION_SEARCH_ARTIST_TOP_TEN_TRACKS = "com.nullcognition.spotifystreamer.action.SEARCH_ARTIST_TOP_TEN_TRACKS";
	public static final String ACTION_SEARCH_ARTIST_NAME = "com.nullcognition.spotifystreamer.action.SEARCH_ARTIST_NAME";

	@Extra
	public String searchAction;

	@Extra
	public String searchQuery;

	private SpotifyService spotifyService;

	public static void startService(String searchAction, String searchQuery, Context context){

		if(isConnectedToInternet(context)){
			context.startService(new IntentServiceSpotifyDownloaderIntentBuilder(searchAction, searchQuery)
					.build(context));
		}
		else{ Toast.makeText(context, "Connect to the internet!", Toast.LENGTH_SHORT).show();}
	}

	@Override
	protected void onHandleIntent(final Intent intent){
		IntentServiceSpotifyDownloaderIntentBuilder.inject(intent, this);

		if(intent != null){
			initSpotify();
			if(ACTION_SEARCH_ARTIST_NAME.equals(searchAction)){
				checkForReapeatedSearch(searchQuery);
			}
			else if(ACTION_SEARCH_ARTIST_TOP_TEN_TRACKS.equals(searchAction)){
				searchArtistsTopTenTracks(searchQuery);
			}
		}
	}

	private void searchArtistsTopTenTracks(final String searchQuery){
		if(searchQuery != null){

			HashMap<String, Object> countryCode = new HashMap<String, Object>();
			countryCode.put("country", "CA");

			try{
				Tracks tracks = spotifyService.getArtistTopTrack(searchQuery, countryCode);
				if(tracks != null){ EventBus.getDefault().post(tracks);}
			}
			catch(Exception e){ Log.e("logErr", "Retrofit return error value searchArtistTop10");}
		}
	}

	private void checkForReapeatedSearch(final String searchQuery){
		if(searchQuery != null){
			if(!searchQuery.equals(Paper.get(PaperProducts.LAST_ARTIST_SEARCH, null))){
				searchByArtistName(searchQuery);
			}
		}
	}

	private void searchByArtistName(String searchQuery){
		try{
			ArtistsPager artistsPager = spotifyService.searchArtists(searchQuery);
			if(artistsPager != null){
				Paper.put(PaperProducts.ARTIST_LIST, artistsPager.artists.items);
				Paper.put(PaperProducts.LAST_ARTIST_SEARCH, searchQuery);
				EventBus.getDefault().post(searchQuery);
			}
		}
		catch(Exception e){
			Log.e("logErr", "Retrofit return error value searchArtistName");
		}
	}

	private static boolean isConnectedToInternet(Context context){
		NetworkInfo activeNetwork =
				((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
						.getActiveNetworkInfo();

		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	private void initSpotify(){
		if(spotifyService == null){ spotifyService = new SpotifyApi().getService();}
	}

	public IntentServiceSpotifyDownloader(){super(IntentServiceSpotifyDownloader.class.getSimpleName());}
	public IntentServiceSpotifyDownloader(final String name){super(name);}
}
