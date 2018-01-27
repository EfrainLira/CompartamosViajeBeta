package com.ganlen.compartamosviaje;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Tab_ProductosFragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private List<Upload_Promocion> imgList;
    private ListView lv;
    private ListAdapter_Promocion adapter;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    public static final String FB_Database_Path = "productos";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_productos, container, false);

        final ArrayList imgList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.listViewImage);
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Por favor espera", "Cargando promociones...",true);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_Database_Path);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Upload_Promocion img = snapshot.getValue(Upload_Promocion.class);
                    imgList.add(img);
                }
                adapter = new ListAdapter_Promocion(getActivity(), R.layout.activity_item, imgList);
                lv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
        return view;
    }
}
