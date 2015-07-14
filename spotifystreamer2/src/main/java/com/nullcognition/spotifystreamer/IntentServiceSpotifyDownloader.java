package com.nullcognition.spotifystreamer;// Created by ersin on 14/07/15

import android.app.IntentService;
import android.content.Intent;

import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;

@IntentBuilder
public class IntentServiceSpotifyDownloader extends IntentService{

	@Extra
	String url;

	public IntentServiceSpotifyDownloader(){super(IntentServiceSpotifyDownloader.class.getSimpleName());}

	public IntentServiceSpotifyDownloader(final String name){
		super(name);
	}

	@Override
	protected void onHandleIntent(final Intent intent){
		IntentServiceSpotifyDownloaderIntentBuilder.inject(intent, this);
	}
}
