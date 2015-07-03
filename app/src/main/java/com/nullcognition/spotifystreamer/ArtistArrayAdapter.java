package com.nullcognition.spotifystreamer;// Created by ersin on 03/07/15

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Image;

public class ArtistArrayAdapter extends ArrayAdapter<ArtistListItem>{

	public ArtistArrayAdapter(final Context context, final ArrayList<ArtistListItem> resource){
		super(context, 0, resource);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent){

		ArtistListItem artistListItem = getItem(position);
		Context context = getContext();
		View rootView = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false);

		TextView textView = (TextView) rootView.findViewById(R.id.textView);
		textView.setText(artistListItem.getArtistName());

		ImageView[] images = new ImageView[4];
		images[0] = (ImageView) rootView.findViewById(R.id.imageView1);
		images[1] = (ImageView) rootView.findViewById(R.id.imageView2);
		images[2] = (ImageView) rootView.findViewById(R.id.imageView3);
		images[3] = (ImageView) rootView.findViewById(R.id.imageView4);

		ArrayList<Image> top4Images = artistListItem.getTop4Images();
		Picasso.with(context).load(top4Images.get(0).url).into(images[0]);
		// make the first image bigger than the rest
		for(int i = 1; i < 4; i++){
			Picasso.with(context).load(top4Images.get(i).url).into(images[i]);

		}
		return rootView;
	}
}
