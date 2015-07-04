package com.nullcognition.spotifystreamer;// Created by ersin on 03/07/15

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.musenkishi.atelier.Atelier;
import com.musenkishi.atelier.ColorType;
import com.musenkishi.atelier.swatch.VibrantSwatch;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistArrayAdapter extends ArrayAdapter<ArtistListItem>{

	static class ViewHolder{
		public ImageView[] imageViews;
		public TextView textView;
		public LinearLayout rootView;
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

			ImageView[] images = new ImageView[4];
			images[0] = (ImageView) rootView.findViewById(R.id.imageView1);
			images[1] = (ImageView) rootView.findViewById(R.id.imageView2);
			images[2] = (ImageView) rootView.findViewById(R.id.imageView3);
			images[3] = (ImageView) rootView.findViewById(R.id.imageView4);
			viewHolder.imageViews = images;

			viewHolder.rootView = (LinearLayout) rootView.findViewById(R.id.linLayout);
			viewHolder.textView = (TextView) rootView.findViewById(R.id.textView);
			rootView.setTag(viewHolder);
		}
		else{ rootView = convertView; }

		final ViewHolder viewHolder = (ViewHolder) rootView.getTag();
		ArtistListItem ali = getItem(position);

		viewHolder.textView.setText(ali.getArtistName());

		int size = ali.getTop4Images().size();
		// unfortunately the top four images are the same, thus the other 3 are not needed
			// test
		if(size == 0){
			final int url = android.R.drawable.sym_def_app_icon;
			Picasso.with(getContext())
			       .load(url)
			       .fit()
			       .into(viewHolder.imageViews[0], new Callback.EmptyCallback(){
				@Override
				public void onSuccess(){
					super.onSuccess();
					Drawable drawable = viewHolder.imageViews[0].getDrawable();
					if(drawable instanceof BitmapDrawable){

						Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
						Context context1 = getContext();
						Atelier.with(context1, String.valueOf(url))
						       .load(bitmap)
						       .swatch(new VibrantSwatch(ColorType.BACKGROUND))
						       .into(viewHolder.rootView);

						Atelier.with(context1, String.valueOf(url))
						       .load(bitmap)
						       .swatch(new VibrantSwatch(ColorType.TEXT_TITLE))
						       .into(viewHolder.textView);
					}
				}

			});
		}
		else{
			for(int i = 0; i < size; i++){
				final int finalI = i;
				final String url = ali.getTop4Images().get(0).url;
				Picasso.with(getContext()).load(url).fit().into(viewHolder.imageViews[i], new Callback.EmptyCallback(){
					@Override
					public void onSuccess(){
						super.onSuccess();
						Drawable drawable = viewHolder.imageViews[finalI].getDrawable();
						if(drawable instanceof BitmapDrawable){

							Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
							Context context1 = getContext();
							Atelier.with(context1, url)
							       .load(bitmap)
							       .swatch(new VibrantSwatch(ColorType.BACKGROUND))
							       .into(viewHolder.rootView);

							Atelier.with(context1, url)
							       .load(bitmap)
							       .swatch(new VibrantSwatch(ColorType.TEXT_TITLE))
							       .into(viewHolder.textView);
						}
					}

				});
			}
		}

		// removed the other images from the view, needed if using custom adapter to display more than 1 image
//		if(size > 0){
//			final String url = ali.getTop4Images().get(0).url;
//			for(int i = size; i < 4; i++){ // fill the empties with the first art
//				final int finalI = i;
//				Picasso.with(getContext()).load(url).fit().into(viewHolder.imageViews[i], new Callback.EmptyCallback(){
//							@Override
//							public void onSuccess(){
//								super.onSuccess();
//								Drawable drawable = viewHolder.imageViews[finalI].getDrawable();
//								if(drawable instanceof BitmapDrawable){
//
//									Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//									Context context1 = getContext();
//									Atelier.with(context1, url)
//									       .load(bitmap)
//									       .swatch(new VibrantSwatch(ColorType.BACKGROUND))
//									       .into(viewHolder.rootView);
//
//									Atelier.with(context1, url)
//									       .load(bitmap)
//									       .swatch(new VibrantSwatch(ColorType.TEXT_TITLE))
//									       .into(viewHolder.textView);
//								}
//							}
//
//						}
//				);
//			}
//		}
		return rootView;
	}
}

