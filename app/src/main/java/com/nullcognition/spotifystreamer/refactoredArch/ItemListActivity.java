package com.nullcognition.spotifystreamer.refactoredArch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ItemListActivity extends AppCompatActivity
		implements ItemListFragment.Callbacks,
		           ItemDetailFragment.Callbacks{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ContentViewSetter.setActivity(this);

	}

	@Override
	public void onItemSelected(String id){
		if(!ContentViewSetter.isTablet()){
			ContentViewSetter.createDetail(null);
		}
	}

	@Override
	public void onItemSelectedDetail(final String id){
		ContentViewSetter.showDialog();
	}

	@Override
	public void onBackPressed(){
		ContentViewSetter.checkBackStack();
		super.onBackPressed();
	}
}
