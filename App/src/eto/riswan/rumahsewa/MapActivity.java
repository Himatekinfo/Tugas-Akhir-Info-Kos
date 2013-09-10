package eto.riswan.rumahsewa;

import java.util.List;

import android.os.Bundle;
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
import eto.riswan.rumahsewa.helper.GeoLocation;
import eto.riswan.rumahsewa.model.RumahSewa;

public class MapActivity extends OrmLiteBaseFragmentActivity {
	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.map_screen);

		this.map = ((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		if (this.map == null)
			Toast.makeText(this, "Google Maps not available", Toast.LENGTH_LONG).show();
		else {
			this.map.setMyLocationEnabled(true);
			this.map.getMyLocation();
			LatLng myLatLng = new LatLng(GeoLocation.getCurrentLocation(this).getLatitude(), GeoLocation
					.getCurrentLocation(this).getLongitude());

			CameraPosition myPosition = new CameraPosition.Builder().target(myLatLng).zoom(12).build();
			this.map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

			// Get points
			RuntimeExceptionDao<RumahSewa, Long> rumahSewaDao;
			rumahSewaDao = this.getHelper().getRumahSewaRuntime();
			List<RumahSewa> accounts = rumahSewaDao.queryForAll();
			if (accounts.size() > 0) {
				for (RumahSewa point : accounts)
					this.map.addMarker(new MarkerOptions()
							.position(new LatLng(point.Latitude, point.Longitude)).title(point.OwnersName)
							.draggable(false).snippet(point.Id + ":" + point.Facilities)
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
					View v = MapActivity.this.getLayoutInflater().inflate(R.layout.detail_screen, null);
					TextView txtTest = (TextView) v.findViewById(R.idMap.txtDetailMapTitle);
					txtTest.setText(arg0.getSnippet());
					return v;
				}
			});
		}
	}
}
