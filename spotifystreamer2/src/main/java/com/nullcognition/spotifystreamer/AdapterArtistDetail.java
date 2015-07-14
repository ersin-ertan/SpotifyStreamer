package com.nullcognition.spotifystreamer;// Created by ersin on 13/07/15

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

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

	List<String> items;
	static RecyclerItemViewClick listener;

	public AdapterArtistDetail(Context context, List<String> items, RecyclerItemViewClick inListener){
		super(context);
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

		String str = items.get(position);
		vh.text.setText(str);
		vh.image.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);

		vh.clickListener = new AdapterArtistDetail.MyClickListener();
		vh.itemView.setOnClickListener(vh.clickListener);
		vh.clickListener.position = position;
	}

	public interface RecyclerItemViewClick{
		void positionClicked(int position);
	}
}
