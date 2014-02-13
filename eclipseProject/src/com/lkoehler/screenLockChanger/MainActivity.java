package com.lkoehler.screenLockChanger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

import com.lkoehler.screenLockController.Controller;
import com.lkoehler.sqlite.DAOStatus;
import com.lkoehler.sqlite.Status;

public class MainActivity extends Activity {
	private DAOStatus daoStatus;

	private Controller controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		daoStatus = new DAOStatus(this);
		controller = new Controller();

		daoStatus.open();
		String[] names = { "simon", "sascha", "lea" };
		daoStatus.init(names);

		Status status;
		ToggleButton tButton;
		ToggleButton allButton = (ToggleButton) findViewById(R.id.allButton);
		allButton.setOnClickListener(listener);

		tButton = (ToggleButton) findViewById(R.id.simonButton);
		tButton.setOnClickListener(listener);
		status = daoStatus.getByName("simon");
		boolean setAllButton = true;

		if (status.getStatus() == 1) {
			tButton.setChecked(true);
		} else {
			setAllButton = false;
		}

		tButton = (ToggleButton) findViewById(R.id.saschaButton);
		tButton.setOnClickListener(listener);
		status = daoStatus.getByName("sascha");
		if (status.getStatus() == 1) {
			tButton.setChecked(true);
		} else {
			setAllButton = false;
		}

		tButton = (ToggleButton) findViewById(R.id.leaButton);
		tButton.setOnClickListener(listener);
		status = daoStatus.getByName("lea");
		if (status.getStatus() == 1) {
			tButton.setChecked(true);
		} else {
			setAllButton = false;
		}

		if (setAllButton)
			allButton.setChecked(true);
	}

	public void updateAllChecked() {
		ToggleButton tButton;
		boolean setAllButton = true;
		ToggleButton allButton = (ToggleButton) findViewById(R.id.allButton);

		tButton = (ToggleButton) findViewById(R.id.simonButton);
		if (!tButton.isChecked())
			setAllButton = false;

		tButton = (ToggleButton) findViewById(R.id.saschaButton);
		if (!tButton.isChecked())
			setAllButton = false;

		tButton = (ToggleButton) findViewById(R.id.leaButton);
		if (!tButton.isChecked())
			setAllButton = false;

		if (setAllButton)
			allButton.setChecked(true);
		else
			allButton.setChecked(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Status status = null;

			if (v.getId() == R.id.simonButton) {
				status = daoStatus.getByName("simon");
				updateAllChecked();

			} else if (v.getId() == R.id.saschaButton) {
				status = daoStatus.getByName("sascha");
				updateAllChecked();

			} else if (v.getId() == R.id.leaButton) {
				status = daoStatus.getByName("lea");
				updateAllChecked();

			} else if (v.getId() == R.id.allButton) {
				ToggleButton tButton = (ToggleButton) findViewById(v.getId());
				boolean bState = false;
				int state = 0;
				if (tButton.isChecked()) {
					state = 1;
					bState = true;
				}

				tButton = (ToggleButton) findViewById(R.id.simonButton);
				status = daoStatus.getByName("simon");
				status.setStatus(state);
				daoStatus.updateStatus(status);
				tButton.setChecked(bState);

				tButton = (ToggleButton) findViewById(R.id.saschaButton);
				status = daoStatus.getByName("sascha");
				status.setStatus(state);
				daoStatus.updateStatus(status);
				tButton.setChecked(bState);

				tButton = (ToggleButton) findViewById(R.id.leaButton);
				status = daoStatus.getByName("lea");
				status.setStatus(state);
				daoStatus.updateStatus(status);
				tButton.setChecked(bState);

				if (bState) {
					controller.activateAll();
				} else {
					controller.deactivateAll();
				}

				return;
			}

			ToggleButton tButton = (ToggleButton) findViewById(v.getId());
			if (tButton.isChecked()) {
				status.setStatus(1);
				controller.activateScreenLock(status.getName());
			} else {
				status.setStatus(0);
				controller.deactivateScreenLock(status.getName());
			}

			daoStatus.updateStatus(status);
		}
	};
}
