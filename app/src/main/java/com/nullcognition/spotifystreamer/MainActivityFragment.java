package com.nullcognition.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

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
		IntentServiceArtistSearch.searchByArtistName(getActivity(), "Paul");
		return rootView;
	}

	// Get input from an edit text and put a listener on it for 2 seconds after input is finished
	// which will auto search/call intent service

//	@Bind(R.id.listView)
//	ListView listView;

	public void onEvent(ArtistListItemData artistListItemData){
		if(artistListItemData != null){
			setArrayAdapter(artistListItemData.getArrayOfArtistListItems());
		}
	}
	private void setArrayAdapter(final ArrayList<ArtistListItem> arrayOfArtistListItems){
		ArtistArrayAdapter artistArrayAdapter = new ArtistArrayAdapter(getActivity(), arrayOfArtistListItems);

		listView.setAdapter(artistArrayAdapter);
		artistArrayAdapter.notifyDataSetChanged();

		// todo check why the getView in the artistarrayAdapter is not being called
	}

}
