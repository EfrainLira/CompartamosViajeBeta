package com.ganlen.compartamosviaje;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.List;

public class ListAdapter_Promocion extends ArrayAdapter<Upload_Promocion> {
    private Activity context;
    private int resource;
    private List<Upload_Promocion> listImage;

    public ListAdapter_Promocion(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Upload_Promocion> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(R.id.tvImageName);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        tvName.setText(listImage.get(position).getName());
        Glide.with(context).load(listImage.get(position).getUrl()).into(img);
        return v;
    }
}