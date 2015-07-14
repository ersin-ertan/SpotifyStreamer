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
		implements SampleAdapterBinder{

	static class MyClickListener implements View.OnClickListener{

		public int position;
		public void onClick(View v){
			listener.p(position);
		}
	}

	@ViewType(
			layout = R.layout.row_with_pic,
			views = {
					@ViewField(id = R.id.textView, name = "text", type = TextView.class),
					@ViewField(id = R.id.imageView, name = "image", type = ImageView.class)
			},
			fields = {
					@Field(
							type = SampleAdapter.MyClickListener.class,
							name = "clickListener"
					)
			}
	)
	public final int rowWithPic = 0;

	List<String> items;
	static SampleAdapter.SL listener;

	public SampleAdapter(Context context, List<String> items, SampleAdapter.SL inListener){
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
	public void bindViewHolder(SampleAdapterHolders.RowWithPicViewHolder vh, int position){

		String str = items.get(position);
		vh.text.setText(str);
		vh.image.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);

		vh.clickListener = new MyClickListener();
		vh.itemView.setOnClickListener(vh.clickListener);
		vh.clickListener.position = position;
	}

	public interface SL{
		void p(int p);
	}
}
