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

public class SampleAdapter extends SupportAnnotatedAdapter
		implements SampleAdapterBinder, View.OnClickListener{

	@ViewType(
			layout = R.layout.row_with_pic,
			views = {
					@ViewField(id = R.id.textView, name = "text", type = TextView.class),
					@ViewField(id = R.id.imageView, name = "image", type = ImageView.class)
			},
			fields = {
					@Field(
							type = SampleAdapter.OnClickListener.class,
							name = "clickListener"
					)
			}
	)
	public final int rowWithPic = 0;

	List<String> items;
	SampleAdapter.OnClickListener listener;

	public SampleAdapter(Context context, List<String> items, SampleAdapter.OnClickListener listener){
		super(context);
		this.items = items;
		this.listener = listener;
	}

	@Override
	public int getItemCount(){ return items == null ? 0 : items.size();}

	// this is to give views like X-Y-X-Y-X-Y or whatever pattern you choose
	@Override
	public int getItemViewType(int position){ return rowWithPic;}

	@Override
	public void bindViewHolder(SampleAdapterHolders.RowWithPicViewHolder vh, int position){

		String str = items.get(position);
		vh.text.setText(str);
		vh.text.setOnClickListener(this);
		vh.image.setOnClickListener(this);
		vh.image.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
		vh.clickListener = listener;
//		vh.getAdapterPosition();
	}


	@Override
	public void onClick(final View v){
		if(v instanceof ImageView){listener.onImageClick((ImageView) v);}
		else if(v instanceof TextView){listener.onTextClick((TextView) v);}
	}

	public interface OnClickListener{
		void onImageClick(ImageView v);
		void onTextClick(TextView v);
	}


}
