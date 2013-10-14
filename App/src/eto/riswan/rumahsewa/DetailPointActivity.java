package eto.riswan.rumahsewa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import eto.riswan.rumahsewa.helper.Database;
import eto.riswan.rumahsewa.model.RumahSewa;

public class DetailPointActivity extends OrmLiteBaseActivity<Database> {

	ScrollView v;
	long id;
	RumahSewa r;

	public void btnViewDirection_Click(View v) {

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
					TextView txtPhoneNumber = (TextView) DetailPointActivity.this.v
							.findViewById(R.idDetail.txtPhoneNumber);
					Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
							+ txtPhoneNumber.getText().toString().trim()));
					DetailPointActivity.this.startActivity(callIntent);

				}
			});
			txtPhoneNumber.setText(this.r.phoneNumber);

			TextView txtRent = (TextView) this.v.findViewById(R.idDetail.txtRent);
			txtRent.setText(this.r.rent.toString());

			Button btnViewDirection = (Button) this.v.findViewById(R.idDetail.btnViewDirection);
			btnViewDirection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri
							.parse("google.navigation:q=" + DetailPointActivity.this.r.latitude + ","
									+ DetailPointActivity.this.r.longitude));
					// Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri
					// .parse("geo:" + DetailPointActivity.this.r.latitude + ","
					// + DetailPointActivity.this.r.longitude));
					DetailPointActivity.this.startActivity(i);

				}
			});

			// TextView txtDistanceFromPosition = (TextView) this.v.findViewById(R.idDetail.);
			// txtDistanceFromPosition.setText(String.valueOf(r
			// .getDistanceFromLocation(DetailPointActivity.this)));
			//
			// TextView txtDistanceFromCenter = (TextView) this.v.findViewById(R.list.txtDistanceFromCenter);
			// txtDistanceFromCenter.setText(String.valueOf(r.getDistanceFromCenter(DetailPointActivity.this)));
		}

		this.setContentView(this.v);
	}
}
