package com.nullcognition.spotifystreamer.refactoredArch;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nullcognition.spotifystreamer.refactoredArch.dummy.DummyContent;

public class ItemDetailFragment extends ListFragment{

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private Callbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private boolean isTablet = false;

	public static ItemDetailFragment newInstance(final boolean isTablet){

		Bundle args = new Bundle();
		args.putBoolean(ContentViewSetter.Resizeable.IS_TABLET, isTablet);
		ItemDetailFragment fragment = new ItemDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	public interface Callbacks{
		public void onItemSelectedDetail(String id);
	}

	private static Callbacks sDummyCallbacks = new Callbacks(){
		@Override
		public void onItemSelectedDetail(String id){
		}
	};

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){

		View rootView = inflater.inflate(android.R.layout.list_content, container, false);
		if(isTablet){
			if(rootView != null){
				rootView.setLayoutParams(new LinearLayout.LayoutParams(
						0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
			}
		}
		return rootView;
	}
	//	public static final String ARG_ITEM_ID = "item_id";
//	private DummyContent.DummyItem mItem;
	public ItemDetailFragment(){
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		isTablet = getArguments().getBoolean(ContentViewSetter.Resizeable.IS_TABLET);

		setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(
				getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1,
				DummyContent.ITEMS));
//		if(getArguments().containsKey(ARG_ITEM_ID)){
//			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
//		}
	}
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);

		view.setBackgroundColor(Color.CYAN);

		if(savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)){
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);

		if(!(activity instanceof Callbacks)){
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach(){
		super.onDetach();

		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id){
		super.onListItemClick(listView, view, position, id);

		mCallbacks.onItemSelectedDetail(DummyContent.ITEMS.get(position).id);
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		if(mActivatedPosition != ListView.INVALID_POSITION){
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick){
		getListView().setChoiceMode(activateOnItemClick
				? ListView.CHOICE_MODE_SINGLE
				: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position){
		if(position == ListView.INVALID_POSITION){
			getListView().setItemChecked(mActivatedPosition, false);
		}
		else{
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//	                         Bundle savedInstanceState){
//		View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
//
//		if(mItem != null){
//			((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.content);
//		}
//
//		return rootView;
//	}
}
