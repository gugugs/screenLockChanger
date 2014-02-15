/**
 * @(#) Controller.java
 */

package com.lkoehler.screenLockController;

import java.io.DataOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.lkoehler.sqlite.DAOStatus;
import com.lkoehler.sqlite.Status;

public class Controller {

	public DAOStatus daoStatus;

	public Controller(Context context) {
		daoStatus = new DAOStatus(context);
	}

	public void lockOn(String name) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(
					process.getOutputStream());
			os.writeBytes("mv /data/system/password.key.old /data/system/password.key\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void lockOff(String name) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(
					process.getOutputStream());
			os.writeBytes("mv /data/system/password.key /data/system/password.key.old\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateScreenLock(String name, boolean state) {

		daoStatus.open();
		Status status = daoStatus.getByName(name);
		status.setStatus(state == true ? 1 : 0);
		daoStatus.updateStatus(status);
		daoStatus.close();

		Log.w(Controller.class.getName(), "ScreenLock fuer " + name
				+ " wurde auf " + state + " gesetzt.");
	}

	public void updateAll(boolean state) {
		String[] names = { "simon", "sascha", "lea" };
		for (String name : names) {
			updateScreenLock(name, state);
		}

		Log.w(Controller.class.getName(), "ScreenLock wurde fuer jeden auf "
				+ state + " gesetzt");
	}

	public void toggle(String name) {
		daoStatus.open();
		Status status = daoStatus.getByName(name);
		boolean state = true;
		if (status.getStatus() == 1 ? true : false) {
			status.setStatus(0);
			state = false;
		} else {
			status.setStatus(1);
			state = true;
		}
		daoStatus.updateStatus(status);
		daoStatus.close();

		Log.w(Controller.class.getName(), "ScreenLock wurde fuer " + name
				+ " auf " + state + " geschalten.");
	}

	public void toggleAll() {
		String[] names = { "simon", "sascha", "lea" };
		boolean allOn = true;
		boolean change = true;
		for (String name : names) {
			if (getStatus(name) == false) {
				allOn = false;
				break;
			}
		}
		if (allOn) {
			change = false;
		}

		boolean allOff = true;
		for (String name : names) {
			if (getStatus(name) == true) {
				allOff = false;
				break;
			}
		}
		if (allOff) {
			change = true;
		}

		for (String name : names) {
			updateScreenLock(name, change);
		}
	}

	public boolean getStatus(String name) {
		daoStatus.open();
		Status status = daoStatus.getByName(name);
		daoStatus.close();
		return (status.getStatus() == 1 ? true : false);
	}
}
