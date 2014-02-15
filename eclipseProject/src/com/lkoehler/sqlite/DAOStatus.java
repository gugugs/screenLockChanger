/**
 * @(#) DAOStatus.java
 */

package com.lkoehler.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DAOStatus {
	private static SQLiteDatabase database;

	private SQLiteInterface sqlInterface;

	public DAOStatus(Context context) {
		sqlInterface = new SQLiteInterface(context);
	}

	public void open() throws SQLException {
		database = sqlInterface.getWritableDatabase();
	}

	public void close() {
		sqlInterface.close();
	}

	public Status getByName(String name) {
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

	public void updateStatus(Status status) {
		database.execSQL("update stati set status = '"
				+ String.valueOf(status.getStatus()) + "' where id = '"
				+ status.getId() + "'");
	}
}
