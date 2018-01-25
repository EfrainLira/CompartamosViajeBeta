package com.ganlen.compartamosviaje;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Perfil_List extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private List<Upload_Promocion> imgList;
    private ListView lv;
    private ListAdapter_Promocion adapter;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    public static final String FB_Database_Path = "compras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil__list);

        imgList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listViewImage);

        //Show progress dialog during list image loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Espera cargando compras...");
        progressDialog.show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_Database_Path);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ProductosUpload class require default constructor
                    Upload_Promocion img = snapshot.getValue(Upload_Promocion.class);
                    imgList.add(img);
                }
                //Init adapter
                adapter = new ListAdapter_Promocion(Perfil_List.this, R.layout.activity_item, imgList);
                //Set adapter for listview
                lv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}