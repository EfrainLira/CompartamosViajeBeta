package com.ganlen.compartamosviaje.ruleta_file;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ganlen.compartamosviaje.R;

public class RuletaSectorsConfig extends ListActivity {

	private static final int ACTIVITY_EDIT = 0;
	public static final String LIST_POSITION = "list_position";
	private RuletaSectorsDbAdapter mDbHelper;
	private Cursor mSectorsCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ruleta_sectors_config);
		setTitle(null);

		mDbHelper = new RuletaSectorsDbAdapter(this);
		mDbHelper.open();
		fillData();

		final Button buttonAdd = (Button) findViewById(R.id.button_add);
		buttonAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RuletaInitialization initializer = new RuletaInitialization(RuletaSectorsConfig.this);
				Resources res = getResources();
				String[] answers = res.getStringArray(R.array.predefined);
				initializer.createRandomSector(answers);
				fillData();
			}
		});
		final Button buttonDone = (Button) findViewById(R.id.button_done);
		buttonDone.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		final Button reset_b = (Button) findViewById(R.id.reset);
		reset_b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RuletaInitialization initializer = new RuletaInitialization(RuletaSectorsConfig.this);
				Resources res = getResources();
				String[] answers = res.getStringArray(R.array.predefined);
				initializer.initRoulette(answers);
				fillData();
			}
		});

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, RuletaSectorEdit.class);
		i.putExtra(RuletaSectorsDbAdapter.KEY_ROWID, id);
		i.putExtra(LIST_POSITION, position);
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}

	private void fillData() {
		// Get all of the rows from the database and create the item list
		mSectorsCursor = mDbHelper.fetchAllSectors();
		startManagingCursor(mSectorsCursor);
		final Button buttonAdd = (Button) findViewById(R.id.button_add);
		if (mSectorsCursor.getCount() < 12) {
			buttonAdd.setEnabled(true);
		} else {
			buttonAdd.setEnabled(false);
		}
		// Create an array to specify the fields we want to display in the list
		String[] from = new String[] { RuletaSectorsDbAdapter.KEY_BODY };

		// and an array of the fields we want to bind those fields to (in this
		// case just text1)
		int[] to = new int[] { R.id.text1 };

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter sectors = new SimpleCursorAdapter(this,
				R.layout.ruleta_sector_text, mSectorsCursor, from, to);
		setListAdapter(sectors);
	}
}