package eto.riswan.rumahsewa.helper;

import com.google.android.gms.maps.model.LatLng;

public class Global {
	public static final double Latitude = -6.560857;
	public static final double Longitude = 106.792172;

	public static LatLng getCenterLocation() {
		return new LatLng(Global.Latitude, Global.Longitude);
	}
}
