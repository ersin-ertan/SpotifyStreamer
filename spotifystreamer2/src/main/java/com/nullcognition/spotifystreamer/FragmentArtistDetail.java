package com.nullcognition.spotifystreamer;// Created by ersin on 13/07/15

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
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

import de.greenrobot.event.EventBus;
import io.paperdb.Paper;
import kaaes.spotify.webapi.android.models.Track;

import static com.nullcognition.spotifystreamer.IntentServiceSpotifyDownloader.ACTION_SEARCH_ARTIST_TOP_TEN_TRACKS;

public class FragmentArtistDetail extends Fragment
		implements AdapterArtistDetail.RecyclerItemViewClick{
	@Arg
	Boolean isTablet;

	protected RecyclerView recyclerView;
	public static MediaPlayer mediaPlayer = new MediaPlayer();

	public FragmentArtistDetail(){}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this); // the updated artist id was never broadcast, forgot to register
		FragmentArgs.inject(this);
		String searchQuery = Paper.get(PaperProducts.ARTIST_ID);
		IntentServiceSpotifyDownloader
				.startService(ACTION_SEARCH_ARTIST_TOP_TEN_TRACKS, searchQuery, getActivity());
		if(Paper.exist(PaperProducts.TRACK_LIST)){
			updateAdapter();
		}
	}

	@Override
	public void positionClicked(final int position){
		if(FragmentDialog.position != position){
			FragmentDialog.position = position;
		}
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
public interface SubtitleListener{
	void changeSubtitle();
}
	SubtitleListener listener;

	@Override
	public void onAttach(final Activity activity){
		super.onAttach(activity);
		super.onAttach(activity);
		if(!(activity instanceof SubtitleListener)){
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		listener = (SubtitleListener) activity;
	}

	@Override
	public void onDestroy(){
		EventBus.getDefault().unregister(this);
		super.onDestroy();
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
			listener.changeSubtitle();
		}
	}

	private void setFragmentArtistListAdapter(final List<Track> tracks){
		if(tracks != null){
			Context context = getActivity();
			/*
			* the context check prevents:
			*  java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Object android.content.Context.getSystemService(java.lang.String)' on a null object reference
            at android.view.LayoutInflater.from(LayoutInflater.java:219)
            at com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter.<init>(SupportAnnotatedAdapter.java:23)
            at com.nullcognition.spotifystreamer.AdapterArtistList.<init>(AdapterArtistList.java:49)
			* */
			if(context != null){
				AdapterArtistDetail adapter = new AdapterArtistDetail(context, tracks, this);
				// getView() will produce null if used before onCreateView
				recyclerView.setAdapter(adapter);
				if(adapter.getItemCount() == 0){
					Toast.makeText(getActivity(), "No Results", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private String lastSearchedArtistId = null;
	public void onEventMainThread(IntentServiceSpotifyDownloader.SearchArtistsTopTenTracks searchedQuery){
		String searchedArtistId = searchedQuery.artistid;
		if(!searchedArtistId.equals(lastSearchedArtistId)){ // stops multiple updates by eventbus
			lastSearchedArtistId = searchedArtistId;
			updateAdapter();
		}
	}

}
