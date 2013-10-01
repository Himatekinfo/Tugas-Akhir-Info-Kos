package eto.riswan.rumahsewa;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import eto.riswan.rumahsewa.core.Parameter;
import eto.riswan.rumahsewa.core.Parameter.ParameterType;
import eto.riswan.rumahsewa.helper.Database;
import eto.riswan.rumahsewa.helper.GeoLocation;
import eto.riswan.rumahsewa.helper.Global;
import eto.riswan.rumahsewa.helper.Service;
import eto.riswan.rumahsewa.model.RumahSewa;

public class AddPointActivity extends OrmLiteBaseActivity<Database> {
	private static final int CAMERA_REQUEST = 31337;

	private static final int RESULT_LOAD_IMAGE = 31338;

	public static final String url = Global.BaseUrl + "/rumahSewa/create/";

	public EditText txtOwnersName;
	public EditText txtAddress;
	public EditText txtPhoneNumber;
	public EditText txtRent;
	public EditText txtFacilities;
	public EditText txtDescription;
	public Button btnLoadPicture;
	public String imageLocation;

	private List<Parameter> lParemeters;

	protected void bindControls() {
		this.txtOwnersName = (EditText) this.findViewById(R.id.txtOwnersName);
		this.txtAddress = (EditText) this.findViewById(R.id.txtAddress);
		this.txtPhoneNumber = (EditText) this.findViewById(R.id.txtPhoneNumber);
		this.txtRent = (EditText) this.findViewById(R.id.txtRent);
		this.txtFacilities = (EditText) this.findViewById(R.id.txtFacilities);
		this.txtDescription = (EditText) this.findViewById(R.id.txtDescription);
		this.btnLoadPicture = (Button) this.findViewById(R.id.btnLoadPicture);

		this.registerForContextMenu(this.btnLoadPicture);
	}

	public void btnLoadPicture_onClick(View v) {
		this.openContextMenu(this.btnLoadPicture);
	}

	public void btnSavePoint_onClick(View v) {
		RumahSewa rumahSewa = new RumahSewa();
		rumahSewa.address = this.txtAddress.getText().toString();
		rumahSewa.description = this.txtDescription.getText().toString();
		rumahSewa.facilities = this.txtFacilities.getText().toString();
		rumahSewa.ownersName = this.txtOwnersName.getText().toString();
		rumahSewa.phoneNumber = this.txtPhoneNumber.getText().toString();
		rumahSewa.rent = Long.valueOf(this.txtRent.getText().toString());
		rumahSewa.latitude = GeoLocation.getCurrentLocation(this).getLatitude();
		rumahSewa.longitude = GeoLocation.getCurrentLocation(this).getLongitude();
		rumahSewa.picturePath = this.imageLocation;

		RuntimeExceptionDao<RumahSewa, Long> rumahSewaDao;
		rumahSewaDao = this.getHelper().getRumahSewaRuntime();
		rumahSewaDao.create(rumahSewa);

		this.lParemeters = new ArrayList<Parameter>();
		this.lParemeters.add(new Parameter("id_rumahSewa", rumahSewa.getGlobalId(this)));
		this.lParemeters.add(new Parameter("latitude", rumahSewa.latitude.toString()));
		this.lParemeters.add(new Parameter("longitude", rumahSewa.longitude.toString()));
		this.lParemeters.add(new Parameter("namapemilik", rumahSewa.ownersName.toString()));
		this.lParemeters.add(new Parameter("alamat", rumahSewa.address.toString()));
		this.lParemeters.add(new Parameter("no_telp", rumahSewa.phoneNumber.toString()));
		this.lParemeters.add(new Parameter("hargasewa", rumahSewa.rent.toString()));
		this.lParemeters.add(new Parameter("foto", this.imageLocation, ParameterType.BINARY));
		this.lParemeters.add(new Parameter("fasilitas", rumahSewa.facilities.toString()));
		this.lParemeters.add(new Parameter("deskripsi", rumahSewa.description.toString()));
		this.lParemeters.add(new Parameter("created_date", rumahSewa.createdDate.toString()));

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					@SuppressWarnings("unused")
					HttpResponse response = Service.makeRequest(AddPointActivity.url,
							AddPointActivity.this.lParemeters);

					@SuppressWarnings("unused")
					String s = Service.inputStreamToString(response.getEntity().getContent()).toString();

					AddPointActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AddPointActivity.this, "Upload successful.", Toast.LENGTH_LONG)
									.show();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();

					AddPointActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AddPointActivity.this, "Upload failed.", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		};
		new Thread(runnable).start();

		Toast.makeText(this, "Data saved successfully.", Toast.LENGTH_LONG).show();

		Intent x = new Intent(AddPointActivity.this, MapActivity.class);
		this.finish();
		this.startActivity(x);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ImageView imageView = (ImageView) this.findViewById(R.id.imgViewPicture);

		if ((requestCode == RESULT_LOAD_IMAGE) && (resultCode == RESULT_OK) && (null != data)) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			this.imageLocation = cursor.getString(columnIndex);
			cursor.close();

			imageView.setImageBitmap(BitmapFactory.decodeFile(this.imageLocation));

		} else if ((requestCode == CAMERA_REQUEST) && (resultCode == RESULT_OK)) {
			this.imageLocation = data.getData().getPath();
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(photo);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Take Picture") {
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			this.startActivityForResult(cameraIntent, CAMERA_REQUEST);
		} else if (item.getTitle() == "Gallery") {
			Intent i = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			this.startActivityForResult(i, RESULT_LOAD_IMAGE);
		} else
			return false;
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.add_screen);

		this.bindControls();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Picture Source");
		menu.add(0, v.getId(), 0, "Take Picture");
		menu.add(0, v.getId(), 0, "Gallery");
	}
}
