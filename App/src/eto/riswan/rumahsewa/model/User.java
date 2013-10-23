package eto.riswan.rumahsewa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class User {

	@DatabaseField(generatedId = true)
	public Long Id;

	@DatabaseField(width = 50)
	public String IdUser;

	@DatabaseField(width = 50, uniqueCombo = true)
	public String UserName;

	@DatabaseField(width = 50, uniqueCombo = true)
	public String FullName;

	@DatabaseField(width = 100)
	public String Password;

	@DatabaseField(width = 500)
	public String Address;

	@DatabaseField(width = 500)
	public String PhotoPicture;
}
