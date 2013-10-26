package eto.riswan.rumahsewa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends MainActivity {
	public void copyright_onClick(View v) {
		Intent x = new Intent(AboutActivity.this, LoginActivity.class);
		this.startActivity(x);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.about_screen);
	}

}
