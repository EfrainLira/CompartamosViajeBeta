package com.ganlen.compartamosviaje.tabs;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ganlen.compartamosviaje.AdapterList;
import com.ganlen.compartamosviaje.R;
import com.ganlen.compartamosviaje.UploadList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tab_EventosFragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private List<UploadList> imgList;
    private ListView lv;
    private AdapterList adapter;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    public static final String FB_Database_Path = "eventos";
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_tab_eventos, container, false);
        final ArrayList imgList = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.listViewImage);
        dialog = ProgressDialog.show(getActivity(), "Por favor espera", "Cargando eventos...",true);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_Database_Path);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UploadList img = snapshot.getValue(UploadList.class);
                    imgList.add(img);
                }
                adapter = new AdapterList(getActivity(), R.layout.activity_item, imgList);
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
