package com.devmasterteam.convidados.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.devmasterteam.convidados.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Identifica os elementos
        this.mViewHolder.mFloatAddGuest = findViewById(R.id.fab_add_guest);
        this.mViewHolder.mDrawerLayout = findViewById(R.id.drawer_layout);
        this.mViewHolder.mNavigationView = findViewById(R.id.nav_view);

        // Adiciona eventos de click
        this.mViewHolder.mFloatAddGuest.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mViewHolder.mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.mViewHolder.mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        this.mViewHolder.mNavigationView.setNavigationItemSelectedListener(this);

        // Incia default fragment
        this.startDefaultFragment();
    }

    @Override
    public void onBackPressed() {
        if (mViewHolder.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mViewHolder.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
    Responsável pela criação do menu superior direito
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass = null;
        int id = item.getItemId();

        if (id == R.id.nav_all_guests) {
            fragmentClass = AllInvitedFragment.class;
        } else if (id == R.id.nav_present) {
            fragmentClass = PresentFragment.class;
        } else {
            fragmentClass = AbsentFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insere a fragment substituindo qualquer uma existente
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_content, fragment).commit();

        mViewHolder.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fab_add_guest) {
            Intent guestForm = new Intent(this, GuestFormActivity.class);
            startActivity(guestForm);
        }
    }

    /**
     * Inicia fragment padrão
     */
    public void startDefaultFragment() {
        Fragment fragment = null;
        Class fragmentClass = AllInvitedFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, fragment).commit();
        this.mViewHolder.mNavigationView.getMenu().getItem(0).setChecked(true);
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        FloatingActionButton mFloatAddGuest;
        DrawerLayout mDrawerLayout;
        NavigationView mNavigationView;
    }

}