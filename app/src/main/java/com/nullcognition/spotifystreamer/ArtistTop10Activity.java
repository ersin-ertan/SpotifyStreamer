package com.nullcognition.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ArtistTop10Activity extends AppCompatActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_top10);

		getIntentExtras();
	}
	private void getIntentExtras(){
		String s = getIntent().getStringExtra(MainActivityFragment.NAME_ARTIST);
		Toast.makeText(ArtistTop10Activity.this, s, Toast.LENGTH_SHORT).show();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_artist_top10, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if(id == R.id.action_settings){
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static void startActivity(Context context, Bundle bundle){
		Intent intent = new Intent(context, ArtistTop10Activity.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}
