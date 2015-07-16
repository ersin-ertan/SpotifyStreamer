package com.nullcognition.spotifystreamer;// Created by ersin on 13/07/15

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;

import java.util.List;

import io.paperdb.Paper;
import kaaes.spotify.webapi.android.models.Track;

import static com.nullcognition.spotifystreamer.IntentServiceSpotifyDownloader.ACTION_SEARCH_ARTIST_TOP_TEN_TRACKS;

public class FragmentArtistDetail extends Fragment
		implements AdapterArtistDetail.RecyclerItemViewClick{
@Arg
Boolean isTablet;

	protected RecyclerView recyclerView;

	public FragmentArtistDetail(){}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		FragmentArgs.inject(this);
		IntentServiceSpotifyDownloader
				.startService(ACTION_SEARCH_ARTIST_TOP_TEN_TRACKS, null, getActivity());
		// searchquery will be inferred

	}

	@Override
	public void positionClicked(final int position){
		FragmentDialog fragment = new FragmentDialogBuilder(position).build();
		fragment.show(getActivity().getFragmentManager(), "t");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.recycler_view, container, false);
		rootView.setTag("fragmentArtistDetail");

		recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		setRecyclerViewLayoutManager(recyclerView);

		return rootView;
	}

	public void setRecyclerViewLayoutManager(final RecyclerView recyclerView){
		int scrollPosition = 0;
		if(recyclerView.getLayoutManager() != null){
			scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
					.findFirstCompletelyVisibleItemPosition();
		}
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.scrollToPosition(scrollPosition);
	}

	public void updateAdapter(){new PaperGetTrackListAndUpdateAdapter().execute();}

	public class PaperGetTrackListAndUpdateAdapter extends AsyncTask<Void, Void, List<Track>>{
		@Override
		protected List<Track> doInBackground(final Void... params){
			return Paper.get(PaperProducts.TRACK_LIST);
		}
		@Override
		protected void onPostExecute(final List<Track> tracks){
			super.onPostExecute(tracks);
			setFragmentArtistListAdapter(tracks);
		}
	}

	private void setFragmentArtistListAdapter(final List<Track> tracks){
		if(tracks != null){
			AdapterArtistDetail adapter = new AdapterArtistDetail(getActivity(), tracks, this);
			// getView() will produce null if used before onCreateView
			recyclerView.setAdapter(adapter);
			if(adapter.getItemCount() == 0){
				Toast.makeText(getActivity(), "No Results", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private String lastSearch;
	public void onEventMainThread(IntentServiceSpotifyDownloader.SearchArtistsTopTenTracks searchedQuery){
		if(!searchedQuery.artistName.equals(lastSearch)){ // stops multiple updates by eventbus
			lastSearch = searchedQuery.artistName;
			updateAdapter();
		}
	}

}
