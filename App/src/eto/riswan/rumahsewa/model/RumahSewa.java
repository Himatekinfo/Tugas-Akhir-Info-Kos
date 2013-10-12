package eto.riswan.rumahsewa.model;

import java.util.Calendar;

import android.app.Activity;
import android.location.Location;
import android.provider.Settings.Secure;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import eto.riswan.rumahsewa.helper.GeoLocation;
import eto.riswan.rumahsewa.helper.Global;

@DatabaseTable
public class RumahSewa {
	@DatabaseField(generatedId = true)
	public Long id;

	@DatabaseField
	public Double latitude;

	@DatabaseField
	public Double longitude;

	@SerializedName("namapemilik")
	@DatabaseField(width = 50)
	public String ownersName;

	@SerializedName("alamat")
	@DatabaseField(width = 500)
	public String address;

	@SerializedName("no_telp")
	@DatabaseField(width = 15)
	public String phoneNumber;

	@SerializedName("hargasewa")
	@DatabaseField
	public Long rent;

	@SerializedName("foto")
	@DatabaseField(width = 500)
	public String picturePath;

	@SerializedName("fasilitas")
	@DatabaseField(width = 500)
	public String facilities;

	@SerializedName("deskriprsi")
	@DatabaseField(width = 500)
	public String description;

	@SerializedName("created_date")
	@DatabaseField
	public String createdDate;

	@DatabaseField
	public Boolean isSynchronized;

	@SerializedName("id_rumahSewa")
	@DatabaseField
	public String idRumahSewa;

	public RumahSewa() {
		this.isSynchronized = false;
		String date = String.valueOf(Calendar.getInstance().getTimeInMillis());
		this.createdDate = date.substring(date.length() - 7, date.length() - 1);
	}

	public float getDistanceFromCenter(Activity a) {
		try {
			float[] results = new float[1];
			Location.distanceBetween(Global.Latitude, Global.Longitude, this.latitude, this.longitude,
					results);

			if (results != null) return results[0];
		} catch (Exception e) {
			Log.e("RumahSewa", e.getMessage());
		}

		return 0;
	}

	public float getDistanceFromLocation(Activity a) {
		try {
			float[] results = new float[1];
			Location.distanceBetween(GeoLocation.getCurrentLocation(a).getLatitude(), GeoLocation
					.getCurrentLocation(a).getLongitude(), this.latitude, this.longitude, results);

			if (results != null) return results[0];
		} catch (Exception e) {
			Log.e("RumahSewa", e.getMessage());
		}

		return 0;
	}

	public String getGlobalId(Activity a) {
		if ((this.idRumahSewa != null) && (this.idRumahSewa.length() > 0)) return this.idRumahSewa;
		return Secure.getString(a.getContentResolver(), Secure.ANDROID_ID) + this.id.toString()
				+ this.createdDate;
	}

	public Long getLocalFromGlobal(Activity a) {
		String android_id = Secure.getString(a.getContentResolver(), Secure.ANDROID_ID);
		String s = this.getGlobalId(a).substring(android_id.length());

		return Long.valueOf(s.substring(0, s.length() - this.createdDate.length()));
	}
}
