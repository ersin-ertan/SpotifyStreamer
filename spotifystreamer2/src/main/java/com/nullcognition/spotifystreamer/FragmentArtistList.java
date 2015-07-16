package com.nullcognition.spotifystreamer;// Created by ersin on 14/07/15

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import io.paperdb.Paper;
import kaaes.spotify.webapi.android.models.Artist;

public class FragmentArtistList extends Fragment // is only ever in a static layout, should be dynamic
		implements AdapterArtistList.RecyclerItemViewClick{

	public FragmentArtistList(){}
	private OnRecyclerViewItemClick listener;
	private RecyclerView recyclerView; // dont know why getView was returning the null recyclerView

	public interface OnRecyclerViewItemClick{
		void positionClicked(final int position);
	}

	@Override
	public void positionClicked(final int position){ listener.positionClicked(position);}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		if(!(activity instanceof OnRecyclerViewItemClick)){
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		listener = (OnRecyclerViewItemClick) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.recycler_view, container, false);
		rootView.setTag("fragmentArtistList");

		recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		setRecyclerViewLayoutManager(recyclerView);
		if(Paper.exist(PaperProducts.ARTIST_LIST)){
			updateAdapter();
		} // should this be here or onViewCreated

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

	public void updateAdapter(){
		new PaperGetArtistListAndUpdateAdapter().execute();
	}

	public class PaperGetArtistListAndUpdateAdapter extends AsyncTask<Void, Void, List<Artist>>{
		@Override
		protected List<Artist> doInBackground(final Void... params){
			return Paper.get(PaperProducts.ARTIST_LIST);
		}
		@Override
		protected void onPostExecute(final List<Artist> artistList){
			super.onPostExecute(artistList);
			setFragmentArtistListAdapter(artistList);
		}
	}

	private void setFragmentArtistListAdapter(final List<Artist> artistList){
		if(artistList != null){
			AdapterArtistList adapter = new AdapterArtistList(getActivity(), artistList, this);
			// getView() will produce null if used before onCreateView
			recyclerView.setAdapter(adapter);
			if(adapter.getItemCount() == 0){
				Toast.makeText(getActivity(), "No Results", Toast.LENGTH_SHORT).show();
			}
		}
	}
}


