package eto.riswan.rumahsewa;

import java.util.ArrayList;

import org.apache.http.HttpResponse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import eto.riswan.rumahsewa.core.Parameter;
import eto.riswan.rumahsewa.helper.Database;
import eto.riswan.rumahsewa.helper.Global;
import eto.riswan.rumahsewa.helper.Service;

public class LoginActivity extends OrmLiteBaseActivity<Database> {
	protected static String url = Global.BaseUrl + "/user/login/";
	private EditText txtUsername;
	private EditText txtPassword;
	private ProgressDialog progressBar;
	private ArrayList<Parameter> lParemeters;

	public void btnLogin_click(View v) {
		this.lParemeters = new ArrayList<Parameter>();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					HttpResponse response = Service.makeRequest(LoginActivity.url + "?user="
							+ LoginActivity.this.txtUsername.getText().toString() + "&pass="
							+ LoginActivity.this.txtPassword.getText().toString(),
							LoginActivity.this.lParemeters);

					final String s = Service.inputStreamToString(response.getEntity().getContent())
							.toString();

					LoginActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Intent x = new Intent(LoginActivity.this, ListPointActivity.class);
							x.putExtra("asAdmin", "1");

							LoginActivity.this.finish();

							if (s.contains(":200,")) {
								Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_LONG)
										.show();
								LoginActivity.this.startActivity(x);
							} else
								Toast.makeText(LoginActivity.this,
										"Invalid username or password. Detail: " + s, Toast.LENGTH_LONG)
										.show();
							LoginActivity.this.progressBar.dismiss();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();

					LoginActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_LONG).show();
							LoginActivity.this.progressBar.dismiss();
						}
					});
				}
			}
		};
		new Thread(runnable).start();

		this.progressBar = new ProgressDialog(this);
		this.progressBar.setCancelable(false);
		this.progressBar.setMessage("Logging in...");
		this.progressBar.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.login_admin);

		this.txtUsername = (EditText) this.findViewById(R.id.txtUsername);
		this.txtPassword = (EditText) this.findViewById(R.id.txtPassword);
	}

}
