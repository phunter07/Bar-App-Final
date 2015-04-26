package com.version1_0.ClubCrawl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.help_page);
		super.onCreate(savedInstanceState);

		Button buttonBack = (Button) findViewById(R.id.backButton);

		buttonBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HelpPage.this, LoginPage.class);
				startActivity(intent);

			}
		});

	}

}
