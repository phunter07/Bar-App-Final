package com.version1_0.sqlite;

/**
 * Data source
 * @author Jiang Zhe Heng
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ClubDataSource {

	// Database fields
	private SQLiteDatabase database;
	private ClubSQLiteHelper dbHelper;
	private String[] allColumns = {ClubSQLiteHelper.COLUMN_BAR_ID,
			ClubSQLiteHelper.COLUMN_BAR_NAME,
			ClubSQLiteHelper.COLUMN_BAR_LAT,
			ClubSQLiteHelper.COLUMN_BAR_LONG};// define the format of string
												// allColumns

	public ClubDataSource(Context context) {
		// instantiate the RUSQLiteHelper class
		dbHelper = new ClubSQLiteHelper(context);
	}

	public void open() throws SQLException {
		// open the database for reading and writing
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void createClub( String name,double latitude,double longitude) {
		// instantiate the class ContentValues
		ContentValues values = new ContentValues();

		// initialise values to be inserted to the database
		values.put(ClubSQLiteHelper.COLUMN_BAR_ID, "null");
		values.put(ClubSQLiteHelper.COLUMN_BAR_NAME, name);
		values.put(ClubSQLiteHelper.COLUMN_BAR_LAT, latitude);
		values.put(ClubSQLiteHelper.COLUMN_BAR_LONG, longitude);

		// Returns a new InsertQuery object for the active database
		database.insert(ClubSQLiteHelper.TABLE_BARS, null, values);

	}

	public void deleteComment(Club club) {
		int id = club.getId();

		// printf(). should use log.w() instead
		System.out.println("Comment deleted with id: " + id);
		// delete records in the database
		database.delete(ClubSQLiteHelper.COLUMN_BAR_ID,
				ClubSQLiteHelper.COLUMN_BAR_ID + " = " + id, null);
	}

	public List<Club> getAllClubs() {
		// instantiate List<Commnet>
		// can we change it to: List<Comment> comments = new
		// ArrayList<Comment>(this);
		List<Club> clubs = new ArrayList<Club>();

		Cursor cursor = database.query(ClubSQLiteHelper.TABLE_BARS,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Club comment = cursorToClub(cursor);
			clubs.add(comment);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return clubs;
	}

	public RegisteredUser getUserInformation(String userName, String passWord) {
		RegisteredUser user=new RegisteredUser();
		String[] args={userName};

		Cursor cursor = database.query(RUSQLiteHelper.TABLE_COMMENTS,
				allColumns, RUSQLiteHelper.COLUMN_USERNAME + " = ?",
				args, null, null, null);
		Log.i(String.valueOf(cursor.getCount()),String.valueOf(cursor.getCount()));
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			user.setUserName(cursor.getString(0));
			user.setPassword(cursor.getString(1));
			cursor.close();
			return user;
		}else{
			cursor.close();
			return null;
		}
		
		
	}

	private Club cursorToClub(Cursor cursor) {
		Club club = new Club(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
		
		return club;
	}
	
	public int insertFromFile(Context context, int resourceId) throws IOException {
	    // Reseting Counter
	    int result = 0;

	    // Open the resource
	    InputStream insertsStream = context.getResources().openRawResource(resourceId);
	    BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

	    // Iterate through lines (assuming each insert has its own line and theres no other stuff)
	    while (insertReader.ready()) {
	        String insertStmt = insertReader.readLine();
	        database.execSQL(insertStmt);
	        result++;
	    }
	    insertReader.close();

	    // returning number of inserted rows
	    return result;
	}
	
	public void reWriteTable(){
		dbHelper.onUpgrade(database, 0, 0);
	}
}
