package com.ganlen.compartamosviaje;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.ganlen.compartamosviaje.ruleta_file.RuletaActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageInfo info;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new PerfilFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_perfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new PerfilFragment()).commit();
        } else if (id == R.id.nav_noticias) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new NoticiasFragment()).commit();
        } else if (id == R.id.nav_explorar) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new ExplorarFragment()).commit();
        } else if (id == R.id.nav_promociones) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new PromocionesFragment()).commit();
        } else if (id == R.id.nav_acerca) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new AcercaDeFragment()).commit();
        } else if (id == R.id.nav_ruleta){
            Intent ruleta = new Intent(MainActivity.this, RuletaActivity.class);
            startActivity(ruleta);
        } else if (id == R.id.nav_clima){
            fragmentManager.beginTransaction().replace(R.id.contenedor, new ClimaFragment()).commit();
        } else if (id == R.id.nav_descubrir){
            fragmentManager.beginTransaction().replace(R.id.contenedor, new DescubrirFragment()).commit();
        } else if (id == R.id.nav_share){
            String appLink = "https://play.google.com/store/apps/details?id=com.ganlen.compartamosviaje&hl=es";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, "Compartamos Viaje");
            share.putExtra(Intent.EXTRA_TEXT, "Te invito a descargar una nueva app para Explorar el mundo contigo \n" + appLink);
            startActivity(Intent.createChooser(share, "Compartir"));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}