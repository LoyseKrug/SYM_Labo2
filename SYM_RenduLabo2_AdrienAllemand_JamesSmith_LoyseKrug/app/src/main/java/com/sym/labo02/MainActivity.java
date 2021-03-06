/**
 * Authors: Adrien Allemand, James Smith, Loyse Krug
 *
 * Date: 25.11.2018
 *
 * Objective: class representing the Main Activity
 *
 * Comments: -
 *
 * Sources: -
 *
 */

package com.sym.labo02;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.sym.labo02.FragmentManagers.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncFragment.OnFragmentInteractionListener {

    Toolbar toolbar = null;
    DrawerLayout drawer = null;
    NavigationView navigationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Run Async Fragment as default fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AsyncFragment fragment = AsyncFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

        toolbar.setTitle("Asynchrone");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_asynch) {
            toolbar.setTitle("Asynchrone");
            AsyncFragment fragment = AsyncFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (id == R.id.nav_differe) {
            toolbar.setTitle("Différé");
            DiffereFragment fragment = DiffereFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (id == R.id.nav_graphql) {
            toolbar.setTitle("GraphQL");
            GraphQLFragment fragment = GraphQLFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (id == R.id.nav_serialization) {
            toolbar.setTitle("Sérialisé");
            SerializationFragment fragment = SerializationFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        } else if (id == R.id.nav_compress) {
            toolbar.setTitle("Compressé");
            CompressedFragment fragment = CompressedFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
