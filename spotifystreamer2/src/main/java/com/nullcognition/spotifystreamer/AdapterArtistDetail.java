package com.nullcognition.spotifystreamer;// Created by ersin on 13/07/15

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Image;

public class AdapterArtistDetail extends SupportAnnotatedAdapter
		implements AdapterArtistDetailBinder{

	static class MyClickListener implements View.OnClickListener{

		public int position;
		public void onClick(View v){
			listener.positionClicked(position);
		}
	}

	@ViewType(
			layout = R.layout.recycler_item_image_text,
			views = {
					@ViewField(id = R.id.textView, name = "text", type = TextView.class),
					@ViewField(id = R.id.imageView, name = "image", type = ImageView.class)
			},
			fields = {
					@Field(type = AdapterArtistDetail.MyClickListener.class, name = "clickListener")
			}
	)
	public final int rowWithPic = 0;

	private List<Track> items;
	private Context context;
	static public RecyclerItemViewClick listener;


	public AdapterArtistDetail(Context context, List<Track> items, RecyclerItemViewClick inListener){
		super(context);
		this.context = context;
		this.items = items;
		listener = inListener;
	}

	@Override
	public int getItemCount(){ return items == null ? 0 : items.size();}

	// may be used to alternate view types like: X-Y-X-Y-X-Y or to whatever pattern you choose
	@Override
	public int getItemViewType(int position){ return rowWithPic;}

	@Override
	public void bindViewHolder(AdapterArtistDetailHolders.RowWithPicViewHolder vh, int position){

		Track track = items.get(position);
		vh.text.setText(track.album.name+"\n"+track.name);
		setImage(vh.image, track.album.images);

		vh.clickListener = new AdapterArtistDetail.MyClickListener();
		vh.itemView.setOnClickListener(vh.clickListener);
		vh.clickListener.position = position;
	}
	private void setImage(final ImageView imageView, final List<Image> images){
		String[] imageUrl = new String[2];

		if(!images.isEmpty()){
			imageUrl[0] = images.get(0).url;
			imageUrl[1] = images.get(images.size() - 1).url;
		}
		// may toggle with 0, or 1 from best to worst depending on network connectivity, Wi-fi vs 3/4/5G

		Glide.with(context)
		     .load(imageUrl[0])
		     .error(android.R.drawable.star_big_on)
		     .centerCrop()
		     .into(imageView);
	}

	public interface RecyclerItemViewClick{
		void positionClicked(int position);
	}
}
