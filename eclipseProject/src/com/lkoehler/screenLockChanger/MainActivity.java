package com.lkoehler.screenLockChanger;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

import com.lkoehler.screenLockController.Controller;

public class MainActivity extends Activity {

	private Controller controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		controller = new Controller(this);

		ToggleButton allButton = (ToggleButton) findViewById(R.id.allButton);
		allButton.setOnClickListener(listener);

		ToggleButton tButton;
		tButton = (ToggleButton) findViewById(R.id.simonButton);
		tButton.setOnClickListener(listener);
		boolean setAllButton = true;

		if (controller.getStatus("simon")) {
			tButton.setChecked(true);
		} else {
			setAllButton = false;
		}

		tButton = (ToggleButton) findViewById(R.id.saschaButton);
		tButton.setOnClickListener(listener);
		if (controller.getStatus("sascha")) {
			tButton.setChecked(true);
		} else {
			setAllButton = false;
		}

		tButton = (ToggleButton) findViewById(R.id.leaButton);
		tButton.setOnClickListener(listener);
		if (controller.getStatus("lea")) {
			tButton.setChecked(true);
		} else {
			setAllButton = false;
		}

		if (setAllButton)
			allButton.setChecked(true);
	}

	public void updateAllButton() {
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

	public void setAllButtons(boolean state) {
		ToggleButton tButton;
		tButton = (ToggleButton) findViewById(R.id.simonButton);
		tButton.setChecked(state);

		tButton = (ToggleButton) findViewById(R.id.saschaButton);
		tButton.setChecked(state);

		tButton = (ToggleButton) findViewById(R.id.leaButton);
		tButton.setChecked(state);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ToggleButton tButton = (ToggleButton) findViewById(v.getId());
			if (tButton.getId() == R.id.allButton) {
				controller.updateAll(tButton.isChecked());
				setAllButtons(tButton.isChecked());
			} else {
				String name = "";
				if (tButton.getId() == R.id.simonButton) {
					name = "simon";
				} else if (tButton.getId() == R.id.saschaButton) {
					name = "sascha";
				} else if (tButton.getId() == R.id.leaButton) {
					name = "lea";
				}
				controller.updateScreenLock(name, tButton.isChecked());
				updateAllButton();
			}

			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(v.getContext(), WidgetProvider.class));
		    if (appWidgetIds.length > 0) {
		        new WidgetProvider().onUpdate(v.getContext(), appWidgetManager, appWidgetIds);
		    }
		}
	};
}
