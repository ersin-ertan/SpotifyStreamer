package com.nullcognition.spotifystreamer;// Created by ersin on 03/07/15

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Image;

public class ArtistListItem{

	private String artistName;
	private ArrayList<Image> top4Images;

	public ArtistListItem(String artistName, ArrayList<Image> top4Images){
		this.artistName = artistName;
		this.top4Images = top4Images;
	}

	public String getArtistName(){
		return artistName;
	}
	public ArrayList<Image> getTop4Images(){
		return top4Images;
	}

}
