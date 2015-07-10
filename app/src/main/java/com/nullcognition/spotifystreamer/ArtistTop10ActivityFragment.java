package com.nullcognition.spotifystreamer;

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

/**
 A placeholder fragment containing a simple view.
 */
public class ArtistTop10ActivityFragment extends Fragment{

	ListView listView;
	View inflated;
	int numTracks = 0;
	List<Track> trackList;

	public ArtistTop10ActivityFragment(){}

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().registerSticky(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		(rootView.findViewById(R.id.editText)).setVisibility(View.GONE);

		ViewStub stub = (ViewStub) rootView.findViewById(R.id.empty);
		inflated = stub.inflate();
		inflated.setVisibility(View.INVISIBLE);

		listView = (ListView) rootView.findViewById(R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id){
				Bundle bundle = new Bundle();
				bundle.putInt(MediaPlayerActivity.NUM_PAGES_KEY, numTracks);
				bundle.putInt(MediaPlayerActivity.CLICKED_PAGE, position);
				MediaPlayerActivity.startActivity(getActivity(), bundle);
			}
		});

		return rootView;
	}

	private static String lastArtistId;

	public void getTopTracks(String artistId){
		lastArtistId = artistId;
		IntentServiceArtistSearch.searchArtistTop10(getActivity(), artistId);
	}

	public void onEventMainThread(Tracks top10Tracks){
		if(top10Tracks != null){
			if(top10Tracks.tracks.isEmpty()){
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
}
