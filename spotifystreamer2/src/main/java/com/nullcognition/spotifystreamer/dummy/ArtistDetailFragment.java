//package com.nullcognition.spotifystreamer;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.hannesdorfmann.fragmentargs.FragmentArgs;
//import com.hannesdorfmann.fragmentargs.annotation.Arg;
//import com.nullcognition.spotifystreamer.dummy.DummyContent;
//
//public class ArtistDetailFragment extends Fragment{
//
//	public static final String ARG_ITEM_ID = "item_id";
//
//	@Arg
//	public String itemId;
//
//
//	private DummyContent.DummyItem mItem;
//
//	public ArtistDetailFragment(){}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState){
//		super.onCreate(savedInstanceState);
//		FragmentArgs.inject(this);
//		mItem = DummyContent.ITEM_MAP.get(itemId);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//		View rootView = inflater.inflate(R.layout.fragment_artist_detail, container, false);
//
//		if(mItem != null){
//			((TextView) rootView.findViewById(R.id.artist_detail)).setText(mItem.content);
//		}
//
//		return rootView;
//	}
//}
