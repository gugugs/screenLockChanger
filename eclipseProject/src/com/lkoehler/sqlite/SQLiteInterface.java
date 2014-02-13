/**
 * @(#) SQLiteInterface.java
 */

package com.lkoehler.sqlite;

import android.content.Context;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteInterface extends SQLiteOpenHelper {

	
	
public SQLiteInterface( Context context ) {
		super(context, "stati", null, 1);
	}

	@Override
	public void onCreate( SQLiteDatabase db ) {
		db.execSQL("create table stati(id integer primary key autoincrement, name text not null, status integer)");
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		Log.w(SQLiteInterface.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS stati");
		onCreate(db);
	}
}
