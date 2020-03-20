package com.example.finalproject.NasaImage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowNasaImage extends AppCompatActivity {
    private Intent intent;
    private String date, hdImageURL, imageURL;
    private Button addFav;
    private Button backTo;
    private Bitmap saveImage;
    private BitmapDrawable bitmapDrawable;
    private ImageView imageView;
    private OutputStream outputStream;
    private TextView dateImage, urlImage, hdUrlImage;
    DatabaseNasaImage db;
    ArrayList<String> arrayList ;

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
        db = new DatabaseNasaImage(this);


    }

    public class CallJson extends AsyncTask<String, Integer, String> {
        String result;
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
                bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                saveImage = bitmapDrawable.getBitmap();
                saveToInternalStorage(saveImage);
            });
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/image
        File directory = cw.getDir("image", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, date + ".jpg");
        FileOutputStream fos = null;

        try {
            if (!mypath.exists()) {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                //add the data to databse by this line
                db.inserData(date,imageURL,hdImageURL);
                Toast.makeText(ShowNasaImage.this, "Added", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(ShowNasaImage.this, "Already Exist", Toast.LENGTH_LONG).show();
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }


}