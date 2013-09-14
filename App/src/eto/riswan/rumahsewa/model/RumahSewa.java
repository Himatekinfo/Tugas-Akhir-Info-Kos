package eto.riswan.rumahsewa.model;

import java.util.Calendar;
import java.util.Date;

import android.provider.Settings;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RumahSewa {
	@DatabaseField(generatedId = true)
	public Long Id;

	@DatabaseField
	public Double Latitude;

	@DatabaseField
	public Double Longitude;

	@DatabaseField(width = 50)
	public String OwnersName;

	@DatabaseField(width = 500)
	public String Address;

	@DatabaseField(width = 15)
	public String PhoneNumber;

	@DatabaseField
	public Long Rent;

	@DatabaseField(width = 500)
	public String PicturePath;

	@DatabaseField(width = 500)
	public String Facilities;

	@DatabaseField(width = 500)
	public String Description;

	@DatabaseField
	private final Date CreatedDate;

	@DatabaseField
	private final Boolean IsShyncrhonized;

	public RumahSewa() {
		this.IsShyncrhonized = false;
		this.CreatedDate = Calendar.getInstance().getTime();
	}

	public String getGlobalId() {
		return Settings.Secure.ANDROID_ID + this.Id.toString() + String.valueOf(this.CreatedDate.getTime());
	}
}
