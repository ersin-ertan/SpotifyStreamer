package com.nullcognition.spotifystreamer;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class MainActivityFragment extends Fragment{

	private static final String LAST_SEARCH = "lastSearch";
	private String lastSearch;
	boolean isLastSearchSaveable = false;

	private static final String SELECTED_KEY = "selected_position";
	private int selectedPosition = ListView.INVALID_POSITION;

	private ListView listView;
	private View inflated;

	public MainActivityFragment(){ }

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().registerSticky(this);
		setRetainInstance(true);
	}

	interface MainFragToActivity{
		void listItemClicked(String id);
	}

	MainFragToActivity mainFragToActivity;


	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mainFragToActivity = (MainFragToActivity) activity;
		}
		catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement MainFragToActivity");
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		listView = (ListView) rootView.findViewById(R.id.listView);
		TextView header = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.header_ad, container);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), getActivity().getString(R.string.font_type));
		header.setTypeface(font);

		listView.addHeaderView(header);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id){

				selectedPosition = position;

				Artist item = (Artist) parent.getItemAtPosition(position);
				mainFragToActivity.listItemClicked(item.id);
				// activity will start the next activity
			}
		});
		// using AdView as header causes app to slow for list interactions

		ViewStub stub = (ViewStub) rootView.findViewById(R.id.empty);
		inflated = stub.inflate();

		initSearchTextWatcher(rootView);

		if(savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)){
			selectedPosition = savedInstanceState.getInt(SELECTED_KEY);
		}
		return rootView;
	}

	@Override
	public void onResume(){
		super.onResume();
		if(selectedPosition != ListView.INVALID_POSITION){
			listView.smoothScrollToPosition(selectedPosition);
		}
	}
	private void initSearchTextWatcher(final View rootView){
		((EditText) rootView.findViewById(R.id.editText)).addTextChangedListener(new TextWatcher(){
			Handler handler = new Handler();
			Runnable latestPost;
			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after){}
			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count){ }
			@Override
			public void afterTextChanged(final Editable s){

				if(s != null && !s.toString().equals("") && s.length() > 0){
					if(listView.getAlpha() > 0.2f){
						listView.animate().alpha(0.2f).setDuration(500);
					}
					if(latestPost != null){ handler.removeCallbacks(latestPost); }
					latestPost = new Runnable(){
						@Override
						public void run(){
							// s value could have changed to invalid input, check again)
							if(!s.toString().equals("") && s.length() > 0){
								if(lastSearch != null && lastSearch.equals(s.toString())){
									ArtistsPager ap = EventBus.getDefault().getStickyEvent(ArtistsPager.class);
									if(ap != null){
										onEventMainThread(ap);
									}
								}
								else{
									listView.animate().alpha(0.0f).setDuration(50);
									lastSearch = s.toString();
									IntentServiceArtistSearch.searchByArtistName(getActivity(), s.toString());
								}
							}
						}
					};
					handler.postDelayed(latestPost, 750); // in milliseconds, leaves enough time for to write
					// but still searches fast enough to stay responsive, conserve internet usage as well
					return;
				}
				if(listView.getAlpha() != 1){
					if(inflated.getVisibility() != View.INVISIBLE && isLastSearchSaveable){
						inflated.setVisibility(View.INVISIBLE);
					}
					listView.animate().alpha(1.0f).setDuration(1000);
				}
			}
		});
	}

	// saves the last entry in the view by default for orientation changes, below accounts for when
	// the last search entry has been deleted from the search bar, which out the EditText check
	// another search would have taken place preceding the default one from the config change
	@Override
	public void onSaveInstanceState(final Bundle outState){
		if(isLastSearchSaveable &&
				((EditText) getActivity().findViewById(R.id.editText)).getText().length() == 0){
			outState.putString(LAST_SEARCH, lastSearch);
		}

		// When tablets rotate, the currently selected list item needs to be saved.
		// When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
		// so check for that before storing.
		if(selectedPosition != ListView.INVALID_POSITION){
			outState.putInt(SELECTED_KEY, selectedPosition);
		}
		super.onSaveInstanceState(outState);
	}
//
//	@Override
//	public void onViewStateRestored(final Bundle savedInstanceState){
//		super.onViewStateRestored(savedInstanceState);
//		if(savedInstanceState != null){
//			lastSearch = savedInstanceState.getString(LAST_SEARCH);
//			IntentServiceArtistSearch.searchByArtistName(getActivity(), lastSearch);
//		}
//	}

	public void onEventMainThread(ArtistsPager artistsPager){
		if(artistsPager != null){
			if(artistsPager.artists.items.isEmpty()){
				if(inflated.getVisibility() != View.VISIBLE){
					inflated.setVisibility(View.VISIBLE);
				}

			}
			else{
				if(inflated.getVisibility() != View.INVISIBLE){
					inflated.setVisibility(View.INVISIBLE);
				}
				isLastSearchSaveable = true;
				populateListView(artistsPager);
			}
		}
	}

	private void populateListView(final ArtistsPager artistsPager){
		if(artistsPager != null && listView != null){
			List<Artist> artistList = artistsPager.artists.items;
			ArrayAdapterSearchArtist arrayAdapterSearchArtist = new ArrayAdapterSearchArtist(getActivity(), artistList);
			listView.setAdapter(arrayAdapterSearchArtist);
			if(listView.getAlpha() != 1){ listView.animate().alpha(1.0f).setDuration(1000); }
		}
	}

	@Override
	public void onDestroy(){
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
