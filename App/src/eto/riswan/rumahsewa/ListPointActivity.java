package eto.riswan.rumahsewa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import eto.riswan.rumahsewa.helper.Database;
import eto.riswan.rumahsewa.model.RumahSewa;

public class ListPointActivity extends OrmLiteBaseActivity<Database> {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView lv = new ListView(this);
		List<RumahSewa> rumahSewas = new ArrayList<RumahSewa>();
		ArrayList<String> al = new ArrayList<String>();
		Bundle extras = this.getIntent().getExtras();

		RuntimeExceptionDao<RumahSewa, Long> rumahSewaDao = this.getHelper().getRumahSewaRuntime();

		if (extras != null)
			try {
				ArrayList<String> facilities = extras.getStringArrayList("facilities");

				String[] rent = extras.getString("rent").split("-");

				QueryBuilder<RumahSewa, Long> qb = this.getHelper().getRumahSewa().queryBuilder();
				Where<RumahSewa, Long> qbWhere = qb.where();
				if (facilities.size() > 1) {
					for (String facility : facilities)
						qbWhere.like("Facilities", facility);
					qbWhere.or(facilities.size()); // wrap previous like statements in or
				} else if (facilities.size() == 1) qbWhere.like("Facilities", facilities.get(0));
				qbWhere.ge("Rent", Long.valueOf(rent[0])).and().le("Rent", Long.valueOf(rent[1]));
				if (!facilities.isEmpty()) qbWhere.and(2);

				rumahSewas = rumahSewaDao.query(qbWhere.prepare());

			} catch (java.sql.SQLException e) {
				// TODO: Exception Handling
				e.printStackTrace();
			}
		else
			rumahSewas = rumahSewaDao.queryForAll();

		if (rumahSewas.size() > 0) for (RumahSewa point : rumahSewas)
			al.add(point.OwnersName);

		Collections.sort(al);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(new ArrayAdapter<String>(this.getApplicationContext(), R.layout.radio_list_item, al) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				CheckedTextView textView = (CheckedTextView) super.getView(position, convertView, parent);
				textView.setTextColor(Color.BLACK);
				textView.setCheckMarkDrawable(null);

				return textView;
			}
		});

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		ll.setGravity(Gravity.CENTER);
		if (rumahSewas.size() <= 0) Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
		ll.addView(lv);
		this.setContentView(ll);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.idMap.action_map:
				Intent intent = new Intent(this, MapActivity.class);
				this.startActivity(intent);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}
}
