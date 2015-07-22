package com.nullcognition.spotifystreamer;// Created by ersin on 14/07/15

import android.app.DialogFragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import kaaes.spotify.webapi.android.models.Track;

public class FragmentDialog extends DialogFragment implements View.OnClickListener{
	@Arg
	public int num; // must be public

	private Track track;
	ImageButton playPause;
	private List<Track> tracks;
	public static MediaPlayer mediaPlayer = FragmentArtistDetail.mediaPlayer;
	public static int position;
	public static String lastAlbumId;

	public FragmentDialog(){}

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		FragmentArgs.inject(this);

		tracks = Paper.get(PaperProducts.TRACK_LIST);
		track = tracks.get(num);
		String albumId = track.album.id;

		if(!mediaPlayer.isPlaying() || lastPos != position || !albumId.equals(lastAlbumId)){
			// in case of new album, but same track position, or if the song finished
			new SwitchSong().execute(tracks.get(position).preview_url);
			lastPos = position;
			lastAlbumId = albumId;
		}

		setRetainInstance(true);
	}

	@Override
	public void onDestroyView(){
		if(getDialog() != null && getRetainInstance()){ getDialog().setDismissMessage(null); }
		super.onDestroyView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_dialog, container, false);

		TextView trackName = (TextView) rootView.findViewById(R.id.txt_track_name);
		TextView trackAlbum = (TextView) rootView.findViewById(R.id.txt_tracks_album);
		ImageView imageAlbum = (ImageView) rootView.findViewById(R.id.image_album);
		TextView artistName = (TextView) rootView.findViewById(R.id.txt_artist_name);
		TextView trackDuration = (TextView) rootView.findViewById(R.id.txt_track_duration);

		trackName.setText(track.name);
		trackAlbum.setText(track.album.name);

		int imageListSize = track.album.images.size();
		String imageUrl = null;
		if(imageListSize != 0){
			imageUrl = track.album.images.get(0).url; // need the biggest picture, as it is the main screen
		}
		Glide.with(getActivity()).load(imageUrl).error(R.drawable.abc_btn_rating_star_off_mtrl_alpha)
		     .fitCenter().into(imageAlbum);


		String artiName = track.artists.get(0).name;
		for(int i = 1; i < track.artists.size(); ++i){
			artiName += ", " + track.artists.get(i).name;
		}
		artistName.setText(artiName);

		long longTrackDur = track.duration_ms;
		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(longTrackDur),
				TimeUnit.MILLISECONDS.toMinutes(longTrackDur) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(longTrackDur) % TimeUnit.MINUTES.toSeconds(1));
		trackDuration.setText(hms);

		//////////////////////////// media player

		playPause = (ImageButton) rootView.findViewById(R.id.btn_media_play_pause);
		playPause.setOnClickListener(this);
		rootView.findViewById(R.id.btn_media_next).setOnClickListener(this);
		rootView.findViewById(R.id.btn_media_previous).setOnClickListener(this);

		return rootView;
	}

	public static int lastPos;

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		if(mediaPlayer == null || !mediaPlayer.isPlaying()){
			new SwitchSong().execute(tracks.get(position).preview_url);
			mediaPlayerPlay();
		}
		else if(lastPos != position){
			new SwitchSong().execute(tracks.get(position).preview_url);
			mediaPlayerPlay();
		}

	}
	@Override
	public void onClick(final View v){
		int viewId = v.getId();
		int action = 0;
		switch(viewId){
			case R.id.btn_media_play_pause:
				mediaPlayerPlay();
				break;
			case R.id.btn_media_next:
				if(position < tracks.size() - 1){
					new SwitchSong().execute(tracks.get(++position).preview_url);
					track = tracks.get(position);
					resetView();
				}
				break;
			case R.id.btn_media_previous:
				if(position > 0){
					new SwitchSong().execute(tracks.get(--position).preview_url);
					track = tracks.get(position);
					resetView();

				}
				break;
		}
		lastPos = position;
	}
	private void resetView(){
		View rootView = getView();
		if(rootView != null){
			TextView trackName = (TextView) rootView.findViewById(R.id.txt_track_name);
			TextView trackAlbum = (TextView) rootView.findViewById(R.id.txt_tracks_album);
			ImageView imageAlbum = (ImageView) rootView.findViewById(R.id.image_album);
			TextView artistName = (TextView) rootView.findViewById(R.id.txt_artist_name);
			TextView trackDuration = (TextView) rootView.findViewById(R.id.txt_track_duration);

			trackName.setText(track.name);
			trackAlbum.setText(track.album.name);

			int imageListSize = track.album.images.size();
			String imageUrl = null;
			if(imageListSize != 0){
				imageUrl = track.album.images.get(0).url; // need the biggest picture, as it is the main screen
			}
			Glide.with(getActivity()).load(imageUrl).placeholder(R.drawable.abc_btn_rating_star_off_mtrl_alpha)
			     .fitCenter().into(imageAlbum);


			String artiName = track.artists.get(0).name;
			for(int i = 1; i < track.artists.size(); ++i){
				artiName += ", " + track.artists.get(i).name;
			}
			artistName.setText(artiName);


			long longTrackDur = track.duration_ms;
			String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(longTrackDur),
					TimeUnit.MILLISECONDS.toMinutes(longTrackDur) % TimeUnit.HOURS.toMinutes(1),
					TimeUnit.MILLISECONDS.toSeconds(longTrackDur) % TimeUnit.MINUTES.toSeconds(1));
			trackDuration.setText(hms);

			//////////////////////////// media player

			playPause = (ImageButton) rootView.findViewById(R.id.btn_media_play_pause);
			playPause.setOnClickListener(this);
			rootView.findViewById(R.id.btn_media_next).setOnClickListener(this);
			rootView.findViewById(R.id.btn_media_previous).setOnClickListener(this);

			rootView.invalidate();
		}

	}

	private static boolean isLoading = true;

	public void mediaPlayerPlay(){
		if(isLoading){
			Toast.makeText(getActivity(), "Song is loading", Toast.LENGTH_SHORT).show();
		}
		else{
			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
				playPause.setImageResource(android.R.drawable.ic_media_pause);
			}
			else{
				mediaPlayer.pause();
				playPause.setImageResource(android.R.drawable.ic_media_play);
			}
		}
	}

	public interface OnMediaControl{
		int PLAY = 0;
		int PREV = 1;
		int NEXT = 2;

		void action(int mediaControlAction);
	}

	static class SwitchSong extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(final String... params){
			isLoading = true;
			if(mediaPlayer == null){
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			}
			try{
				mediaPlayer.reset();
				mediaPlayer.setDataSource(params[0]);
				mediaPlayer.prepare();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			mediaPlayer.start();
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean aBoolean){
			super.onPostExecute(aBoolean);
			isLoading = false;

		}
	}
}

