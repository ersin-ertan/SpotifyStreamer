package com.nullcognition.spotifystreamer.refactoredArch;// Created by ersin on 13/07/15

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import auto.parcel.AutoParcel;
import kaaes.spotify.webapi.android.models.Artist;

@AutoParcel
public abstract class ParcelListArtist implements Parcelable{


	abstract String name();
	abstract List<Artist> artistList();

	static ParcelListArtist create(String name, List<Artist> artistList) {
		return new AutoParcel_ParcelListArtist(name, artistList);
	}

}
