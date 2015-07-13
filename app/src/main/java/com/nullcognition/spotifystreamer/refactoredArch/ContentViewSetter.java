package com.nullcognition.spotifystreamer.refactoredArch;// Created by ersin on 12/07/15

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nullcognition.spotifystreamer.IntentServiceArtistSearch;
import com.nullcognition.spotifystreamer.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import kaaes.spotify.webapi.android.models.Artist;

public class ContentViewSetter{


	private static int frameLayoutsId;
	private static boolean detailClicked;

	public static List<Artist> artistList;
	private static String lastArtistNameSearch;

	public static void checkBackStack(){
		if(detailClicked){ detailClicked = false; } // popped
	}
	public static void searchByArtistName(final FragmentActivity activity, final String searchArtistName){
			IntentServiceArtistSearch.searchByArtistName(activity, searchArtistName);
	}

	public interface Resizeable{
		String IS_TABLET = "isTablet";
		void resizeView(boolean isTablet);
	}

	static private AppCompatActivity activity;
	static private boolean isTablet;
	static public boolean isTablet(){return isTablet;}

	private static int ld;

	public static void setActivity(AppCompatActivity inActivity){
		activity = inActivity;
		isTablet = activity.getResources().getBoolean(R.bool.isTablet);
		LinearLayout layout = createLinearLayout();
		if(isTablet){
			createMaster(layout);
			createDetail(layout);
		}
		else{
			createMaster(layout);
			if(detailClicked){
				createDetail(null);
			}
		}
		ld = layout.getId();
	}

	private static void createMaster(LinearLayout layout){
		FrameLayout frameLayout = new FrameLayout(activity);
		frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		frameLayoutsId = generateViewId();
		frameLayout.setId(frameLayoutsId);
		layout.addView(frameLayout);

		addFragment(ItemListFragment.newInstance(isTablet), frameLayout.getId());
	}

	public static void createDetail(LinearLayout layout){
		if(layout == null){
			detailClicked = true;
			addFragment((ItemDetailFragment.newInstance(isTablet)), frameLayoutsId);
		}
		else{
			addFragment((ItemDetailFragment.newInstance(isTablet)), layout.getId());
		}
	}

	private static LinearLayout createLinearLayout(){

		LinearLayout layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		layout.setId(generateViewId());

		activity.setContentView(layout);
		return layout;
	}

	private static void addFragment(Fragment fragment, int layoutId){
		if(!isTablet && fragment instanceof ItemDetailFragment){
			activity.getSupportFragmentManager().beginTransaction()
			        .add(layoutId, fragment, fragment.getClass().getSimpleName())
			        .addToBackStack(fragment.getClass().getSimpleName()).commit();

			return;
		}
		activity.getSupportFragmentManager().beginTransaction()
		        .add(layoutId, fragment, fragment.getClass().getSimpleName()).commit();
	}

	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	private static int generateViewId(){
		for(; ; ){
			final int result = sNextGeneratedId.get();
			// aapt-generated IDs have the high byte nonzero; clamp to the range under that.
			int newValue = result + 1;
			if(newValue > 0x00FFFFFF){
				newValue = 1; // Roll over to 1, not 0.
			}
			if(sNextGeneratedId.compareAndSet(result, newValue)){
				return result;
			}
		}
	}

	public static void showDialog(){
		MyDialogFragment.newInstance().show(activity.getSupportFragmentManager(),
				MyDialogFragment.class.getSimpleName());
	}
}
