package com.version1_0.ClubCrawl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class LoveBelfastVideo extends Activity {
	private static final String FONA_CAB = "http://www.fonacab.com/";
	private static final String VALUE_CAB = "http://www.valuecabs.co.uk/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.love_belfast);
		super.onCreate(savedInstanceState);

		Button fonaBtn = (Button) findViewById(R.id.fonaCab);
		Button valueBtn = (Button) findViewById(R.id.valueCab);
		Button homeBtm = (Button) findViewById(R.id.homeBtn);

		fonaBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(FONA_CAB));
				startActivity(intent);

			}
		});

		valueBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(VALUE_CAB));
				startActivity(intent);

			}
		});

	
		homeBtm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentHome = new Intent(LoveBelfastVideo.this,
						DrinkSelection.class);
				startActivity(intentHome);

			}
		});
		VideoView vid = (VideoView) findViewById(R.id.loveBelfastVideo);

		String urlPath = "android.resource://" + getPackageName() + "/"
				+ R.raw.nightlife_belfast;

		vid.setVideoURI(Uri.parse(urlPath));

		vid.start();

	}

}
