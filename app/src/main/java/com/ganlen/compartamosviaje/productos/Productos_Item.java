package com.ganlen.compartamosviaje.productos;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ganlen.compartamosviaje.R;

public class Productos_Item extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.activity_lugares__item, container, false);
        return rootview;
    }
}


