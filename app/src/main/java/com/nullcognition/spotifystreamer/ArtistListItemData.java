package com.nullcognition.spotifystreamer;// Created by ersin on 03/07/15

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.models.Image;

public class ArtistListItemData{

	private final HashMap<String, ArrayList<Image>> artistsAndImages;

	public ArtistListItemData(final HashMap<String, ArrayList<Image>> artistsAndImages){
		this.artistsAndImages = artistsAndImages;
	}

	public ArrayList<ArtistListItem> getArrayOfArtistListItems(){
		ArrayList<ArtistListItem> artistListItems = new ArrayList<ArtistListItem>();
		for(HashMap.Entry<String, ArrayList<Image>> entry : artistsAndImages.entrySet()){
			artistListItems.add(new ArtistListItem(entry.getKey(), entry.getValue()));
		}
		return artistListItems;
	}
}
