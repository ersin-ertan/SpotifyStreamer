package com.nullcognition.spotifystreamer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class IntentServiceArtistSearch extends IntentService{

	private static final String ACTION_SEARCH_ARTIST_NAME = "com.nullcognition.spotifystreamer.action.SEARCH_ARTIST_NAME";
	private static final String EXTRA_ARTIST_NAME = "com.nullcognition.spotifystreamer.extra.ARTIST_NAME";

	public static void searchByArtistName(Context context, String artistName){
		Intent intent = new Intent(context, IntentServiceArtistSearch.class);
		intent.setAction(ACTION_SEARCH_ARTIST_NAME);
		intent.putExtra(EXTRA_ARTIST_NAME, artistName);
		context.startService(intent);
	}

	public IntentServiceArtistSearch(){ super("IntentServiceArtistSearch");}

	@Override
	protected void onHandleIntent(Intent intent){
		if(intent != null){
			final String action = intent.getAction();
			initSpotify();

			if(ACTION_SEARCH_ARTIST_NAME.equals(action)){
				final String artistName = intent.getStringExtra(EXTRA_ARTIST_NAME);
				searchByArtistName(artistName);
			}
		}
	}

	private void searchByArtistName(String artistName){
		if(artistName != null){
			EventBus.getDefault().post(spotifyService.searchArtists(artistName));
		}
	}

	private SpotifyApi spotifyApi;
	private SpotifyService spotifyService;

	private void initSpotify(){
		if(spotifyService == null){
			spotifyApi = new SpotifyApi();
			spotifyService = spotifyApi.getService();
		}
	}
}

//		HashMap<String, ArrayList<Image>> hm = new HashMap<>();
//		ArrayList<Image> ar = new ArrayList<Image>();
//		Image i = new Image();
//		i.height = 64;
//		i.width = 64;
//		i.url = "http://people.mozilla.org/~faaborg/files/shiretoko/firefoxIcon/firefox-64-noshadow.png";
//		ar.add(i);
//		ar.add(i);
//		ar.add(i);
//		ar.add(i); // need 4
//		hm.put("test", ar);
//		ArtistListItemData a = new ArtistListItemData(hm);
//		EventBus.getDefault().post(a);
