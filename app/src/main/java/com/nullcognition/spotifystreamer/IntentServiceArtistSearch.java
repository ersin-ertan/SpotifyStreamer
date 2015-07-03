package com.nullcognition.spotifystreamer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

public class IntentServiceArtistSearch extends IntentService{

	private static final String ACTION_SEARCH_ARTIST_NAME = "com.nullcognition.spotifystreamer.action.SEARCH_ARTIST_NAME";
	private static final String EXTRA_ARTIST_NAME = "com.nullcognition.spotifystreamer.extra.ARTIST_NAME";

	public static void searchByArtistName(Context context, String artistName){
		Intent intent = new Intent(context, IntentServiceArtistSearch.class);
		intent.setAction(ACTION_SEARCH_ARTIST_NAME);
		intent.putExtra(EXTRA_ARTIST_NAME, artistName);
		context.startService(intent);
	}

	public IntentServiceArtistSearch(){
		super("IntentServiceArtistSearch");
	}

	@Override
	protected void onHandleIntent(Intent intent){
		if(intent != null){
			final String action = intent.getAction();
			if(ACTION_SEARCH_ARTIST_NAME.equals(action)){
				final String artistName = intent.getStringExtra(EXTRA_ARTIST_NAME);
				searchByArtistName(artistName);
			}
		}
	}

	// Get the artists for the search and 4 images associated with them
	private void searchByArtistName(String artistName){
		SpotifyApi api = new SpotifyApi();
		SpotifyService service = api.getService();

		if(artistName != null){
			ArtistsPager results = service.searchArtists(artistName);
			HashMap<String, ArrayList<Image>> artistsAndImages = new HashMap<>();

			ArrayList<Artist> artists = (ArrayList<Artist>) results.artists.items;

			for(Artist a : artists){
				artistsAndImages.put(a.name, (ArrayList<Image>) a.images);
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

			EventBus.getDefault().post(new ArtistListItemData(artistsAndImages));
		}
	}

}
