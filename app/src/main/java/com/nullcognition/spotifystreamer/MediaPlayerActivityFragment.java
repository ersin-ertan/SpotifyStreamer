package com.nullcognition.spotifystreamer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.models.Track;

public class MediaPlayerActivityFragment extends Fragment{

	private Track track;

	public MediaPlayerActivityFragment(){}

	static public MediaPlayerActivityFragment newInstance(final Track track){
		MediaPlayerActivityFragment m = new MediaPlayerActivityFragment();
		m.track = track;
		return m;
	}
	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

		TextView trackName = (TextView) rootView.findViewById(R.id.txt_track_name);
		TextView trackAlbum = (TextView) rootView.findViewById(R.id.txt_tracks_album);
		ImageView imageAlbum = (ImageView) rootView.findViewById(R.id.image_album);
		TextView artistName = (TextView) rootView.findViewById(R.id.txt_artist_name);
		TextView trackDuration = (TextView) rootView.findViewById(R.id.txt_track_duration);

		trackName.setText(track.name);
		trackAlbum.setText(track.album.name);

		int imageListSize = track.album.images.size();
		String imageUrl = null;
		if(imageListSize != 0){
			imageUrl = track.album.images.get(0).url; // need the biggest picture, as it is the main screen
		}
		Picasso.with(getActivity()).load(imageUrl).placeholder(R.drawable.logo_spotify).fit().into(imageAlbum);


		String artiName = track.artists.get(0).name;
		for(int i = 1; i < track.artists.size(); ++i){
			artiName += ", " + track.artists.get(i).name;
		}
		artistName.setText(artiName);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.font_type));
		artistName.setTypeface(font);
		trackAlbum.setTypeface(font);
		trackName.setTypeface(font);
		trackDuration.setTypeface(font);


		long longTrackDur = track.duration_ms;
		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(longTrackDur),
				TimeUnit.MILLISECONDS.toMinutes(longTrackDur) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(longTrackDur) % TimeUnit.MINUTES.toSeconds(1));
		trackDuration.setText(hms);


		return rootView;
	}
}
