package eto.riswan.rumahsewa;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import eto.riswan.rumahsewa.helper.Database;
import eto.riswan.rumahsewa.helper.Global;
import eto.riswan.rumahsewa.helper.Service;
import eto.riswan.rumahsewa.model.RumahSewa;

public class DetailPointActivity extends OrmLiteBaseActivity<Database> {

	ScrollView v;
	long id;
	RumahSewa r;

	private void doCall() {
		TextView txtPhoneNumber = (TextView) this.v.findViewById(R.idDetail.txtPhoneNumber);
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ txtPhoneNumber.getText().toString().trim()));
		this.startActivity(callIntent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.id = this.getIntent().getExtras().getLong("Id");
		this.v = (ScrollView) this.getLayoutInflater().inflate(R.layout.detail_screen, null);
		this.v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		this.r = this.getHelper().getRumahSewaRuntime().queryForId(this.id);

		if (this.r != null) {
			TextView txtOwnersName = (TextView) this.v.findViewById(R.idDetail.txtOwnerName);
			txtOwnersName.setText(this.r.ownersName);

			TextView txtPicture = (TextView) this.v.findViewById(R.idDetail.txtDescription);
			txtPicture.setText(this.r.description);

			TextView txtPrice = (TextView) this.v.findViewById(R.idDetail.txtFacilities);
			txtPrice.setText(this.r.facilities);

			TextView txtPhoneNumber = (TextView) this.v.findViewById(R.idDetail.txtPhoneNumber);
			txtPhoneNumber.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DetailPointActivity.this.doCall();
				}
			});
			txtPhoneNumber.setText(this.r.phoneNumber);

			TextView txtRent = (TextView) this.v.findViewById(R.idDetail.txtRent);
			txtRent.setText(this.r.rent.toString());

			TextView txtDistance = (TextView) this.v.findViewById(R.idDetail.txtDistance);
			txtDistance.setText(String.valueOf(this.r.getDistanceFromLocation(this)));

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						URL url;
						url = new URL(Global.BaseUrl.replace("/index.php", "")
								+ DetailPointActivity.this.r.picturePath);
						final Bitmap bmp = Service.loadBitmapFromUri(DetailPointActivity.this, url);
						DetailPointActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								ImageView imageLocation = (ImageView) DetailPointActivity.this.v
										.findViewById(R.idDetail.imageLocation);
								imageLocation.setImageBitmap(bmp);
							}
						});
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			thread.start();

			Button btnViewDirection = (Button) this.v.findViewById(R.idDetail.btnViewDirection);
			btnViewDirection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri
							.parse("google.navigation:q=" + DetailPointActivity.this.r.latitude + ","
									+ DetailPointActivity.this.r.longitude));
					DetailPointActivity.this.startActivity(i);

				}
			});

			Button btnCall = (Button) this.v.findViewById(R.idDetail.btnCall);
			btnCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DetailPointActivity.this.doCall();
				}
			});
		}

		this.setContentView(this.v);
	}
}
