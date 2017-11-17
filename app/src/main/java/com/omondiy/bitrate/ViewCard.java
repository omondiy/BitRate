package com.omondiy.bitrate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCard extends AppCompatActivity {
    String currencyval;
    String coinval;
    JSONObject jsonobject;
    JSONArray jsonarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currencyval = extras.getString("currencyval");
            coinval = extras.getString("coinval");

            TextView convetheader = (TextView) findViewById(R.id.convetheader);
            TextView convertedtext = (TextView) findViewById(R.id.convertedtext);

            new Convert().execute(currencyval, coinval);
        }

    }

    // FetchCoins AsyncTask
    private class Convert extends AsyncTask<String, JSONObject, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            String curr = params[0];
            String coin = params[1];
            String myUrl = "https://min-api.cryptocompare.com/data/price?fsym="+ coin +"&tsyms="+ curr;

            jsonobject = JSONfunctions
                    .getJSONfromURL(myUrl);
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if(jsonObject != null) {
                    //jsonarray = jsonObject.getJSONArray("reviewscount");
                    // Locate the array name in JSON
                    String val = jsonObject.getString("USD");

                    TextView convetheader = (TextView) findViewById(R.id.convetheader);
                    TextView convertedtext = (TextView) findViewById(R.id.convertedtext);

                    convetheader.setText("Here is the converted value in " + currencyval + ":");
                    convertedtext.setText(val);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

        }
    }

}
