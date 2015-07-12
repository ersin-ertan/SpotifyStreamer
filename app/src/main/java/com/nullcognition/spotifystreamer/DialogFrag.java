package com.nullcognition.spotifystreamer;// Created by ersin on 11/07/15

import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class DialogFrag extends DialogFragment implements View.OnClickListener{

	private Track track;
	ImageButton playPause;
	private Tracks tracks;
	static MediaPlayer mediaPlayer = MediaPlayerControlsFragment.mediaPlayer;
	public static int position;

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		tracks = EventBus.getDefault().getStickyEvent(Tracks.class);
		track = tracks.tracks.get(position);
		setRetainInstance(true);
	}

	@Nullable
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.dialog_fragment, container, false);

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

		//////////////////////////// media player

		playPause = (ImageButton) rootView.findViewById(R.id.btn_media_play_pause);
		playPause.setOnClickListener(this);
		rootView.findViewById(R.id.btn_media_next).setOnClickListener(this);
		rootView.findViewById(R.id.btn_media_previous).setOnClickListener(this);


		return rootView;
	}
	@Override
	public void onDestroy(){
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	private static int lastPos;

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		if(mediaPlayer == null || !mediaPlayer.isPlaying()){
			new SwitchSong().execute(tracks.tracks.get(position).preview_url);
			mediaPlayerPlay();
		}
		else if(lastPos != position){
			new SwitchSong().execute(tracks.tracks.get(position).preview_url);
			mediaPlayerPlay();
		}

	}
	@Override
	public void onClick(final View v){
		int viewId = v.getId();
		int action = 0;
		switch(viewId){
			case R.id.btn_media_play_pause:
				mediaPlayerPlay();
				break;
			case R.id.btn_media_next:
				if(position < tracks.tracks.size() - 1){
					new SwitchSong().execute(tracks.tracks.get(++position).preview_url);
					track = tracks.tracks.get(position);
					resetView();
				}
				break;
			case R.id.btn_media_previous:
				if(position > 0){
					new SwitchSong().execute(tracks.tracks.get(--position).preview_url);
					track = tracks.tracks.get(position);
					resetView();

				}
				break;
		}
		lastPos = position;
	}
	private void resetView(){
		View rootView = getView();
		if(rootView != null){
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

			//////////////////////////// media player

			playPause = (ImageButton) rootView.findViewById(R.id.btn_media_play_pause);
			playPause.setOnClickListener(this);
			rootView.findViewById(R.id.btn_media_next).setOnClickListener(this);
			rootView.findViewById(R.id.btn_media_previous).setOnClickListener(this);

			rootView.invalidate();
		}

	}

	private static boolean isLoading = true;

	public void mediaPlayerPlay(){
		if(isLoading){
			Toast.makeText(getActivity(), "Song is loading", Toast.LENGTH_SHORT).show();
		}
		else{
			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
				playPause.setImageResource(android.R.drawable.ic_media_pause);
			}
			else{
				mediaPlayer.pause();
				playPause.setImageResource(android.R.drawable.ic_media_play);
			}
		}
	}

	public interface OnMediaControl{
		int PLAY = 0;
		int PREV = 1;
		int NEXT = 2;

		void action(int mediaControlAction);
	}

	static class SwitchSong extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(final String... params){
			isLoading = true;
			if(mediaPlayer == null){
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			}
			try{
				mediaPlayer.reset();
				mediaPlayer.setDataSource(params[0]);
				mediaPlayer.prepare();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			mediaPlayer.start();
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean aBoolean){
			super.onPostExecute(aBoolean);
			isLoading = false;

		}
	}
}
