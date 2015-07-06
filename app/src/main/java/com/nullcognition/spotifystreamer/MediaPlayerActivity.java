package com.nullcognition.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

public class MediaPlayerActivity extends FragmentActivity implements MediaPlayerControlsFragment.OnMediaControl{
	/**
	 */
	private static final int NUM_PAGES = 10;

	private ViewPager pager;
	private PagerAdapter pagerAdapter;
	private MediaPlayerControlsFragment mpcf;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_player);

		pager = (ViewPager) findViewById(R.id.pager);
		pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		pager.setPageTransformer(true, new ZoomOutPageTransformer());

		mpcf = (MediaPlayerControlsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_media_controls);


	}

	@Override
	public void onBackPressed(){
		super.onBackPressed();
//		if(pager.getCurrentItem() == 0){
//		}
//		else{
//			pager.setCurrentItem(pager.getCurrentItem() - 1);
//		}
	}
	@Override
	public void action(final int mediaControlAction){
		switch(mediaControlAction){
			case MediaPlayerControlsFragment.OnMediaControl.PLAY:
				// service.play
				// hide playbutton
				break;
			case MediaPlayerControlsFragment.OnMediaControl.NEXT:
				if(pager.getCurrentItem() == NUM_PAGES - 1){
				}
				else{
					// service.next
					pager.setCurrentItem(pager.getCurrentItem() + 1);
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

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{
		public ScreenSlidePagerAdapter(FragmentManager fm){
			super(fm);
		}

		@Override
		public Fragment getItem(int position){
			return new MediaPlayerActivityFragment();
		}

		@Override
		public int getCount(){ return NUM_PAGES;}
	}

	public static void startActivity(Context context, Bundle bundle){
		Intent intent = new Intent(context, MediaPlayerActivity.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
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
