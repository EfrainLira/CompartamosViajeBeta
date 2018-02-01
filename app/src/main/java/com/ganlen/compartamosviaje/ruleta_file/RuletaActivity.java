package com.ganlen.compartamosviaje.ruleta_file;

import android.os.Bundle;
import android.app.Activity;
import com.ganlen.compartamosviaje.R;

public class RuletaActivity extends Activity {
    private RuletaView mRouletteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);
        mRouletteView = findViewById(R.id.rouletteView);
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