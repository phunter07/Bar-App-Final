package com.version1_0.ClubCrawl;

/**
 * the class represents the set clock activity, the user can set the clock in this activity
 * @author Jiang Zhe Heng
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.version1_0.ClubCrawl.R;
import com.version1_0.sqlite.Club;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

public class SetClock extends ListActivity implements OnClickListener {

	private TextView alarmRecord;
	// private SharedPreferences sharedPreferences;
	private View dialogView;
	private TimePicker timePicker;
	private LinkedList<Club> leftClubs;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_clock);
		// add itself into activityList
		MyApplication.getInstance().addActivity(this);
		leftClubs=(LinkedList<Club>) MyApplication.getInstance().getHashMap()
				.get("clubList");
		Button setClock = (Button) findViewById(R.id.setClock_activityMain);
		Button cancelClock = (Button) findViewById(R.id.cancelClock);
//		Button addAlarm3 = (Button) findViewById(R.id.B45minutes_activityMain);
//		Button addAlarm4 = (Button) findViewById(R.id.B1hour_activityMain);
//		Button addAlarm5 = (Button) findViewById(R.id.B1_5hour_activityMain);
		alarmRecord = (TextView) findViewById(R.id.TVleave_activityMain);
		setClock.setOnClickListener(this);
		cancelClock.setOnClickListener(this);
//		addAlarm3.setOnClickListener(this);
//		addAlarm4.setOnClickListener(this);
//		addAlarm5.setOnClickListener(this);
		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		if(!leftClubs.isEmpty()){
		ArrayAdapter<Club> adapter = new ArrayAdapter<Club>(this,
				android.R.layout.simple_list_item_1, leftClubs);
		setListAdapter(adapter);
		}else{
			List<String> data=new ArrayList<String>();
			data.add("No club left");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, data);
			setListAdapter(adapter);
			
		}
		

		AlarmManager aManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		intent = new Intent(this, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		aManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60 * 1000,
				pendingIntent);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.getInstance().getHashMap().get("settingTime") != null) {
			alarmRecord.setText("I will leave at :"
					+ MyApplication.getInstance().getHashMap()
							.get("settingTime"));
		}

	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		MyApplication.getInstance().getHashMap().remove("settingTime");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setClock_activityMain:
			showAddAlarm(0, 30);
			break;
		case R.id.cancelClock:
			MyApplication.getInstance().getHashMap().remove("settingTime");
			alarmRecord.setText("I will leave at :");
			break;
//		case R.id.B45minutes_activityMain:
//			showAddAlarm(0, 45);
//			break;
//		case R.id.B1hour_activityMain:
//			showAddAlarm(1, 0);
//			break;
//		case R.id.B1_5hour_activityMain:
//			showAddAlarm(1, 30);
//			break;

		}
	}

	public void showAddAlarm(int extraHours, int extraMinutes) {
		dialogView = getLayoutInflater().inflate(R.layout.add_alarm, null);
		timePicker = (TimePicker) dialogView.findViewById(R.id.TP_settingClock);
		setTimePicker(extraHours, extraMinutes);
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Setting");
		dialog.setView(dialogView);
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				String timeStr = String.valueOf(timePicker.getCurrentHour())
						+ ":" + String.valueOf(timePicker.getCurrentMinute());
				if (timeStr != null) {
					alarmRecord.setText("I will leave at :" + timeStr);
					MyApplication.getInstance().getHashMap()
							.put("settingTime", timeStr);
				}

			}
		});
		dialog.setNegativeButton("Cancel", null);
		dialog.show();
	}

	public void setTimePicker(int extraHours, int extraMinutes) {

		Calendar calendar = Calendar.getInstance();
		int currentMinute = calendar.get(Calendar.MINUTE) + extraMinutes;
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY) + extraHours;
		Log.i(String.format("%02d", calendar.get(Calendar.MINUTE)),
				String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
		if (currentMinute < 60) {
			timePicker.setCurrentMinute(currentMinute);
		} else {
			timePicker.setCurrentMinute(currentMinute - 60);
			currentHour++;
		}
		if (currentHour < 24) {
			timePicker.setCurrentHour(currentHour);
		} else {
			timePicker.setCurrentHour(currentHour - 24);
		}
		timePicker.setIs24HourView(true);
	}

}
