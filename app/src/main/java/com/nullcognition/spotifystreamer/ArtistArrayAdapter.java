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

public class ArtistArrayAdapter extends ArrayAdapter<ArtistListItem>{

	static class ViewHolder{
		public ImageView[] imageViews;
		public TextView textView;
	}
	public ArtistArrayAdapter(final Context context, final ArrayList<ArtistListItem> resource){
		super(context, 0, resource);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent){


		View rootView;
		Context context = getContext();

		if(convertView == null){
			rootView = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) rootView.findViewById(R.id.textView);

			ImageView[] images = new ImageView[4];
			images[0] = (ImageView) rootView.findViewById(R.id.imageView1);
			images[1] = (ImageView) rootView.findViewById(R.id.imageView2);
			images[2] = (ImageView) rootView.findViewById(R.id.imageView3);
			images[3] = (ImageView) rootView.findViewById(R.id.imageView4);
			viewHolder.imageViews = images;
			rootView.setTag(viewHolder);
		}
		else{ rootView = convertView; }

		ViewHolder viewHolder = (ViewHolder) rootView.getTag();
		ArtistListItem ali = getItem(position);

		viewHolder.textView.setText(ali.getArtistName());

		int size = ali.getTop4Images().size();
		for(int i = 0; i < size; i++){
			Picasso.with(getContext()).load(ali.getTop4Images().get(i).url).fit().into(viewHolder.imageViews[i]);
		}
		if(size > 0){ // in case the size is 0, there is no default '0' url to use
			String url = ali.getTop4Images().get(0).url;
			for(int i = size; i < 4; i++){ // fill the empties with the first art
				Picasso.with(getContext()).load(url).fit().into(viewHolder.imageViews[i]);
			}
		}
		return rootView;

	}

}
