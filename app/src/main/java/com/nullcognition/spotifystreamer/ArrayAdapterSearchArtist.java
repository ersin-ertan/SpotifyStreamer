package com.nullcognition.spotifystreamer;// Created by ersin on 04/07/15

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class ArrayAdapterSearchArtist extends ArrayAdapter<Artist>{
	public ArrayAdapterSearchArtist(final Context context, final List<Artist> resource){
		super(context, 0, resource);
	}

	static class ViewHolder{
		public ImageView imageView;
		public TextView textView;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent){

		View rootView;
		Context context = getContext();

		if(convertView == null){
			rootView = LayoutInflater.from(context).inflate(R.layout.item_search_artist, parent, false);
			ViewHolder viewHolder = new ViewHolder();

			viewHolder.imageView = (ImageView) rootView.findViewById(R.id.image_item_search_artist);
			viewHolder.textView = (TextView) rootView.findViewById(R.id.text_item_search_artist);

			rootView.setTag(viewHolder);
		}
		else{ rootView = convertView; }

		final ViewHolder viewHolder = (ViewHolder) rootView.getTag();
		Artist artist = getItem(position);

		viewHolder.textView.setText(artist.name);
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), context.getString(R.string.font_type));
		viewHolder.textView.setTypeface(font);

		String imageUrl = null;
		if(!artist.images.isEmpty()){ imageUrl = artist.images.get(0).url;}
		Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.logo_spotify).fit().into(viewHolder.imageView);

		return rootView;
	}
}
