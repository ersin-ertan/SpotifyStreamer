package com.nullcognition.spotifystreamer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class ArtistTop10ActivityFragment extends Fragment{

	private static final String LAST_SEARCH = "lastSearch";
	private String lastArtistId;
	boolean isLastSearchSaveable = false;


	private ListView listView;
	private View inflated;
	private int numTracks = 0;
	private List<Track> trackList;

	public ArtistTop10ActivityFragment(){}

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().registerSticky(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		(rootView.findViewById(R.id.editText)).setVisibility(View.GONE);

		ViewStub stub = (ViewStub) rootView.findViewById(R.id.empty);
		inflated = stub.inflate();
		inflated.setVisibility(View.INVISIBLE);

		listView = (ListView) rootView.findViewById(R.id.listView);
		listView.setOnItemClickListener(newOnItemClickListener());
		return rootView;
	}

	@Override
	public void onAttach(final Activity activity){
		super.onAttach(activity);
		if(MainActivity.twoPane){
			twoPane = (TwoPane) activity;
		}
	}

	interface TwoPane{
		void itemClicked(final int position);
	}

	TwoPane twoPane;

	private AdapterView.OnItemClickListener newOnItemClickListener(){
		return new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id){
				if(MainActivity.twoPane){
					twoPane.itemClicked(position);
				}
				else{
					Bundle bundle = new Bundle();
					bundle.putInt(MediaPlayerActivity.NUM_PAGES_KEY, numTracks);
					bundle.putInt(MediaPlayerActivity.CLICKED_PAGE, position);
					MediaPlayerActivity.startActivity(getActivity(), bundle);
				}
			}
		};
	}

	@Override
	public void onSaveInstanceState(final Bundle outState){
		if(isLastSearchSaveable){
			outState.putString(LAST_SEARCH, lastArtistId);
		}
		super.onSaveInstanceState(outState);

	}

	@Override
	public void onViewStateRestored(final Bundle savedInstanceState){
		super.onViewStateRestored(savedInstanceState);
		if(savedInstanceState != null){
			lastArtistId = savedInstanceState.getString(LAST_SEARCH);
			IntentServiceArtistSearch.searchArtistTop10(getActivity(), lastArtistId);
		}
	}

	public void onEventMainThread(Tracks top10Tracks){
		if(top10Tracks != null){
			if(top10Tracks.tracks.isEmpty()){
				isLastSearchSaveable = false;
				listView.setAdapter(null);
				// added to clear the listview when you clicked on an artist with no tracks to display
				// the previous artists selected tracks would still show, this is required in this
				// fragment and not in main fragment because the list view is hidden behind inflated
				// no results stub
				if(inflated != null && inflated.getVisibility() != View.VISIBLE){
					inflated.setVisibility(View.VISIBLE);
				}

			}
			else{
				if(inflated != null && inflated.getVisibility() != View.INVISIBLE){
					inflated.setVisibility(View.INVISIBLE);
				}
				numTracks = top10Tracks.tracks.size();
				isLastSearchSaveable = true;
				populateListView(top10Tracks);
			}
		}
	}

	private void populateListView(final Tracks top10Tracks){
		if(top10Tracks != null && listView != null){
			trackList = top10Tracks.tracks;
			ArrayAdapterTopTracks arrayadapterTopTracks = new ArrayAdapterTopTracks(getActivity(), trackList);
			listView.setAdapter(arrayadapterTopTracks);
		}
	}

	// called from activity in single layout as well
	//// called when the list item has been clicked in a two pane layout from another fragment, fragment to activity to fragment call
	public void getTopTracks(String artistId){
		lastArtistId = artistId;
		IntentServiceArtistSearch.searchArtistTop10(getActivity(), artistId);
	}
}
