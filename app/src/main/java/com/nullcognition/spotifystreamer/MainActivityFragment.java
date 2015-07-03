package com.nullcognition.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import kaaes.spotify.webapi.android.models.Image;

/**
 A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment{

	View emptyView = null;

	public MainActivityFragment(){ }

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
//		setRetainInstance(true); // is this needed to save the results on rotation?
		IntentServiceArtistSearch.searchByArtistName(getActivity(), "Paul");



	}
	private void testAdapter(){
		HashMap<String, ArrayList<Image>> hm = new HashMap<>();
		ArrayList<Image> ar = new ArrayList<Image>();
		Image i = new Image();
		i.height = 64;
		i.width = 64;
		i.url = "http://people.mozilla.org/~faaborg/files/shiretoko/firefoxIcon/firefox-64-noshadow.png";
		ar.add(i);
		ar.add(i);
		ar.add(i);
		ar.add(i); // need 4
		hm.put("test", ar);
		ArtistListItemData a = new ArtistListItemData(hm);
		populateAdapter( a);
	}

	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		listView = (ListView) rootView.findViewById(R.id.listView);
		View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_ad, container);
		listView.addHeaderView(header);

		// test to see if changing the adapter auto refreshes the table, yes it does
//		listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, new String[]{".,", ",.p", ",.p"}));
		// testing
//		testAdapter();
// if(emptyView == null){
//			emptyView = ((ViewStub) rootView.findViewById(R.id.empty)).inflate();
//		}

		((EditText) rootView.findViewById(R.id.editText)).addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after){

			}
			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count){

			}
			@Override
			public void afterTextChanged(final Editable s){
//				Toast.makeText(getActivity(), "text changed", Toast.LENGTH_SHORT).show();


			}
		});
		return rootView;
	}

	public void populateAdapter(final ArtistListItemData a){
		listView.setAdapter(new ArtistArrayAdapter(getActivity(), a.getArrayOfArtistListItems()));
		Toast.makeText(getActivity(), "populateAdapter", Toast.LENGTH_SHORT).show();

	}


	// Get input from an edit text and put a listener on it for 2 seconds after input is finished
	// which will auto search/call intent service
	public void onEventMainThread(ArtistListItemData artistListItemData){
		if(artistListItemData != null){
			ArtistArrayAdapter arrayAdapter = new ArtistArrayAdapter(getActivity(), artistListItemData.getArrayOfArtistListItems());
			listView.setAdapter(arrayAdapter);
//			if(emptyView != null){ emptyView.setVisibility(View.INVISIBLE); }
		}
	}
}
