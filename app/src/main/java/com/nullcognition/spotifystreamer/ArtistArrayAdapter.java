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

		View rowView = convertView;
		Context context = getContext();

		if(rowView == null){
			View rootView = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false);
			ViewHolder viewHolder = new ViewHolder();

			viewHolder.textView = (TextView) rootView.findViewById(R.id.textView);

			ImageView[] images = new ImageView[4];
			images[0] = (ImageView) rootView.findViewById(R.id.imageView1);
			images[1] = (ImageView) rootView.findViewById(R.id.imageView2);
			images[2] = (ImageView) rootView.findViewById(R.id.imageView3);
			images[3] = (ImageView) rootView.findViewById(R.id.imageView4);
			viewHolder.imageViews = images;
			if(rowView != null){
				rowView.setTag(viewHolder);
			}
		}

		ViewHolder viewHolder = (ViewHolder) rowView.getTag();
		ArtistListItem artistListItem = getItem(position);

		viewHolder.textView.setText(artistListItem.getArtistName());

		ArrayList<Image> top4Images = artistListItem.getTop4Images();
		Picasso.with(context).load(top4Images.get(0).url).into(viewHolder.imageViews[0]);
		// todo make the first image bigger than the rest
		for(int i = 1; i < 4; i++){
			Picasso.with(context).load(top4Images.get(i).url).into(viewHolder.imageViews[i]);
		}
		return rowView;
	}

	static class ViewHolder{
		public ImageView[] imageViews;
		public TextView textView;
	}
}
