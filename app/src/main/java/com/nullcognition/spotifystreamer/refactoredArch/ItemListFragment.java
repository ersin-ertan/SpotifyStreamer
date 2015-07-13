package com.nullcognition.spotifystreamer.refactoredArch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nullcognition.spotifystreamer.ArrayAdapterSearchArtist;
import com.nullcognition.spotifystreamer.R;
import com.nullcognition.spotifystreamer.refactoredArch.dummy.DummyContent;

import java.util.List;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class ItemListFragment extends ListFragment implements ContentViewSetter.Resizeable{

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private Callbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private String searchArtistName;

	private List<Artist> artistList;
	private boolean isTablet = false;

	@Override
	public void resizeView(final boolean isTablet){
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){
		View rootView = inflater.inflate(android.R.layout.list_content, container, false);
		rootView.setBackgroundResource(R.drawable.round_back_green_dark_stroke);
		if(isTablet){
			rootView.setLayoutParams(new LinearLayout.LayoutParams(
					0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
		}
		return rootView;
	}
	public interface Callbacks{
		public void onItemSelected(String id);
	}

	private static Callbacks sDummyCallbacks = new Callbacks(){
		@Override
		public void onItemSelected(String id){
		}
	};

	public static ItemListFragment newInstance(final boolean isTablet){

		Bundle args = new Bundle();
		args.putBoolean(ContentViewSetter.Resizeable.IS_TABLET, isTablet);
		ItemListFragment fragment = new ItemListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	public ItemListFragment(){
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().registerSticky(this);
		isTablet = getArguments().getBoolean(ContentViewSetter.Resizeable.IS_TABLET);

		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		String lastS = sharedPref.getString("lastSearch", "defaultedValues");

		if(lastS.equals("defaultedValues") || !lastS.equals("b")){
			ContentViewSetter.searchByArtistName(getActivity(), "b");
		}
		else{
			ArtistsPager ap = EventBus.getDefault().getStickyEvent(ArtistsPager.class);
			if(ap != null){
				artistList = ap.artists.items;
				FragmentActivity fa = getActivity();
				if(fa != null){
					setListAdapter(new ArrayAdapterSearchArtist(getActivity(), artistList));
				}
			}
		}

	}

	public void onEventMainThread(ArtistsPager artistsPager){
		if(artistsPager != null){
			if(artistsPager.artists.items.isEmpty()){

			}
			else{
				if(artistList != null){
					boolean areSame = true;
					for(int i = 0; i < artistList.size(); i++){
						if(!artistList.get(i).id.equals(artistsPager.artists.items.get(i).id)){
							areSame = false;
							break;
						}
					}
					if(areSame){return;}
					artistList = artistsPager.artists.items;
					FragmentActivity fa = getActivity();
					if(fa != null){
						setListAdapter(new ArrayAdapterSearchArtist(getActivity(), artistList));
					}
				}
				else if(artistList == null){
					artistList = artistsPager.artists.items;
					FragmentActivity fa = getActivity();
					if(fa != null){
						setListAdapter(new ArrayAdapterSearchArtist(getActivity(), artistList));
					}
				}
			}
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);

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

		mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
	}

	@Override
	public void onPause(){
		super.onPause();
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("lastSearch", "b");
		editor.commit();
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

	@Override
	public void onDestroy(){
		SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
		super.onDestroy();
	}
}
