package com.ganlen.compartamosviaje.servicios;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.ganlen.compartamosviaje.R;
import com.ganlen.compartamosviaje.lugares.Lugares_List;
import com.ganlen.compartamosviaje.productos.Productos_List;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Servicios_List extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private List<ServiciosUpload> imgList;
    private ListView lv;
    private ServiciosListAdapter adapter;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    public static final String FB_Database_Path = "servicios";
    ImageView btnLugares, btnProductos, btnServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios__list);

        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewImage);

        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Espera cargando promociones...");
        progressDialog.show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_Database_Path);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ProductosUpload class require default constructor
                    ServiciosUpload img = snapshot.getValue(ServiciosUpload.class);
                    imgList.add(img);
                }
                //Init adapter
                adapter = new ServiciosListAdapter(Servicios_List.this, R.layout.activity_servicios__item, imgList);
                //Set adapter for listview
                lv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        btnLugares = (ImageView) findViewById(R.id.imagen1);
        btnProductos = (ImageView) findViewById(R.id.imagen2);
        btnServicios = (ImageView) findViewById(R.id.imagen3);

        btnLugares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent promo = new Intent(Servicios_List.this, Lugares_List.class);
                startActivity(promo);
                finish();
            }
        });

        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent promo = new Intent(Servicios_List.this, Productos_List.class);
                startActivity(promo);
                finish();
            }
        });

        btnServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent promo = new Intent(Servicios_List.this, Servicios_List.class);
                startActivity(promo);
                finish();
            }
        });

    }
}