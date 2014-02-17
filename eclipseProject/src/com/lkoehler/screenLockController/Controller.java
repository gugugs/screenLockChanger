package com.lkoehler.screenLockController;

import java.io.DataOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.lkoehler.sqlite.DAOStatus;
import com.lkoehler.sqlite.Status;

public class Controller {

	private boolean execute = false;

	private static final String SIMON_PATH = "/data/system/users/10/";
	private static final String SASCHA_PATH = "/data/system/users/10/";
	private static final String LEA_PATH = "/data/system/users/10/";

	private DAOStatus daoStatus;

	public Controller(Context context) {
		daoStatus = new DAOStatus(context);
	}

	public String fromNameToPath(String name) {
		if (name.equals("simon")) {
			return SIMON_PATH;
		} else if (name.equals("sascha")) {
			return SASCHA_PATH;
		} else if (name.equals("lea")) {
			return LEA_PATH;
		} else {
			return null;
		}
	}

	public void setExecute(boolean state) {
		execute = state;
	}

	public void updateScreenLock(String name, boolean state) {
		daoStatus.open();
		Status status = daoStatus.getByName(name);
		status.setStatus(state == true ? 1 : 0);
		daoStatus.updateStatus(status);
		daoStatus.close();

		String path = fromNameToPath(name);
		String command;
		if (state) {
			command = "mv " + path + "password.key.old " + path
					+ "password.key\n";
		} else {
			command = "mv " + path + "password.key " + path
					+ "password.key.old\n";
		}
		if (execute) {
			try {
				Process process = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(
						process.getOutputStream());
				os.writeBytes(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.w(Controller.class.getName(), command);
	}

	public void updateAll(boolean state) {
		String[] names = { "simon", "sascha", "lea" };
		for (String name : names) {
			updateScreenLock(name, state);
		}
	}

	public void toggle(String name) {
		daoStatus.open();
		Status status = daoStatus.getByName(name);
		daoStatus.close();
		if (status.getStatus() == 1 ? true : false) {
			updateScreenLock(name, false);
		} else {
			updateScreenLock(name, true);
		}
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
