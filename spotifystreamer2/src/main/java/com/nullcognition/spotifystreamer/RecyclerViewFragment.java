package com.nullcognition.spotifystreamer;// Created by ersin on 13/07/15

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;

import java.util.Arrays;

public class RecyclerViewFragment extends Fragment
		implements SampleAdapter.RecyclerItemViewClick{

	public RecyclerViewFragment(){}

	private static final String TAG = "RecyclerViewFragment";

	@Arg(required = false)
	public int DATASET_COUNT = 60;
	@Arg(required = true)
	public boolean isDynamic = false;

	protected RecyclerView recyclerView;
	protected SampleAdapter adapter;
	protected String[] dataset;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(isDynamic){
			FragmentArgs.inject(this);
		}
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

	@Override
	public void onAttach(final Activity activity){
		super.onAttach(activity);
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
	public void positionClicked(final int position){
		FragmentDialog fragment = new FragmentDialogBuilder(position).build();
		fragment.show(getActivity().getFragmentManager(), "t");
	}

}
