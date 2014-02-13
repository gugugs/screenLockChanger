/**
 * @(#) DAOStatus.java
 */

package com.lkoehler.sqlite;

import android.database.Cursor;
import android.database.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

public class DAOStatus {
	private SQLiteDatabase database;

	private SQLiteInterface sqlInterface;

	public DAOStatus( Context context ) {
		sqlInterface = new SQLiteInterface(context);
	}

	public void open( ) throws SQLException {
		database = sqlInterface.getWritableDatabase();
	}

	public void close( ) {
		sqlInterface.close();
	}

	public void init( String[] names ) {
		for (String name : names) {
			ContentValues values = new ContentValues();
			values.put("name", name);
			values.put("status", 0);
			database.insert("stati", null, values);
		}
	}

	public Status getById( long id ) {
		String[] columns = { "id", "name", "status" };
		Cursor cursor = database.query("stati", columns,
				"id = " + String.valueOf(id), null, null, null, null);
		Status status = new Status(cursor.getLong(0), cursor.getString(1),
				cursor.getInt(2));
		return status;
	}

	public Status getByName( String name ) {
		String[] columns = { "id", "name", "status" };
		Cursor cursor = database.query("stati", columns, "name = '" + name
				+ "'", null, null, null, null);
		cursor.moveToFirst();
		long id = cursor.getLong(0);
		String sName = cursor.getString(1);
		int sStatus = cursor.getInt(2);
		Status returnStatus = new Status(id, sName, sStatus);
		cursor.close();
		return returnStatus;
	}

	public void updateStatus( Status status ) {
		database.execSQL("update stati set status = '"
				+ String.valueOf(status.getStatus()) + "' where id = '"
				+ status.getId() + "'");
	}
}
