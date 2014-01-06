package eto.riswan.rumahsewa.admin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

	public static String url = Global.BaseUrl + "/rumahSewa/create/";

	public EditText txtOwnersName;
	public EditText txtAddress;
	public EditText txtPhoneNumber;
	public EditText txtRent;
	public EditText txtFacilities;
	public EditText txtDescription;
	public Button btnLoadPicture;
	public String imageLocation;

	ProgressDialog progressBar;

	private List<Parameter> lParemeters;

	private boolean asUpdate = false;

	private RumahSewa rumahSewa;

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
		RumahSewa rumahSewa;
		if (this.rumahSewa != null)
			rumahSewa = this.rumahSewa;
		else
			rumahSewa = new RumahSewa();
		rumahSewa.address = this.txtAddress.getText().toString();
		rumahSewa.description = this.txtDescription.getText().toString();
		rumahSewa.facilities = this.txtFacilities.getText().toString();
		rumahSewa.ownersName = this.txtOwnersName.getText().toString();
		rumahSewa.phoneNumber = this.txtPhoneNumber.getText().toString();
		rumahSewa.rent = Long.valueOf(this.txtRent.getText().toString());
		if (!this.asUpdate) {
			rumahSewa.latitude = GeoLocation.getCurrentLocation(this).getLatitude();
			rumahSewa.longitude = GeoLocation.getCurrentLocation(this).getLongitude();
		}
		rumahSewa.picturePath = this.imageLocation;

		RuntimeExceptionDao<RumahSewa, Long> rumahSewaDao;
		rumahSewaDao = this.getHelper().getRumahSewaRuntime();
		if (!this.asUpdate)
			rumahSewaDao.create(rumahSewa);
		else
			rumahSewaDao.update(rumahSewa);

		this.lParemeters = new ArrayList<Parameter>();
		if (!this.asUpdate) this.lParemeters.add(new Parameter("id_rumahSewa", rumahSewa.getGlobalId(this)));
		this.lParemeters.add(new Parameter("latitude", rumahSewa.latitude.toString()));
		this.lParemeters.add(new Parameter("longitude", rumahSewa.longitude.toString()));
		this.lParemeters.add(new Parameter("namapemilik", rumahSewa.ownersName.toString()));
		this.lParemeters.add(new Parameter("alamat", rumahSewa.address.toString()));
		this.lParemeters.add(new Parameter("no_telp", rumahSewa.phoneNumber.toString()));
		this.lParemeters.add(new Parameter("hargasewa", rumahSewa.rent.toString()));
		if (!this.asUpdate)
			this.lParemeters.add(new Parameter("foto", this.imageLocation, ParameterType.BINARY));
		this.lParemeters.add(new Parameter("fasilitas", rumahSewa.facilities.toString()));
		this.lParemeters.add(new Parameter("deskripsi", rumahSewa.description.toString()));
		this.lParemeters.add(new Parameter("created_date", rumahSewa.createdDate.toString()));

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					HttpResponse response = Service.makeRequest(AddPointActivity.url,
							AddPointActivity.this.lParemeters);

					@SuppressWarnings("unused")
					String s = Service.inputStreamToString(response.getEntity().getContent()).toString();

					AddPointActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AddPointActivity.this, "Data saved successfully.",
									Toast.LENGTH_LONG).show();

							Intent x = new Intent(AddPointActivity.this, MapActivity.class);
							AddPointActivity.this.finish();
							AddPointActivity.this.startActivity(x);

							Toast.makeText(AddPointActivity.this, "Upload successful.", Toast.LENGTH_LONG)
									.show();
							AddPointActivity.this.progressBar.dismiss();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();

					AddPointActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AddPointActivity.this, "Upload failed.", Toast.LENGTH_LONG).show();
							AddPointActivity.this.progressBar.dismiss();
						}
					});
				}
			}
		};
		new Thread(runnable).start();

		this.progressBar = new ProgressDialog(this);
		this.progressBar.setCancelable(false);
		this.progressBar.setMessage("Saving data...");
		this.progressBar.show();
	}

	private String createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		return image.getAbsolutePath();
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
			try {
				this.imageLocation = this.createImageFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

		if (this.getIntent().hasExtra("asUpdate")) {
			this.asUpdate = true;
			try {
				this.rumahSewa = this.getHelper().getRumahSewa()
						.queryForId(this.getIntent().getExtras().getLong("Id"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AddPointActivity.url = AddPointActivity.url.replace("create", "update");
			if (!AddPointActivity.url.contains("?id="))
				AddPointActivity.url = AddPointActivity.url + "?id=" + this.rumahSewa.getGlobalId(this);

			this.txtOwnersName.setText(this.rumahSewa.ownersName);
			this.txtAddress.setText(this.rumahSewa.address);
			this.txtPhoneNumber.setText(this.rumahSewa.phoneNumber);
			this.txtRent.setText(this.rumahSewa.rent.toString());
			this.txtFacilities.setText(this.rumahSewa.facilities);
			this.txtDescription.setText(this.rumahSewa.description);
			this.imageLocation = this.rumahSewa.picturePath;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Picture Source");
		menu.add(0, v.getId(), 0, "Take Picture");
		menu.add(0, v.getId(), 0, "Gallery");
	}
}
