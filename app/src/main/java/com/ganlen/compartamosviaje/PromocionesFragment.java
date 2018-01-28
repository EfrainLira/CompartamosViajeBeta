package com.ganlen.compartamosviaje;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class PromocionesFragment extends Fragment {
    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_promociones, container, false);
        View contenedor = (View) container.getParent();
        appBar = (AppBarLayout) contenedor.findViewById(R.id.appbar);
        tabs = new TabLayout(getActivity());
        tabs.setTabTextColors(Color.parseColor("#ffffff"), Color.parseColor("#9fffe0"));

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null) && (wifi.isConnected() | datac.isConnected())) {
            //Conexión disponible
            appBar.addView(tabs);
            viewPager = (ViewPager) view.findViewById(R.id.pager);
            ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
            viewPager.setAdapter(pagerAdapter);
            tabs.setupWithViewPager(viewPager);

        }else{
            //No hay conexión
            Toast.makeText(getActivity().getApplicationContext(), "No hay conexión a Internet", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(tabs);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter (FragmentManager fragmentManager){
            super(fragmentManager);
        }
        String [] tituloTabs = {"Lugares", "Productos", "Servicios"};

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new Tab_LugaresFragment();
                case 1: return new Tab_ProductosFragment();
                case 2: return new Tab_ServiciosFragment();
            }   return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tituloTabs[position];
        }
    }
}