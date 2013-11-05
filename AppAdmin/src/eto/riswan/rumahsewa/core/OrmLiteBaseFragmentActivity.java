package eto.riswan.rumahsewa.core;

import android.support.v4.app.FragmentActivity;
import eto.riswan.rumahsewa.helper.Database;

public class OrmLiteBaseFragmentActivity extends FragmentActivity {
	private Database databaseHelper = null;

	protected Database getHelper() {
		if (this.databaseHelper == null)
			this.databaseHelper = Database.getInstance(this.getApplicationContext());
		return this.databaseHelper;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (this.databaseHelper != null) this.databaseHelper = null;
	}
}
