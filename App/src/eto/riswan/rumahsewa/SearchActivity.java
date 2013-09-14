package eto.riswan.rumahsewa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import eto.riswan.rumahsewa.helper.Database;
import eto.riswan.rumahsewa.model.RumahSewa;

public class SearchActivity extends OrmLiteBaseActivity<Database> {
	private final long rentRange = 100000;
	ListView listViewPrice;
	ListView listViewFacility;
	ArrayList<String> arrayListPrice; // list of the strings that should appear in ListView
	ArrayList<String> arrayListFacility; // list of the strings that should appear in ListView

	String selectedPrice;
	ArrayList<String> selectedFacility;

	private int menuId;

	private void bindBiayaList() {
		this.listViewPrice = (ListView) this.findViewById(R.id.lstPrice);
		this.arrayListPrice = new ArrayList<String>();

		RuntimeExceptionDao<RumahSewa, Long> rumahSewaDao;
		rumahSewaDao = this.getHelper().getRumahSewaRuntime();
		List<RumahSewa> accounts = rumahSewaDao.queryForAll();
		if (accounts.size() > 0) {
			ArrayList<Long> rents = new ArrayList<Long>();
			Long tmp = (long) 0;
			for (RumahSewa point : accounts) {
				tmp = (long) (Math.floor(point.Rent / this.rentRange) * this.rentRange);
				if (!rents.contains(tmp)) rents.add(tmp);
			}
			Collections.sort(rents);

			tmp = (long) 0;
			for (Long point : rents) {
				this.arrayListPrice.add(tmp.toString() + "-" + String.valueOf(point + this.rentRange));
				tmp = point + this.rentRange;
			}
		} else {
			this.arrayListPrice.add("400000-500000");
			this.arrayListPrice.add("500000-600000");
		}

		this.listViewPrice.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		this.listViewPrice.setAdapter(new ArrayAdapter<String>(this.getApplicationContext(),
				R.layout.radio_list_item, this.arrayListPrice) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				CheckedTextView textView = (CheckedTextView) super.getView(position, convertView, parent);
				textView.setTextColor(Color.BLACK);
				textView.setCheckMarkDrawable(android.R.drawable.btn_radio);

				return textView;
			}
		});

		this.listViewPrice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long itemId) {
				CheckedTextView textView = (CheckedTextView) view;
				SearchActivity.this.selectedPrice = (String) textView.getText();
			}
		});
	}

	private void bindFacilitiesList() {
		this.listViewFacility = (ListView) this.findViewById(R.id.lstFacilities);
		this.arrayListFacility = new ArrayList<String>();

		RuntimeExceptionDao<RumahSewa, Long> rumahSewaDao;
		rumahSewaDao = this.getHelper().getRumahSewaRuntime();
		List<RumahSewa> accounts = rumahSewaDao.queryForAll();
		if (accounts.size() > 0)
			for (RumahSewa point : accounts) {
				String[] currentFacilities = point.Facilities.split(",");
				for (String facility : currentFacilities)
					if (!this.arrayListFacility.contains(facility.trim()))
						this.arrayListFacility.add(facility.trim());
			}
		else {
			this.arrayListFacility.add("Kamar mandi dalam");
			this.arrayListFacility.add("Parkir motor");
			this.arrayListFacility.add("Parkir mobil");
			this.arrayListFacility.add("Wastafel");
		}

		Collections.sort(this.arrayListFacility);
		this.listViewFacility.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		this.listViewFacility.setAdapter(new ArrayAdapter<String>(this.getApplicationContext(),
				R.layout.radio_list_item, this.arrayListFacility) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				CheckedTextView textView = (CheckedTextView) super.getView(position, convertView, parent);
				textView.setTextColor(Color.BLACK);

				return textView;
			}
		});

		if (SearchActivity.this.selectedFacility == null)
			SearchActivity.this.selectedFacility = new ArrayList<String>();

		SearchActivity.this.selectedFacility.clear();

		this.listViewFacility.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long itemId) {
				CheckedTextView textView = (CheckedTextView) view;
				String tmp = (String) textView.getText();
				if (!SearchActivity.this.selectedFacility.contains(tmp))
					SearchActivity.this.selectedFacility.add(tmp);
				else
					SearchActivity.this.selectedFacility.remove(tmp);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.search_screen);

		this.bindBiayaList();
		this.bindFacilitiesList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add("Search");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		this.menuId = item.getItemId();

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == this.menuId) {
			Intent intent = new Intent(this, ListPointActivity.class);
			intent.putExtra("rent", this.selectedPrice);
			intent.putStringArrayListExtra("facilities", this.selectedFacility);
			this.startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}
}
