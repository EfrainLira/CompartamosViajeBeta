package com.ganlen.compartamosviaje.ruleta_file;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ganlen.compartamosviaje.R;

public class RuletaSectorEdit extends Activity {
	
	private RuletaSectorsDbAdapter mDbHelper;
    private EditText mSectorText;
    private Long mRowId;
    private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.ruleta_edit_sector);
		setTitle("Configuración");
		
        mSectorText = (EditText) findViewById(R.id.sector_text);
        Button buttonOk = (Button) findViewById(R.id.button_ok);
        Button buttonDelete = (Button) findViewById(R.id.button_delete);

        mDbHelper = new RuletaSectorsDbAdapter(this);
        mDbHelper.open();

        Bundle extras = getIntent().getExtras();
        mRowId = extras != null ? extras.getLong(RuletaSectorsDbAdapter.KEY_ROWID)
                                : null;
        mPosition = extras != null ? extras.getInt(RuletaSectorsConfig.LIST_POSITION)
                : null;
        populateText();   
        
       
        buttonOk.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
            	saveState();
            	setResult(RESULT_OK);
            	finish();          	
            }
        });
        if (mPosition < 2){
        	buttonDelete.setEnabled(false);
        }
        else
        {
        buttonDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks
                mDbHelper.deleteSector(mRowId);
                setResult(RESULT_OK);
                finish();
            }
        });
        }
		
	}
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        populateText();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(RuletaSectorsDbAdapter.KEY_ROWID, mRowId);
    }
    
    private void populateText() {
        if (mRowId != null) {
            Cursor sector = mDbHelper.fetchSector(mRowId);
            startManagingCursor(sector);
            mSectorText.setText(sector.getString(
                    sector.getColumnIndexOrThrow(RuletaSectorsDbAdapter.KEY_BODY)));
        }
    }
    
    private void saveState() {
        String body = mSectorText.getText().toString();
        if (body.length() != 0 && mRowId != null) {
             mDbHelper.updateSector(mRowId, body);
        }
    }
}