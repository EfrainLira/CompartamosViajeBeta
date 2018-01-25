package com.ganlen.compartamosviaje.ruleta_file;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.ganlen.compartamosviaje.R;

public class RuletaActivity extends Activity {
    private RuletaView mRouletteView;
    public static final int CONFIG_ID = Menu.FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);
        mRouletteView = (RuletaView) findViewById(R.id.rouletteView);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mRouletteView.updateSectors();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRouletteView.updateSectors();
    }
}
