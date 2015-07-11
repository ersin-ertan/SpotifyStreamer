package com.nullcognition.spotifystreamer;// Created by ersin on 06/07/15

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 A placeholder fragment containing a simple view.
 */
public class MediaPlayerControlsFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

	OnMediaControl onMediaControl;
	private static MediaPlayer mediaPlayer;
	private Tracks tracks;
	private static boolean isLoading = true;

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		EventBus.getDefault().registerSticky(this);
	}
	public MediaPlayerControlsFragment(){
	}
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			onMediaControl = (OnMediaControl) activity;
		}
		catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement OnMediaControl");
		}
		tracks = EventBus.getDefault().getStickyEvent(Tracks.class);
	}

	@Override
	public void onDestroy(){
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	public void onEvent(Tracks tracks){ this.tracks = tracks;}

	ImageButton playPause;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_media_player_controls, container, false);

		playPause = (ImageButton) rootView.findViewById(R.id.btn_media_play_pause);
		playPause.setOnClickListener(this);
		rootView.findViewById(R.id.btn_media_next).setOnClickListener(this);
		rootView.findViewById(R.id.btn_media_previous).setOnClickListener(this);

		return rootView;
	}

	public void switchTrack(int position){
		new SwitchSong().execute(tracks.tracks.get(position).preview_url);
	}

	@Override
	public void onClick(final View v){
		int viewId = v.getId();
		int action = 0;
		switch(viewId){
			case R.id.btn_media_play_pause:
				action = OnMediaControl.PLAY;
				break;
			case R.id.btn_media_next:
				action = OnMediaControl.NEXT; // if inter fragment control is needed
				break;
			case R.id.btn_media_previous:
				action = OnMediaControl.PREV;
				break;
		}
		onMediaControl.action(action);
	}
	public void mediaPlayerPlay(){
		if(isLoading){
			Toast.makeText(getActivity(), "Song is loading", Toast.LENGTH_SHORT).show();
		}
		else{
			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
				playPause.setImageResource(android.R.drawable.ic_media_play);
			}
			else{
				mediaPlayer.pause();
				playPause.setImageResource(android.R.drawable.ic_media_pause);
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
//			mediaPlayer.setDataSource(getActivity(), Uri.parse(tracks.tracks.get(position).preview_url));
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
