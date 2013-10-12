package eto.riswan.rumahsewa;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import eto.riswan.rumahsewa.helper.Database;
import eto.riswan.rumahsewa.helper.Global;
import eto.riswan.rumahsewa.helper.Service;
import eto.riswan.rumahsewa.model.RumahSewa;
import eto.riswan.rumahsewa.model.ServiceResponse;

public class ListPointActivity extends OrmLiteBaseActivity<Database> {
	public static final String url = Global.BaseUrl + "/rumahSewa/";

	Boolean keepAlive = true;

	private Thread downloadThread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setView();
		this.startProgress();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		this.keepAlive = false;
		if (this.downloadThread != null) {
			this.downloadThread.interrupt();
			this.downloadThread = null;
		}
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.idMap.action_map:
				Intent intent = new Intent(this, MapActivity.class);
				this.startActivity(intent);
				this.keepAlive = false;
				this.finish();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}

	private void setView() {
		ListView lv = new ListView(this);

		ArrayList<String> al = new ArrayList<String>();
		Bundle extras = this.getIntent().getExtras();

		RuntimeExceptionDao<RumahSewa, Long> rumahSewaDaoRuntime = this.getHelper().getRumahSewaRuntime();
		List<RumahSewa> rumahSewas = new ArrayList<RumahSewa>();

		if (extras != null)
			try {
				ArrayList<String> facilities = extras.getStringArrayList("facilities");

				String[] rent = extras.getString("rent").split("-");

				QueryBuilder<RumahSewa, Long> qb = this.getHelper().getRumahSewa().queryBuilder();
				Where<RumahSewa, Long> qbWhere = qb.where();
				if (facilities.size() > 1) {
					for (String facility : facilities)
						qbWhere.like("Facilities", facility);
					qbWhere.or(facilities.size()); // wrap previous like statements in or
				} else if (facilities.size() == 1) qbWhere.like("Facilities", facilities.get(0));
				qbWhere.ge("Rent", Long.valueOf(rent[0])).and().le("Rent", Long.valueOf(rent[1]));
				if (!facilities.isEmpty()) qbWhere.and(2);

				rumahSewas = rumahSewaDaoRuntime.query(qbWhere.prepare());

			} catch (java.sql.SQLException e) {
				// TODO: Exception Handling
				e.printStackTrace();
			}
		else
			rumahSewas = rumahSewaDaoRuntime.queryForAll();

		// if (rumahSewas.size() > 0) for (RumahSewa point : rumahSewas)
		// al.add(point.ownersName);
		//
		Collections.sort(al);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(new ArrayAdapter<RumahSewa>(this.getApplicationContext(), R.layout.point_list_item,
				rumahSewas) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View row = convertView;

				LayoutInflater inflater = ((Activity) ListPointActivity.this).getLayoutInflater();
				row = inflater.inflate(R.layout.point_list_item, parent, false);

				RumahSewa r = this.getItem(position);

				TextView txtOwnersName = (TextView) row.findViewById(R.list.txtOwnersName);
				txtOwnersName.setText(r.ownersName);

				TextView txtPicture = (TextView) row.findViewById(R.list.txtPicture);
				txtPicture.setText(r.picturePath);

				TextView txtPrice = (TextView) row.findViewById(R.list.txtRentPrice);
				txtPrice.setText(r.rent.toString());

				TextView txtDistanceFromPosition = (TextView) row
						.findViewById(R.list.txtDistanceFromPosition);
				txtDistanceFromPosition.setText(String.valueOf(r
						.getDistanceFromLocation(ListPointActivity.this)));

				TextView txtDistanceFromCenter = (TextView) row.findViewById(R.list.txtDistanceFromCenter);
				txtDistanceFromCenter.setText(String.valueOf(r.getDistanceFromCenter(ListPointActivity.this)));

				return row;
			}
		});

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		ll.setGravity(Gravity.CENTER);
		if (rumahSewas.size() <= 0) Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
		ll.addView(lv);
		this.setContentView(ll);
	}

	public void showToast(String message) {
		Toast.makeText(ListPointActivity.this, message, Toast.LENGTH_LONG).show();
	}

	public void startProgress() {
		this.keepAlive = true;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				ServiceResponse response;

				while (ListPointActivity.this.keepAlive) {
					InputStream source = Service.retrieveStream(ListPointActivity.url);
					Gson gson = new Gson();
					Reader reader = new InputStreamReader(source);

					response = gson.fromJson(reader, ServiceResponse.class);
					List<RumahSewa> results = response.data;

					for (RumahSewa result : results)
						try {
							Dao<RumahSewa, Long> rumahSewa = Database.getInstance(
									ListPointActivity.this.getApplicationContext()).getRumahSewa();

							result.isSynchronized = true;
							if (!rumahSewa.idExists(result.getLocalFromGlobal(ListPointActivity.this)))
								rumahSewa.createIfNotExists(result);

						} catch (SQLException e) {
							e.printStackTrace();
						}

					if (ListPointActivity.this.keepAlive)
						ListPointActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ListPointActivity.this.setView();
							}
						});

					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		this.downloadThread = new Thread(runnable);
		this.downloadThread.start();
	}
}
