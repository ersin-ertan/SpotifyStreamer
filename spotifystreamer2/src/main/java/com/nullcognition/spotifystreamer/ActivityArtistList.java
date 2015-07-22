package com.nullcognition.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

import java.util.List;

import de.greenrobot.event.EventBus;
import io.paperdb.Paper;
import kaaes.spotify.webapi.android.models.Artist;
import se.emilsjolander.intentbuilder.IntentBuilder;

import static com.nullcognition.spotifystreamer.IntentServiceSpotifyDownloader.ACTION_SEARCH_ARTIST_NAME;


@IntentBuilder
public class ActivityArtistList extends FragmentActivity
		implements FragmentArtistList.OnRecyclerViewItemClick{

	private static boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		Paper.init(this);

		setContentView(R.layout.activity_artist_list);
		if(findViewById(R.id.artist_detail_container) != null){mTwoPane = true;}

		initRebound();

	}

	private void initRebound(){

		SpringSystem springSystem = SpringSystem.create();
		final Spring spring = springSystem.createSpring();
		final ImageButton imageButton = (ImageButton) findViewById(R.id.imgbtn_search);
		spring.addListener(new SimpleSpringListener(){
			@Override
			public void onSpringUpdate(Spring spring){
				float value = (float) spring.getCurrentValue();
				float scale = 1f - (value * 0.5f);
				imageButton.setScaleX(scale);
				imageButton.setScaleY(scale);
			}
		});

		imageButton.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(final View v, final MotionEvent event){
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						spring.setEndValue(1);
						String searchQuery = ((EditText) findViewById(R.id.edittxt_search)).getText().toString();
						IntentServiceSpotifyDownloader.startService(ACTION_SEARCH_ARTIST_NAME, searchQuery, getApplicationContext());
						break;
					case MotionEvent.ACTION_UP:
						spring.setEndValue(0);
						break;
				}
				return true;
			}
		});
	}

	@Override
	public void positionClicked(final int position){
		// known to be init
		String artistId = ((List<Artist>) Paper.get(PaperProducts.ARTIST_LIST)).get(position).id;
		Paper.put(PaperProducts.ARTIST_ID, artistId);
		if(mTwoPane){
			Fragment fragment = new FragmentArtistDetailBuilder(true).build(); // should this recyclerview be in an artist detail container to replace its own container on each click
			getSupportFragmentManager().beginTransaction()
			                           .replace(R.id.artist_detail_container, fragment)
			                           .commit();
			// if was .add() then could not garbage collector
			// single screen ActivityArtistDetail only add()'s once, rotation does not add more
			// must be taken care of by the fragment manager
		}
		else{ startActivity(new ActivityArtistDetailIntentBuilder().build(this));}
	}

	private String lastSearch;
	public void onEventMainThread(IntentServiceSpotifyDownloader.SearchByArtistName searchedQuery){

		if(!searchedQuery.artistName.equals(lastSearch)){ // stops multiple updates by eventbus
			lastSearch = searchedQuery.artistName;
			((FragmentArtistList) getSupportFragmentManager().findFragmentById(R.id.fragment_artist_list))
					.updateAdapter();
		}
	}

	@Override
	protected void onDestroy(){
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}
