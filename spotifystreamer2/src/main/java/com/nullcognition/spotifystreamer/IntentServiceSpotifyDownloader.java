package com.nullcognition.spotifystreamer;// Created by ersin on 14/07/15

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.paperdb.Paper;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
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

	public static void startService(String searchAction, @Nullable String searchQuery, Context context){

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
				checkForRepeatedSearch(searchQuery, searchAction);
			}
			else if(ACTION_SEARCH_ARTIST_TOP_TEN_TRACKS.equals(searchAction)){
				checkForRepeatedSearch(Paper.get(PaperProducts.LAST_ARTIST_SEARCH, "default"), searchAction);
				// default needed for type inference
			}
		}
	}

	private void checkForRepeatedSearch(String searchQuery, final String searchAction){
		if(searchQuery != null){
			if(ACTION_SEARCH_ARTIST_NAME.equals(searchAction)){
				if(!searchQuery.equals(Paper.get(PaperProducts.LAST_ARTIST_SEARCH, null))){
					searchByArtistName(searchQuery);
				}
			}
			else if(ACTION_SEARCH_ARTIST_TOP_TEN_TRACKS.equals(searchAction)){
				searchQuery = Paper.get(PaperProducts.LAST_ARTIST_SEARCH);
				if(!searchQuery.equals(Paper.get(PaperProducts.LAST_TRACK_SEARCH, null))){
					searchArtistsTopTenTracks(Paper.get(PaperProducts.ARTIST_ID, "default"));
				}
			}
		}
	}

	private void searchArtistsTopTenTracks(final String searchedArtistId){
		if(searchedArtistId != null){
			HashMap<String, Object> countryCode = new HashMap<String, Object>();
			countryCode.put("country", "CA");
			try{

				Tracks tracks = spotifyService.getArtistTopTrack(searchedArtistId, countryCode);
				Paper.put(PaperProducts.TRACK_LIST, tracks.tracks);
				Paper.put(PaperProducts.LAST_TRACK_SEARCH, searchQuery);
				EventBus.getDefault().post(new SearchArtistsTopTenTracks(searchQuery));
			}
			catch(Exception e){ Log.e("logErr", "Retrofit return error value searchArtistTop10");}
		}
	}

	private void searchByArtistName(String searchQuery){
		try{
			ArtistsPager artistsPager = spotifyService.searchArtists(searchQuery);
			if(artistsPager != null){
				Paper.put(PaperProducts.ARTIST_LIST, artistsPager.artists.items);
				Paper.put(PaperProducts.LAST_ARTIST_SEARCH, searchQuery);
				EventBus.getDefault().post(new SearchByArtistName(searchQuery));
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

	public static class SearchByArtistName{
		protected SearchByArtistName(String artistName){this.artistName = artistName;}
		public String artistName;
	}

	public static class SearchArtistsTopTenTracks{
		protected SearchArtistsTopTenTracks(String artistName){this.artistName = artistName;}
		public String artistName;
	}
}
