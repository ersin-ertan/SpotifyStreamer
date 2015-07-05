package com.nullcognition.spotifystreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 A placeholder fragment containing a simple view.
 */
public class ArtistTop10ActivityFragment extends Fragment{

	public ArtistTop10ActivityFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_artist_top10, container, false);
	}
}
