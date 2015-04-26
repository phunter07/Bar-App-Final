package com.version1_0.ClubCrawl;
/**
 * shows the user the rules to the app and what they must do to use the app
 * @author Team Baromino
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpPage extends Activity {
	// sets up the help page with the pub rules
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.help_page);
		super.onCreate(savedInstanceState);
		// find the home button
		Button buttonBack = (Button) findViewById(R.id.backButton);
		// register click with home button - brings you back to the login page
		buttonBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HelpPage.this, LoginPage.class);
				startActivity(intent);

			}
		});

	}

}
