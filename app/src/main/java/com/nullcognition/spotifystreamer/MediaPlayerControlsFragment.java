package com.nullcognition.spotifystreamer;// Created by ersin on 06/07/15

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 A placeholder fragment containing a simple view.
 */
public class MediaPlayerControlsFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

	public MediaPlayerControlsFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_media_player_controls, container, false);

		rootView.findViewById(R.id.btn_media_play_pause).setOnClickListener(this);
		rootView.findViewById(R.id.btn_media_next).setOnClickListener(this);
		rootView.findViewById(R.id.btn_media_previous).setOnClickListener(this);


		return rootView;
	}

	OnMediaControl onMediaControl;

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			onMediaControl = (OnMediaControl) activity;
		}
		catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement OnMediaControl");
		}
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
				action = OnMediaControl.NEXT;
				break;
			case R.id.btn_media_previous:
				action = OnMediaControl.PREV;
				break;
		}
		onMediaControl.action(action);
	}

	public interface OnMediaControl{
		int PLAY = 0;
		int PREV = 1;
		int NEXT = 2;

		void action(int mediaControlAction);

	}
}
