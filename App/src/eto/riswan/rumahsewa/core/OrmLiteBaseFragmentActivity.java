package eto.riswan.rumahsewa.core;

import android.support.v4.app.FragmentActivity;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import eto.riswan.rumahsewa.helper.Database;

public class OrmLiteBaseFragmentActivity extends FragmentActivity {
	private Database databaseHelper = null;

	protected Database getHelper() {
		if (this.databaseHelper == null)
			this.databaseHelper = OpenHelperManager.getHelper(this.getApplicationContext(), Database.class);
		return this.databaseHelper;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (this.databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			this.databaseHelper = null;
		}
	}
}
