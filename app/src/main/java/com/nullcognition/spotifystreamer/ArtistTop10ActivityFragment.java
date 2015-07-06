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

	public ArtistTop10ActivityFragment(){}

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	public void getTopTracks(String artistId){
		IntentServiceArtistSearch.searchArtistTop10(getActivity(), artistId);
	}

	ListView listView;
	View inflated;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		(rootView.findViewById(R.id.editText)).setVisibility(View.GONE);
		ViewStub stub = (ViewStub) rootView.findViewById(R.id.empty);
		inflated = stub.inflate();
		listView = (ListView) rootView.findViewById(R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id){
				Track item = (Track) parent.getItemAtPosition(position);
				Bundle bundle = new Bundle();
				// bundl.putList// todo ----------------------------------------------------------------------------------------
				MediaPlayerActivity.startActivity(getActivity(), bundle);
			}
		});

		return rootView;
	}

	public void onEventMainThread(Tracks top10Tracks){
		if(top10Tracks != null){
			if(top10Tracks.tracks.isEmpty()){
				if(inflated.getVisibility() != View.VISIBLE){
					inflated.setVisibility(View.VISIBLE);
				}

			}
			else{
				if(inflated.getVisibility() != View.INVISIBLE){
					inflated.setVisibility(View.INVISIBLE);
				}
				populateListView(top10Tracks);
			}
		}
	}
	private void populateListView(final Tracks top10Tracks){
		List<Track> trackList = top10Tracks.tracks;
		ArrayAdapterTopTracks arrayadapterTopTracks = new ArrayAdapterTopTracks(getActivity(), trackList);
		listView.setAdapter(arrayadapterTopTracks);
	}
}
