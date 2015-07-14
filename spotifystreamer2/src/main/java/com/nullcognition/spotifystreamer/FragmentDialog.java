package com.nullcognition.spotifystreamer;// Created by ersin on 14/07/15

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;

public class FragmentDialog extends DialogFragment{
		@Arg
	public int num; // must be public
	public FragmentDialog(){}
//	public static FragmentDialog newInstance(int in){
//
//		Bundle args = new Bundle();
//		args.putInt("t", in);
//		FragmentDialog fragment = new FragmentDialog();
//		fragment.setArguments(args);
//		return fragment;
//	}

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	//	num = getArguments().getInt("t");
		FragmentArgs.inject(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_dialog, container, false);
		((TextView) v.findViewById(R.id.dText)).setText(String.valueOf(num));
		return v;
	}
}

