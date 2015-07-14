//package com.nullcognition.spotifystreamer.dummy;// Created by ersin on 13/07/15
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.hannesdorfmann.annotatedadapter.AbsListViewAnnotatedAdapter;
//import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
//import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
//import com.nullcognition.spotifystreamer.ListAdapterBinder;
//import com.nullcognition.spotifystreamer.ListAdapterHolders;
//import com.nullcognition.spotifystreamer.R;
//
//import java.util.List;
//
//public class ListAdapter extends AbsListViewAnnotatedAdapter implements ListAdapterBinder{
//
//	@ViewType(
//			layout = R.layout.recycler_item_image_text,
//			views = {
//					@ViewField(id = R.id.textView, name = "text", type = TextView.class),
//					@ViewField(id = R.id.imageView, name = "image", type = ImageView.class)
//			},
//			initMethod = true)
//	public final int rowSimple = 0;
//
//	private List<String> items;
//
//	public ListAdapter(Context context, List<String> items){
//		super(context);
//		this.items = items;
//	}
//
//	@Override
//	public int getItemViewType(int position){
//		return rowSimple;
//	}
//
//	@Override
//	public int getCount(){
//		return items == null ? 0 : items.size();
//	}
//
//	@Override
//	public Object getItem(int position){
//		return items.get(position);
//	}
//
//	@Override
//	public long getItemId(int position){
//		return 0;
//	}
//
//	@Override
//	public void initViewHolder(ListAdapterHolders.RowSimpleViewHolder vh, View view,
//	                           ViewGroup parent){
////		vh.text.setTextColor(Color.RED);
//	}
//
//	@Override
//	public void bindViewHolder(ListAdapterHolders.RowSimpleViewHolder vh, int position){
//		String item = items.get(position);
//		vh.text.setText(item);
//		vh.image.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
//	}
//
//}
