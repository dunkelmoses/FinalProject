package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowNasaImage extends AppCompatActivity {
    private Intent intent;
    private String date;
    private Button addFav;
    private Button backTo;
    private Bitmap saveImage;
    private BitmapDrawable bitmapDrawable;
    private ImageView imageView;
    private OutputStream outputStream;
    private TextView dateImage, urlImage, hdUrlImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nasa_image);
        imageView = findViewById(R.id.ImageFromUrl);
        dateImage = findViewById(R.id.dateOftheImage);
        urlImage = findViewById(R.id.RegUrl);
        hdUrlImage = findViewById(R.id.hdUrl);
        addFav = findViewById(R.id.AddToFav);

        intent = getIntent();
        date = intent.getStringExtra("date");
        CallJson callJson = new CallJson();
        callJson.execute();


    }

    public class CallJson extends AsyncTask<String, Integer, String> {
        String result;
        String imageURL, hdImageURL;
        Bitmap image;

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + date);
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
                hdImageURL = jObject.getString("hdurl");

            } catch (Exception e) {
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            Picasso.with(ShowNasaImage.this).load(imageURL).into(imageView);

            dateImage.setText(date);
            urlImage.setText(imageURL);
            hdUrlImage.setText(hdImageURL);
            addFav.setOnClickListener(add -> {

                bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
                saveImage = bitmapDrawable.getBitmap();
                saveToInternalStorage(saveImage);
            });
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("image", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,date+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}