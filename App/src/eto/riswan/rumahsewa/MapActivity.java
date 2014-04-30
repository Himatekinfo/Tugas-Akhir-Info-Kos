package eto.riswan.rumahsewa;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import eto.riswan.rumahsewa.core.OrmLiteBaseFragmentActivity;
import eto.riswan.rumahsewa.helper.GeoLocation;
import eto.riswan.rumahsewa.helper.Global;
import eto.riswan.rumahsewa.model.RumahSewa;

public class MapActivity extends OrmLiteBaseFragmentActivity {
	GoogleMap map;

	private RuntimeExceptionDao<RumahSewa, Long> rumahSewaDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.showMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.idMap.action_list:
				Intent intent = new Intent(this, ListPointActivity.class);
				intent.putExtras(MapActivity.this.getIntent());
				this.startActivity(intent);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}

	private void showMap() {
		this.setContentView(R.layout.map_screen);

		this.map = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		if (this.map == null)
			Toast.makeText(this, "Google Maps not available", Toast.LENGTH_LONG).show();
		else {
			this.map.setMyLocationEnabled(true);
			this.map.getMyLocation();
			LatLng myLatLng = Global.getCenterLocation();

			CameraPosition myPosition = new CameraPosition.Builder().target(myLatLng).zoom(12).build();
			this.map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

			// Get points
			this.rumahSewaDao = this.getHelper().getRumahSewaRuntime();

			List<RumahSewa> rumahSewas = new ArrayList<RumahSewa>();
			Bundle extras = this.getIntent().getExtras();
			if ((extras != null) && extras.containsKey("rent"))
				try {
					ArrayList<String> facilities = extras.getStringArrayList("facilities");

					String[] rent = extras.getString("rent").split("-");

					QueryBuilder<RumahSewa, Long> qb = this.getHelper().getRumahSewa().queryBuilder();
					Where<RumahSewa, Long> qbWhere = qb.where();
					if (facilities.size() > 1) {
						for (String facility : facilities)
							qbWhere.like("facilities", facility);
						qbWhere.or(facilities.size()); // wrap previous like statements in or
					} else if (facilities.size() == 1) qbWhere.like("facilities", facilities.get(0));
					qbWhere.ge("rent", Long.valueOf(rent[0])).and().le("rent", Long.valueOf(rent[1]));
					if (!facilities.isEmpty()) qbWhere.and(2);

					rumahSewas = this.rumahSewaDao.query(qbWhere.prepare());

				} catch (java.sql.SQLException e) {
					// TODO: Exception Handling
					e.printStackTrace();
				}
			else
				rumahSewas = this.rumahSewaDao.queryForAll();

			this.map.addMarker(new MarkerOptions()
					.position(
							new LatLng(GeoLocation.getCurrentLocation(this).getLatitude(), GeoLocation
									.getCurrentLocation(this).getLongitude())).title("Current Location")
					.draggable(false));

			if (rumahSewas.size() > 0) {
				int count = 0;
				for (RumahSewa point : rumahSewas)
					if (point.getDistanceFromLocation(this) < Global.DistanceRange) {
						this.map.addMarker(new MarkerOptions()
								.position(new LatLng(point.latitude, point.longitude))
								.title(point.ownersName).draggable(false).snippet(point.id.toString())
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
						count++;
					}
				Toast.makeText(this, "Found " + String.valueOf(count) + " point(s).", Toast.LENGTH_LONG)
						.show();
			} else
				Toast.makeText(this, "No points found.", Toast.LENGTH_LONG).show();

			this.map.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoContents(Marker arg0) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public View getInfoWindow(Marker arg0) {
					View v = null;

					try {
						RumahSewa r = MapActivity.this.rumahSewaDao.queryForId(Long.parseLong(arg0
								.getSnippet()));

						v = MapActivity.this.getLayoutInflater().inflate(R.layout.detail_screen, null);

						TextView txtOwnerName = (TextView) v.findViewById(R.id.txtOwnerName);
						txtOwnerName.setText(r.ownersName);

						TextView txtRent = (TextView) v.findViewById(R.id.txtRent);
						txtRent.setText(r.rent.toString());

						TextView txtPhoneNumber = (TextView) v.findViewById(R.id.txtPhoneNumber);
						txtPhoneNumber.setText(r.phoneNumber);

						TextView txtFacilities = (TextView) v.findViewById(R.id.txtFacilities);
						txtFacilities.setText(r.facilities);

						TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);
						txtDescription.setText(r.description);

						TextView txtDistance = (TextView) v.findViewById(R.id.txtDistance);
						txtDistance.setText(String.valueOf(r.getDistanceFromLocation(MapActivity.this))
								+ " km");
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					return v;
				}
			});

			this.map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker arg0) {
					try {
						Intent x = new Intent(MapActivity.this, DetailPointActivity.class);
						x.putExtra("Id", (Long.parseLong(arg0.getSnippet())));
						MapActivity.this.startActivity(x);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}
	}
}
