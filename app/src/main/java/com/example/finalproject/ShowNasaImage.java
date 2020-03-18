package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowNasaImage extends AppCompatActivity {
    private  Intent intent;
    private String date;
    private Button addFav;
    private Button backTo;

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nasa_image);
        imageView = findViewById(R.id.ImageFromUrl);
        intent = getIntent();
        date = intent.getStringExtra("date");
        CallJson callJson = new CallJson();
        callJson.execute();
    }

    public class CallJson extends AsyncTask<String, Integer, String> {
        String result;
        String imageURL;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date="+date);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inStream = connection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                 result = sb.toString();
                //now a JSON table:
                JSONObject jObject = new JSONObject(result);
                imageURL = jObject.getString("url");
            } catch (Exception e) {
            }
            return "Done";
        }
        @Override
        protected void onPostExecute(String s) {
            Picasso.with(ShowNasaImage.this).load(imageURL).into(imageView);
        }
    }
}