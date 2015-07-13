package com.nullcognition.spotifystreamer.refactoredArch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nullcognition.spotifystreamer.R;

/**
 An activity representing a single Item detail screen. This
 activity is only used on handset devices. On tablet-size devices,
 item details are presented side-by-side with a list of items
 in a {@link ItemListActivity}.
 <p/>
 This activity is mostly just a 'shell' activity containing nothing
 more than a {@link ItemDetailFragment}.
 */
public class ItemDetailActivity extends AppCompatActivity implements ItemDetailFragment.Callbacks{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if(savedInstanceState == null){
			Bundle arguments = new Bundle();
//			arguments.putString(ItemDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
			                           .add(R.id.item_detail_container, fragment)
			                           .commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if(id == android.R.id.home){
			NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onItemSelectedDetail(final String id){

	}
}
