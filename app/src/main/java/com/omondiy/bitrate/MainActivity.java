package com.omondiy.bitrate;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    JSONObject jsonobject;
    String myUrl;
    List<String> coinarraylist;
    Spinner spinner, coinspinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String[] currencies = {"CNY", "INR", "SGD", "TWD", "USD", "AUD", "EUR", "GBP", "RUB", "ZAR",
                "MXN", "ILS", "MYR", "NZD", "SEK", "CHF", "NOK", "BRL", "TRY", "PKR", "NGN" };
        List<String> aList = new ArrayList<String>(Arrays.asList(currencies));

        spinner = (Spinner) findViewById(R.id.currencyspinner);
        coinspinner = (Spinner) findViewById(R.id.coinspinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,aList);
        spinner.setAdapter(adapter);

        new FetchCoins().execute();

        Button createCard = (Button) findViewById(R.id.createCard);
        createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RateCardList.class);
                String currencyval = String.valueOf(spinner.getSelectedItem());
                String coinval = String.valueOf(coinspinner.getSelectedItem());
                intent.putExtra("currencyval", currencyval);
                intent.putExtra("coinval", coinval);
                startActivity(intent);
            }
        });
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // FetchCoins AsyncTask
    private class FetchCoins extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Do stuff with params, for example:
            myUrl = "https://www.cryptocompare.com/api/data/coinlist/";
            // Create an array
            //coinarraylist = new ArrayList<Coins>();
            coinarraylist = new ArrayList<String>();
            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONfunctions
                    .getJSONfromURL(myUrl);
            try {
                // Locate the array name in JSON
                if(jsonobject != null) {
                    JSONArray jsonarray = jsonobject.getJSONArray("coins");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        //Coins list = new Coins();
                        jsonobject = (JSONObject) jsonarray.get(i);
                        //list.setCoin(jsonobject.getString("Data"));
                        coinarraylist.add(jsonobject.getString("Data"));
                    }                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            //mProgressDialog.dismiss();
            // Locate the listview in listview_main.xml
            Coins list = new Coins();

            if(coinarraylist.size() > 0){
                ArrayAdapter<String> coinadapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,coinarraylist);
                coinspinner.setAdapter(coinadapter);
            }
        }
    }

}
