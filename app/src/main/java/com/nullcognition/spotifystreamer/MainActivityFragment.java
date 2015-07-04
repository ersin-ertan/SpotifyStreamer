package com.nullcognition.spotifystreamer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ListView;

import de.greenrobot.event.EventBus;

public class MainActivityFragment extends Fragment{

	public MainActivityFragment(){ }

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	ListView listView;
	View inflated;

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		listView = (ListView) rootView.findViewById(R.id.listView);
		View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_ad, container);
		listView.addHeaderView(header);
		// using AdView as header causes app to slow for list interactions

		ViewStub stub = (ViewStub) rootView.findViewById(R.id.empty);
		inflated = stub.inflate();

		((EditText) rootView.findViewById(R.id.editText)).addTextChangedListener(new TextWatcher(){
			Handler handler = new Handler();
			Runnable latestPost;
			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after){ }
			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count){}
			@Override
			public void afterTextChanged(final Editable s){
				if(s != null && !s.toString().equals("") && s.length() > 0){
					if(latestPost != null){ handler.removeCallbacks(latestPost); }
					latestPost = new Runnable(){
						@Override
						public void run(){
							if(!s.toString().equals("") && s.length() > 0){ // need to check for s content data change
								IntentServiceArtistSearch.searchByArtistName(getActivity(), s.toString());
								if(inflated.getVisibility() == View.VISIBLE){
									inflated.setVisibility(View.INVISIBLE); // add an ad
								}
							}
						}
					};
					handler.postDelayed(latestPost, 750); // in milliseconds, leaves enough time for to write
					// but still searches fast enough to stay responsive, conserve usage as well
				}
				else if(s != null && s.length() == 0 && latestPost == null){
					inflated.setVisibility(View.VISIBLE);
				}
			}
		});
		return rootView;
	}

	public void onEventMainThread(ArtistListItemData artistListItemData){
		if(artistListItemData != null){
			ArtistArrayAdapter arrayAdapter = new ArtistArrayAdapter(getActivity(), artistListItemData.getArrayOfArtistListItems());
			listView.setAdapter(arrayAdapter);
			listView.invalidate();
		}
	}
}

//	private void testAdapter(){
//		HashMap<String, ArrayList<Image>> hm = new HashMap<>();
//		ArrayList<Image> ar = new ArrayList<Image>();
//		Image i = new Image();
//		i.height = 64;
//		i.width = 64;
//		i.url = "http://people.mozilla.org/~faaborg/files/shiretoko/firefoxIcon/firefox-64-noshadow.png";
//		ar.add(i);
//		ar.add(i);
//		ar.add(i);
//		ar.add(i); // need 4
//		hm.put("test", ar);
//		ArtistListItemData a = new ArtistListItemData(hm);
//		populateAdapter(a);
//	}
