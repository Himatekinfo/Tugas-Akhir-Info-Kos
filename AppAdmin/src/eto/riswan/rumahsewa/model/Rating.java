package eto.riswan.rumahsewa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Rating {
	@DatabaseField
	public Long Id;

	@DatabaseField
	public String IdRating;

	@DatabaseField
	public Short Rate;

	@DatabaseField
	public String IdRumahSewa;
}
