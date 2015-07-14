package com.nullcognition.spotifystreamer;// Created by ersin on 14/07/15

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.fragmentargs.annotation.Arg;

import java.util.Arrays;

public class FragmentArtistList extends Fragment // is only ever in a static layout
		implements AdapterArtistList.RecyclerItemViewClick{

	public FragmentArtistList(){}

	private static final String TAG = "FragmentArtistDetail";

	@Arg(required = false) // will this allow static layout fragment generation
	public int DATASET_COUNT = 30;

	@Arg(required = true)
	public String title;


	protected RecyclerView recyclerView;
	protected AdapterArtistList adapter;
	protected String[] dataset;

	private OnRecyclerViewItemClick listener;

	public interface OnRecyclerViewItemClick{
		void positionClicked(final int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initDataset();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.recycler_view, container, false);
		rootView.setTag(TAG);

		recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		setRecyclerViewLayoutManager();

		adapter = new AdapterArtistList(getActivity(), Arrays.asList(dataset), this);
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

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);

		if(!(activity instanceof OnRecyclerViewItemClick)){
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		listener = (OnRecyclerViewItemClick) activity;
	}

	@Override
	public void onDetach(){
		listener = null;
		super.onDetach();
	}

	@Override
	public void positionClicked(final int position){
		listener.positionClicked(position);
	}

}
