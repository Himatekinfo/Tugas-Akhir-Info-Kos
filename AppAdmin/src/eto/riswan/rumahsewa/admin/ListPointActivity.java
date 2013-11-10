package eto.riswan.rumahsewa.admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

import eto.riswan.rumahsewa.core.Parameter;
import eto.riswan.rumahsewa.helper.Database;
import eto.riswan.rumahsewa.helper.Global;
import eto.riswan.rumahsewa.helper.Service;
import eto.riswan.rumahsewa.model.RumahSewa;
import eto.riswan.rumahsewa.model.ServiceResponse;

public class ListPointActivity extends OrmLiteBaseActivity<Database> {
	public static final String url = Global.BaseUrl + "/rumahSewa/";
	public boolean asAdmin = false;

	Boolean keepAlive = true;

	private Thread downloadThread = null;
	private ArrayList<Parameter> lParemeters;
	private ProgressDialog progressBar;

	private void delete(final String globalId, final long id) {
		this.lParemeters = new ArrayList<Parameter>();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					String url = ListPointActivity.url + "delete/?id=" + globalId;

					Log.i("Deleting data...", "Url:" + url);

					HttpResponse response = Service.makeRequest(url, ListPointActivity.this.lParemeters);

					final String s = Service.inputStreamToString(response.getEntity().getContent())
							.toString();

					ListPointActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Intent x = new Intent(ListPointActivity.this, ListPointActivity.class);
							if (ListPointActivity.this.asAdmin) x.putExtra("asAdmin", "1");

							ListPointActivity.this.finish();
							ListPointActivity.this.startActivity(x);

							if (s.contains(":200")) {
								try {
									ListPointActivity.this.getHelper().getRumahSewa().deleteById(id);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Toast.makeText(ListPointActivity.this, "Delete successfully.",
										Toast.LENGTH_LONG).show();
							} else
								Toast.makeText(ListPointActivity.this, "Failed to delete data.",
										Toast.LENGTH_LONG).show();
							ListPointActivity.this.progressBar.dismiss();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();

					ListPointActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ListPointActivity.this, "Delete failed.", Toast.LENGTH_LONG)
									.show();
							ListPointActivity.this.progressBar.dismiss();
						}
					});
				}
			}
		};
		new Thread(runnable).start();

		this.progressBar = new ProgressDialog(this);
		this.progressBar.setCancelable(false);
		this.progressBar.setMessage("Deleting data...");
		this.progressBar.show();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		RumahSewa r = new RumahSewa();
		try {
			r = this.getHelper().getRumahSewa().queryForId((long) item.getItemId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (item.getTitle() == "Update") {
			Intent x = new Intent(ListPointActivity.this, AddPointActivity.class);
			x.putExtra("asUpdate", "1");
			x.putExtra("Id", r.id);

			ListPointActivity.this.finish();
			ListPointActivity.this.startActivity(x);
		} else if (item.getTitle() == "Delete")
			this.delete(r.getGlobalId(this), r.id);
		else
			return false;
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setView();
		this.startProgress();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		RumahSewa r = (RumahSewa) v.getTag();
		menu.setHeaderTitle("Admin Action");
		menu.add(0, r.id.intValue(), 0, "Update");
		menu.add(0, r.id.intValue(), 0, "Delete");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.maps, menu);
		if (this.asAdmin) menu.add("Add");
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
		if (item.getTitle() == "Add") {
			Intent x = new Intent(this, AddPointActivity.class);
			this.startActivity(x);
		} else
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

		Bundle extras = this.getIntent().getExtras();
		if ((extras != null) && extras.containsKey("asAdmin")) this.asAdmin = true;

		RuntimeExceptionDao<RumahSewa, Long> rumahSewaDaoRuntime = this.getHelper().getRumahSewaRuntime();
		List<RumahSewa> rumahSewas = new ArrayList<RumahSewa>();
		List<RumahSewa> rumahSewasInRange = new ArrayList<RumahSewa>();

		if ((extras != null) && extras.containsKey("rent"))
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

		if (rumahSewas.size() > 0) for (RumahSewa point : rumahSewas)
			// if distance is less than 1000 m, include it in the list
			// if (point.getDistanceFromLocation(this) < 1000)
			rumahSewasInRange.add(point);

		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(new ArrayAdapter<RumahSewa>(this.getApplicationContext(), R.layout.item_list_rumahsewa,
				rumahSewasInRange) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = ((Activity) ListPointActivity.this).getLayoutInflater();
				final View row = inflater.inflate(R.layout.item_list_rumahsewa, parent, false);

				final RumahSewa r = this.getItem(position);
				row.setTag(r);

				TextView txtOwnersName = (TextView) row.findViewById(R.list.txtOwnersName);
				txtOwnersName.setText(r.ownersName);

				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							URL url;
							url = new URL(Global.BaseUrl.replace("index.php", "") + r.picturePath);
							final Bitmap bmp = Service.loadBitmapFromUri(ListPointActivity.this, url);
							Log.i("Loading image...", url.toString());
							ListPointActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ImageView imageLocation = (ImageView) row
											.findViewById(R.list.imagePicture);
									imageLocation.setImageBitmap(bmp);
								}
							});
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				thread.start();

				TextView txtAddress = (TextView) row.findViewById(R.list.txtAddress);
				txtAddress.setText(r.address == null ? "" : r.address);

				TextView txtPhone = (TextView) row.findViewById(R.list.txtPhone);
				txtPhone.setText(r.phoneNumber);

				TextView txtSewa = (TextView) row.findViewById(R.list.txtSewa);
				txtSewa.setText(r.rent.toString());

				TextView txtDistance = (TextView) row.findViewById(R.list.txtDistanceFromLocation);
				txtDistance.setText(String.valueOf(r.getDistanceFromLocation(ListPointActivity.this)));

				// TextView txtPrice = (TextView) row.findViewById(R.list.txtRentPrice);
				// txtPrice.setText(r.rent.toString());

				// TextView txtDistanceFromPosition = (TextView) row
				// .findViewById(R.list.txtDistanceFromPosition);
				// txtDistanceFromPosition.setText(String.valueOf(r
				// .getDistanceFromLocation(ListPointActivity.this)));
				//
				// TextView txtDistanceFromCenter = (TextView) row.findViewById(R.list.txtDistanceFromCenter);
				// txtDistanceFromCenter.setText(String.valueOf(r.getDistanceFromCenter(ListPointActivity.this)));

				if (ListPointActivity.this.asAdmin) ListPointActivity.this.registerForContextMenu(row);

				return row;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent x = new Intent(ListPointActivity.this, DetailPointActivity.class);
				x.putExtra("Id", ((RumahSewa) arg1.getTag()).id);
				ListPointActivity.this.startActivity(x);
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
					Log.i("Service", "Starting loop...");
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
						Log.i("Service", "Resting...");
						Thread.sleep(6000000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		this.downloadThread = new Thread(runnable);
		Log.i("Service", "Getting data...");
		this.downloadThread.start();
	}

	public void update(RumahSewa r) {

	}
}
