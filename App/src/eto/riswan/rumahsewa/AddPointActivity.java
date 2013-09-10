package eto.riswan.rumahsewa;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import eto.riswan.rumahsewa.helper.Database;
import eto.riswan.rumahsewa.helper.GeoLocation;
import eto.riswan.rumahsewa.model.RumahSewa;

public class AddPointActivity extends OrmLiteBaseActivity<Database> {
	private static int RESULT_LOAD_IMAGE = 1;

	public EditText txtOwnersName;
	public EditText txtAddress;
	public EditText txtPhoneNumber;
	public EditText txtRent;
	public EditText txtFacilities;
	public EditText txtDescription;
	public String imageLocation;

	protected void bindControls() {
		this.txtOwnersName = (EditText) this.findViewById(R.id.txtOwnersName);
		this.txtAddress = (EditText) this.findViewById(R.id.txtAddress);
		this.txtPhoneNumber = (EditText) this.findViewById(R.id.txtPhoneNumber);
		this.txtRent = (EditText) this.findViewById(R.id.txtRent);
		this.txtFacilities = (EditText) this.findViewById(R.id.txtFacilities);
		this.txtDescription = (EditText) this.findViewById(R.id.txtDescription);
	}

	public void btnLoadPicture_onClick(View v) {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		this.startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	public void btnSavePoint_onClick(View v) {
		this.bindControls();

		RumahSewa rumahSewa = new RumahSewa();
		rumahSewa.Address = this.txtAddress.getText().toString();
		rumahSewa.Description = this.txtDescription.getText().toString();
		rumahSewa.Facilities = this.txtFacilities.getText().toString();
		rumahSewa.OwnersName = this.txtOwnersName.getText().toString();
		rumahSewa.PhoneNumber = this.txtPhoneNumber.getText().toString();
		rumahSewa.Rent = Long.valueOf(this.txtRent.getText().toString());
		rumahSewa.Latitude = GeoLocation.getCurrentLocation(this).getLatitude();
		rumahSewa.Longitude = GeoLocation.getCurrentLocation(this).getLongitude();
		rumahSewa.PicturePath = this.imageLocation;

		RuntimeExceptionDao<RumahSewa, Long> rumahSewaDao;
		rumahSewaDao = this.getHelper().getRumahSewaRuntime();
		rumahSewaDao.create(rumahSewa);

		Toast.makeText(this, "Data saved successfully.", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if ((requestCode == RESULT_LOAD_IMAGE) && (resultCode == RESULT_OK) && (null != data)) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			this.imageLocation = cursor.getString(columnIndex);
			cursor.close();

			ImageView imageView = (ImageView) this.findViewById(R.id.imgViewPicture);
			imageView.setImageBitmap(BitmapFactory.decodeFile(this.imageLocation));

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.add_screen);
	}
}
