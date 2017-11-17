package com.omondiy.bitrate;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class RateCardList extends AppCompatActivity {
    ListView listview;
    RateAdapter rateCardAdapter;
    ArrayList<Cards> arraycard;
    String state;
    Parcelable mListInstanceState;
    static String LIST_INSTANCE_STATE;
    String[] currencies;

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.

    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.

        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LIST_INSTANCE_STATE != null)
            listview.onRestoreInstanceState(mListInstanceState);
        LIST_INSTANCE_STATE = null;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_INSTANCE_STATE, listview.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListInstanceState = state.getParcelable(LIST_INSTANCE_STATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_card_list);

        listview = (ListView) findViewById(R.id.listview);

        String[] currencies = {"USD", "EUR", "Pencils", "Notebooks" };
        ArrayList<String> aList = new ArrayList<String>(Arrays.asList(currencies));
        // Pass the results into ListViewAdapter.java
        rateCardAdapter = new RateAdapter(RateCardList.this, R.layout.list_item_card, arraycard);
        // Set the adapter to the ListView
        Log.e("searchresultsadapter3",String.valueOf(rateCardAdapter));
        listview.setAdapter(rateCardAdapter);

        //Prevent null parcelable objects
        if(!state.equals("0")) {
            listview.onRestoreInstanceState(mListInstanceState);
        }
        // Locate the gridView in gridView_main.xml
        rateCardAdapter.notifyDataSetChanged();
    }
}
