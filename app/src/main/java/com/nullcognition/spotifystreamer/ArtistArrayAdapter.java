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
	//	@Override
//	public View getView(final int position, final View convertView, final ViewGroup parent){
//		View rootView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_item, parent, false);
//
//		ArtistListItem ali = getItem(position);
//
//		((TextView) rootView.findViewById(R.id.textView)).setText(ali.getArtistName());
//
//		ImageView[] images = new ImageView[4];
//		images[0] = (ImageView) rootView.findViewById(R.id.imageView1);
//		images[1] = (ImageView) rootView.findViewById(R.id.imageView2);
//		images[2] = (ImageView) rootView.findViewById(R.id.imageView3);
//		images[3] = (ImageView) rootView.findViewById(R.id.imageView4);
//
//
//		int size = ali.getTop4Images().size();
//		for(int i = 0; i < size; i++){
//			Picasso.with(getContext()).load(ali.getTop4Images().get(i).url).fit().into(images[i]);
//
//		}
//		return rootView;
//
//	}
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent){
		View rootView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_item, parent, false);

		ArtistListItem ali = getItem(position);

		((TextView) rootView.findViewById(R.id.textView)).setText(ali.getArtistName());

		ImageView[] images = new ImageView[4];
		images[0] = (ImageView) rootView.findViewById(R.id.imageView1);
		images[1] = (ImageView) rootView.findViewById(R.id.imageView2);
		images[2] = (ImageView) rootView.findViewById(R.id.imageView3);
		images[3] = (ImageView) rootView.findViewById(R.id.imageView4);


		int size = ali.getTop4Images().size();
		for(int i = 0; i < size; i++){
			Picasso.with(getContext()).load(ali.getTop4Images().get(i).url).fit().into(images[i]);
		}
		if(size > 0){ // in case the size is 0, there is no default '0' url to use
			for(int i = size; i < 4; i++){ // fill the empties with the first art
				Picasso.with(getContext()).load(ali.getTop4Images().get(0).url).fit().into(images[i]);
			}
		}
		return rootView;

	}

	//	@Override
//	public View getView(int position, View convertView, ViewGroup parent){
//
//		View rootView;
//		Context context = getContext();
//
//		if(convertView == null){
//			rootView = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false);
//			ViewHolder viewHolder = new ViewHolder();
//
//			viewHolder.textView = (TextView) rootView.findViewById(R.id.textView);
//
//			ImageView[] images = new ImageView[4];
//			images[0] = (ImageView) rootView.findViewById(R.id.imageView1);
//			images[1] = (ImageView) rootView.findViewById(R.id.imageView2);
//			images[2] = (ImageView) rootView.findViewById(R.id.imageView3);
//			images[3] = (ImageView) rootView.findViewById(R.id.imageView4);
//			viewHolder.imageViews = images;
//			rootView.setTag(viewHolder);
//		}
//		else{ rootView = convertView; }
//
//		ViewHolder viewHolder = (ViewHolder) rootView.getTag();
//		ArtistListItem artistListItem = getItem(position);
//
//		viewHolder.textView.setText(artistListItem.getArtistName());
//
//		ArrayList<Image> top4Images = artistListItem.getTop4Images();
//		Picasso.with(context).load(top4Images.get(0).url).into(viewHolder.imageViews[0]);
//		// todo make the first image bigger than the rest
//		for(int i = 1; i < 4; i++){
//			Picasso.with(context).load(top4Images.get(i).url).into(viewHolder.imageViews[i]);
//		}
//		return convertView;
//	}


}
