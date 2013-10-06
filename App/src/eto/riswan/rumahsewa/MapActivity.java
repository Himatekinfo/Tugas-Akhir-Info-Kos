package eto.riswan.rumahsewa;

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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import eto.riswan.rumahsewa.core.OrmLiteBaseFragmentActivity;
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
			List<RumahSewa> accounts = this.rumahSewaDao.queryForAll();
			if (accounts.size() > 0) {
				for (RumahSewa point : accounts)
					this.map.addMarker(new MarkerOptions()
							.position(new LatLng(point.latitude, point.longitude)).title(point.ownersName)
							.draggable(false).snippet(point.id.toString())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
				Toast.makeText(this, "Found " + String.valueOf(accounts.size()) + " point(s).",
						Toast.LENGTH_LONG).show();
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
					RumahSewa r = MapActivity.this.rumahSewaDao.queryForId(Long.parseLong(arg0.getSnippet()));

					View v = MapActivity.this.getLayoutInflater().inflate(R.layout.detail_screen, null);

					TextView txtOwnerName = (TextView) v.findViewById(R.idDetail.txtOwnerName);
					txtOwnerName.setText(r.ownersName);

					TextView txtRent = (TextView) v.findViewById(R.idDetail.txtRent);
					txtRent.setText(r.rent.toString());

					TextView txtPhoneNumber = (TextView) v.findViewById(R.idDetail.txtPhoneNumber);
					txtPhoneNumber.setText(r.phoneNumber);

					TextView txtFacilities = (TextView) v.findViewById(R.idDetail.txtFacilities);
					txtFacilities.setText(r.facilities);

					TextView txtDescription = (TextView) v.findViewById(R.idDetail.txtDescription);
					txtDescription.setText(r.description);

					return v;
				}
			});
		}
	}
}
