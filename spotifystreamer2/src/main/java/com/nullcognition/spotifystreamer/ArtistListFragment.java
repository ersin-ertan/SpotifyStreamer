package com.nullcognition.spotifystreamer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.nullcognition.spotifystreamer.dummy.DummyContent;

import java.util.Arrays;

public class ArtistListFragment extends ListFragment{

	private Callbacks listener;

	public interface Callbacks{
		public void onItemSelected(String id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		initDataset();
	}

	protected String[] dataset;
	private static final int DATASET_COUNT = 60;
	private void initDataset(){
		dataset = new String[DATASET_COUNT];
		for(int i = 0; i < DATASET_COUNT; i++){
			dataset[i] = "This is element #" + i;
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		setListAdapter(new ListAdapter(getActivity(), Arrays.asList(dataset)));

	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);

		if(!(activity instanceof Callbacks)){
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		listener = (Callbacks) activity;
	}

	@Override
	public void onDetach(){
		listener = null;
		super.onDetach();
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id){
		super.onListItemClick(listView, view, position, id);

		listener.onItemSelected(DummyContent.ITEMS.get(position).id);
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
	}

}
