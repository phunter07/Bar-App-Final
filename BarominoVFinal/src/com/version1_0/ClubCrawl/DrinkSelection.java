package com.version1_0.ClubCrawl;
/**
 * Brings the user to select their activity
 * Either a quick drink ( three closest pubs)
 * Or a Planned crawl were the user can select the pubs they want on their crawl
 * @author Team Baromino
 */
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import com.version1_0.ClubCrawl.R;

public class DrinkSelection extends Activity implements OnClickListener {
	private MediaPlayer mediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// add itself into activityList
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.drink_selection);
		// setting buttons for DrinkSelection Screen
		Button btnQuick = (Button) findViewById(R.id.btnQuick);
		Button btnPlan = (Button) findViewById(R.id.btnPlan);
		Button vidBtn = (Button) findViewById(R.id.videoButton);
		btnQuick.setOnClickListener(this);
		btnPlan.setOnClickListener(this);
		mediaPlayer = MediaPlayer.create(this, R.raw.nightlife_belfast);
		vidBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DrinkSelection.this,LoveBelfastVideo.class);
				startActivity(intent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Method for btnQuick layout change - brings the user to google maps with
	// the 3 closest bars to their location
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btnQuick:

			intent.setClassName("com.version1_0.ClubCrawl",
					"com.version1_0.GoogleMap.ShowNearestClubs");

			break;
		case R.id.btnPlan:
			intent.setAction("android.intent.action.PlanCrawl");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
		}
		startActivity(intent);

	}
}
