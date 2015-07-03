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

import butterknife.Bind;
import butterknife.OnTextChanged;
import de.greenrobot.event.EventBus;

/**
 A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment{

	@Bind(R.id.editText)
	EditText editText;
	@OnTextChanged(R.id.editText)
	void autoSearch(){
		// first test functionality, then implement the wait for 2 seconds after text input done
		// editText.getText(); is the correct argument)
	}


	public MainActivityFragment(){
	}

	@Override
	public void onCreate(final Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	ListView listView = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		listView = (ListView) rootView.findViewById(R.id.listView);

		((EditText) rootView.findViewById(R.id.editText)).addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after){

			}
			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count){

			}
			@Override
			public void afterTextChanged(final Editable s){
				Toast.makeText(getActivity(), "text changed", Toast.LENGTH_SHORT).show();
				IntentServiceArtistSearch.searchByArtistName(getActivity(), "Paul");


			}
		});
		return rootView;
	}

	// Get input from an edit text and put a listener on it for 2 seconds after input is finished
	// which will auto search/call intent service

//	@Bind(R.id.listView)
//	ListView listView;

	public void onEvent(ArtistListItemData artistListItemData){
		if(artistListItemData != null){
			listView.setAdapter(new ArtistArrayAdapter(getActivity(), artistListItemData.getArrayOfArtistListItems()));
		}
	}
}
