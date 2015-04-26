package com.version1_0.ClubCrawl;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class LoveBelfastVideo extends Activity {
	
	//private static final String MOVIE_URL = "https://www.youtube.com/watch?v=h7p0lNrQyCs";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.love_belfast);
		super.onCreate(savedInstanceState);
		
		VideoView vid = (VideoView) findViewById(R.id.loveBelfastVideo);
		
		String urlPath = "android.resource://" + getPackageName() + "/" + R.raw.nightlife_belfast;
		
		
		vid.setVideoURI(Uri.parse(urlPath));
		
		vid.start();
		
		
		

	}

}
