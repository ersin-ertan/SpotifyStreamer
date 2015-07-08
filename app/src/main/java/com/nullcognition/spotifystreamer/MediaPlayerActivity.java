package com.nullcognition.spotifystreamer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Tracks;

public class MediaPlayerActivity extends FragmentActivity
		implements MediaPlayerControlsFragment.OnMediaControl{

	public static final String NUM_PAGES_KEY = "numPages";
	public static final String CLICKED_PAGE = "clickedPage";
	private static int NUM_PAGES;
	Tracks tracks;
	private ViewPager pager;
	private PagerAdapter pagerAdapter;
	private MediaPlayerControlsFragment mpcf;
	private int currentTrack = 0;

	public static void startActivity(Context context, Bundle bundle){
		Intent intent = new Intent(context, MediaPlayerActivity.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().registerSticky(this);
		setContentView(R.layout.activity_media_player);

		Intent intent = getIntent();
		NUM_PAGES = intent.getIntExtra(NUM_PAGES_KEY, 0);
		int clickedPage = intent.getIntExtra(CLICKED_PAGE, 0);

		pager = (ViewPager) findViewById(R.id.pager);
		pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		pager.setPageTransformer(true, new ZoomOutPageTransformer());
		pager.setCurrentItem(clickedPage);
		playClickedTrack(clickedPage);

//		mpcf = (MediaPlayerControlsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_media_controls);
	}
	private void playClickedTrack(final int clickedPage){

		currentTrack = clickedPage;
		if(mediaPlayer == null){
			mediaPlayer = MediaPlayer.create(this, Uri.parse(tracks.tracks.get(currentTrack).preview_url));

		}
		else{
		}
//		doBindService();
//		Intent intent = new Intent(this, SpotifyMusicService.class);
//		startService(intent);

	}

	MediaPlayer mediaPlayer;

	@Override
	public void action(final int mediaControlAction){
		switch(mediaControlAction){
			case MediaPlayerControlsFragment.OnMediaControl.PLAY:
				if(!mediaPlayer.isPlaying()){ mediaPlayer.start(); }
				else{mediaPlayer.pause();}
				// service.play
				// hide playbutton

				break;
			case MediaPlayerControlsFragment.OnMediaControl.NEXT:
				if(pager.getCurrentItem() == NUM_PAGES - 1){
				}
				else{
					// service.next
					pager.setCurrentItem(pager.getCurrentItem() + 1);
					mediaPlayer.stop();
					mediaPlayer = null;
					mediaPlayer = MediaPlayer.create(this, Uri.parse(tracks.tracks.get(pager.getCurrentItem()).preview_url));
					mediaPlayer.start();
				}
				break;
			case MediaPlayerControlsFragment.OnMediaControl.PREV:
				if(pager.getCurrentItem() == 0){

				}
				else{
					// service.prev
					pager.setCurrentItem(pager.getCurrentItem() - 1);
				}
				break;

		}
	}

	public void onEventMainThread(Tracks top10Tracks){
		if(top10Tracks != null){
			if(top10Tracks.tracks.isEmpty()){ }
			else{ tracks = top10Tracks;}
		}
	}

	// -------------------------------------------------- Service Methods Start
	private SpotifyMusicService boundService;
	private boolean isBound = false;

	private ServiceConnection mConnection = new ServiceConnection(){
		public void onServiceConnected(ComponentName className, IBinder service){
			boundService = ((SpotifyMusicService.LocalBinder) service).getService();

			Toast.makeText(MediaPlayerActivity.this, "Connected", Toast.LENGTH_SHORT).show();
		}

		public void onServiceDisconnected(ComponentName className){
			boundService = null;
			Toast.makeText(MediaPlayerActivity.this, "DisConnected", Toast.LENGTH_SHORT).show();
		}
	};

	void doBindService(){
		bindService(new Intent(MediaPlayerActivity.this, SpotifyMusicService.class), mConnection, Context.BIND_AUTO_CREATE);
		isBound = true;
	}

	void doUnbindService(){
		if(isBound){
			unbindService(mConnection);
			isBound = false;
		}
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		doUnbindService();
	}
// -------------------------------------------------- Service Methods End


	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{
		public ScreenSlidePagerAdapter(FragmentManager fm){ super(fm);}

		@Override
		public Fragment getItem(int position){
			return MediaPlayerActivityFragment.newInstance(tracks.tracks.get(position));
		}

		@Override
		public int getCount(){ return NUM_PAGES;}
	}
}

class ZoomOutPageTransformer implements ViewPager.PageTransformer{
	private static final float MIN_SCALE = 0.85f;
	private static final float MIN_ALPHA = 0.5f;

	public void transformPage(View view, float position){
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();

		if(position < -1){ // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);

		}
		else if(position <= 1){ // [-1,1]
			// Modify the default slide transition to shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			if(position < 0){
				view.setTranslationX(horzMargin - vertMargin / 2);
			}
			else{
				view.setTranslationX(-horzMargin + vertMargin / 2);
			}

			// Scale the page down (between MIN_SCALE and 1)
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);

			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA +
					(scaleFactor - MIN_SCALE) /
							(1 - MIN_SCALE) * (1 - MIN_ALPHA));

		}
		else{ // (1,+Infinity]
			// This page is way off-screen to the right.
			view.setAlpha(0);
		}
	}
}
