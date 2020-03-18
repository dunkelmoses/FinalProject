package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowNasaImage extends AppCompatActivity {
    private TextView textView;
    private  Intent intent;
    private String imageURL;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nasa_image);
        textView = findViewById(R.id.showUrl);

        intent = getIntent();
        date = intent.getStringExtra("date");


        CallJson callJson = new CallJson();
        callJson.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=2020-02-21");
    }

    public class CallJson extends AsyncTask<String, Integer, String> {

        String imageURL;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL UVurl = new URL(strings[0]);
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                InputStream inStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                JSONObject jObject = new JSONObject(result);
                imageURL = jObject.getString("url");
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String url = imageURL + date;
            textView.setText(url);
        }
    }
}