package com.nullcognition.spotifystreamer;// Created by ersin on 13/07/15

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;

import java.util.Arrays;

public class RecyclerViewFragment extends Fragment
		implements SampleAdapter.SL{

	public RecyclerViewFragment(){}

	private static final String TAG = "RecyclerViewFragment";

	@Arg
	public int DATASET_COUNT = 60;

	protected RecyclerView recyclerView;
	protected SampleAdapter adapter;
	protected String[] dataset;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		FragmentArgs.inject(this);

		initDataset();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
		rootView.setTag(TAG);

		recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		setRecyclerViewLayoutManager();

		adapter = new SampleAdapter(getActivity(), Arrays.asList(dataset), this);
		recyclerView.setAdapter(adapter);

		return rootView;
	}

	public void setRecyclerViewLayoutManager(){
		int scrollPosition = 0;

		if(recyclerView.getLayoutManager() != null){
			scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
					.findFirstCompletelyVisibleItemPosition();
		}

		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.scrollToPosition(scrollPosition);
	}

	private void initDataset(){
		dataset = new String[DATASET_COUNT];
		for(int i = 0; i < DATASET_COUNT; i++){
			dataset[i] = "This is element #" + i;
		}
	}


	//	@Override
	public void onImageClick(final ImageView v){
		Toast.makeText(getActivity(), "IMAGE", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void p(final int p){
		Toast.makeText(getActivity(), "at Position" + p, Toast.LENGTH_SHORT).show();
	}
}