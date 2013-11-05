package eto.riswan.rumahsewa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	public void action_settings_click(MenuItem m) {
		Intent x = new Intent(MainActivity.this, LoginActivity.class);
		this.startActivity(x);
	}

	public void btnAbout_onClick(View v) {
		Intent x = new Intent(MainActivity.this, AboutActivity.class);
		this.startActivity(x);
	}

	public void btnHelp_onClick(View v) {
		Intent x = new Intent(MainActivity.this, HelpActivity.class);
		this.startActivity(x);
	}

	public void btnInfoRumahSewa_OnClick(View v) {
		Intent x = new Intent(MainActivity.this, MapActivity.class);
		this.startActivity(x);
	}

	public void btnPencarian_onClick(View v) {
		Intent x = new Intent(MainActivity.this, SearchActivity.class);
		this.startActivity(x);
	}

	public void btnSekilasUika(View v) {
		Intent x = new Intent(MainActivity.this, SekilasUika.class);
		this.startActivity(x);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
