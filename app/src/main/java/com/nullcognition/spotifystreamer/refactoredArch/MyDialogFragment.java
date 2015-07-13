package com.nullcognition.spotifystreamer.refactoredArch;// Created by ersin on 12/07/15

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nullcognition.spotifystreamer.R;

public class MyDialogFragment extends DialogFragment{

	static MyDialogFragment newInstance(){
		return new MyDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.dialog_fragment, container, false);
		return v;
	}
}
