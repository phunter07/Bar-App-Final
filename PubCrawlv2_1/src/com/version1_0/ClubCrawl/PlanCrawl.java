package com.version1_0.ClubCrawl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import java.util.HashMap;

import com.version1_0.sqlite.Club;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This page requires a basic menu Functionality should include Load crawl
 * button placed at the top right Save and start Crawl button placed at top left
 * (deactivated unless two or more bars are in the list) - Once clicked a
 * message pops up to allow input of a name with save and cancel buttons, once
 * submitted then the user is taken to the Crawl page
 * 
 * A list of current bars which can be reordered by drag and drop, removed by a
 * double click
 * 
 * Swipe left to reveal all bars ordered by distance away from current location
 * - contains a filter at the top with categories - to be defined - from here
 * they can be added to the crawl by double click - long press reveals
 * additional information in a pop-up with a add to crawl button and back button
 * - potential 'bar not here? Add it' option
 * 
 * Swipe right to see currently selected bars and route displayed on a map -
 * numbered place holders?
 * 
 * @author Matt
 * 
 */
public class PlanCrawl extends Activity {

	ListView listView;
	Context mContext;
	MyListAdapter adapter;
	List<Integer> listItemID = new ArrayList<Integer>();
	List<Club> clubList;

	/**
	 * add the component of the view
	 * 
	 * @author Jiang Zhe Heng
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan_crawl);
		// add itself into activityList
		MyApplication.getInstance().addActivity(this);
		Button start = (Button) findViewById(R.id.start);
		Button cancle=(Button) findViewById(R.id.cancel);
		// use method getAllComments() of datasource and
		clubList = MyApplication.getInstance().getAllClubList();

		mContext = getApplicationContext();

		listView = (ListView) findViewById(R.id.clubListView);

		adapter = new MyListAdapter(clubList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//get viewHolder
				ViewHolder holder=(ViewHolder) view.getTag();
				//change the status of checkBox
				holder.checked.toggle();
				adapter.mChecked.set(position, holder.checked.isChecked());
				
			}
		});

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<Club> checkedClub = new LinkedList<Club>();
				// check if the checkbox is checked
				for (int loop = 0; loop < adapter.mChecked.size(); loop++) {
					if (adapter.mChecked.get(loop)) {
						checkedClub.add(clubList.get(loop));

					}
				}
				// if there are some clubs been checked,store them into hash map
				if (!checkedClub.isEmpty()) {

					MyApplication.getInstance().getHashMap()
							.put("clubList", checkedClub);
					Intent intent = new Intent();
					intent.setClassName("com.version1_0.ClubCrawl",
							"com.version1_0.GoogleMap.GoogleMaps");
					startActivity(intent);
				}
			}

		});
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				adapter = new MyListAdapter(clubList);
				listView.setAdapter(adapter);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.plan_crawl, menu);
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

	class MyListAdapter extends BaseAdapter {
		private List<Boolean> mChecked;
		private List<Club> clubList;

		public MyListAdapter(List<Club> list) {
			clubList = new ArrayList<Club>();
			clubList = list;

			mChecked = new ArrayList<Boolean>();
			for (int i = 0; i < list.size(); i++) {
				mChecked.add(false);
			}
		}

		@Override
		public int getCount() {
			return clubList.size();
		}

		@Override
		public Object getItem(int position) {
			return clubList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Get a View that displays the data at the specified position in the
		 * data set.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item, null);
				holder.checked =  (CheckBox) convertView.findViewById(R.id.list_checkBox);
				holder.name =  (TextView) convertView.findViewById(R.id.list_name);

				convertView.setTag(holder);
			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(clubList.get(position).getName());
			
			holder.checked.setChecked(mChecked.get(position));
			return convertView;
		}

	}

	/**
	 * the class represent the view of an item of a list view
	 * 
	 * @author Jiang Zhe Heng
	 * 
	 */
	static class ViewHolder {
		CheckBox checked;
		TextView name;
	}
}
