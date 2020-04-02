package com.example.finalproject.NasaImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.BBCNEWS;
import com.example.finalproject.Guardian;
import com.example.finalproject.MainActivity;
import com.example.finalproject.NasaDatabase;
import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;
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

public class ShowNasaImage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private Intent intent;
    private String date, hdImageURL, imageURL;
    private Button addFav;
    private Bitmap saveImage;
    private BitmapDrawable bitmapDrawable;
    private ImageView imageView;
    private TextView dateImage, urlImage, hdUrlImage;
    private DatabaseNasaImage db;
    private EditText messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nasa_image);
        imageView = findViewById(R.id.ImageFromUrl);
        dateImage = findViewById(R.id.dateOftheImage);
        urlImage = findViewById(R.id.RegUrl);
        hdUrlImage = findViewById(R.id.hdUrl);
        addFav = findViewById(R.id.AddToFav);
        messages = findViewById(R.id.messages);

        intent = getIntent();
        date = intent.getStringExtra("date");

        CallJson callJson = new CallJson();
        callJson.execute();
        db = new DatabaseNasaImage(this);

        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public class CallJson extends AsyncTask<String, Integer, String> {
        String result;


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

            dateImage.setText("Date: "+date);
            urlImage.setText("URL: "+imageURL);
            hdUrlImage.setText("HDURL: "+hdImageURL);
            addFav.setOnClickListener(add -> {
                bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                saveImage = bitmapDrawable.getBitmap();
                saveToInternalStorage(saveImage);

                Bundle bundle = new Bundle();
                bundle.putString("messages", messages.getText().toString());
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
                db.inserData(date,imageURL,hdImageURL,messages.getText().toString());
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.MainPage:
                Intent mainPage = new Intent(ShowNasaImage.this, MainActivity.class);
                startActivity(mainPage);
                break;
            case R.id.FavouriteList:
                Intent intent = new Intent(ShowNasaImage.this,ImagesList.class);
                startActivity(intent);
                break;
            case R.id.SearchImage:
                Intent search = new Intent(ShowNasaImage.this,NasaImageOfTheDay.class);
                startActivity(search);
                break;
            case R.id.help:
                Toast.makeText(ShowNasaImage.this,"This Project Was Made By Batman",Toast.LENGTH_LONG).show();
                break;
            case R.id.BBC:
                Intent bbc = new Intent(ShowNasaImage.this, BBCNEWS.class);
                startActivity(bbc);
                break;
            case R.id.GUADRIAN:
                Intent gardian = new Intent(ShowNasaImage.this, Guardian.class);
                startActivity(gardian);
                break;
            case R.id.NASALANGLAT:
                Intent nasaLongLat = new Intent(ShowNasaImage.this, NasaDatabase.class);
                startActivity(nasaLongLat);
                break;

        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.MainPage:
                Toast.makeText(ShowNasaImage.this,"This Project Was Made By WRAITH",Toast.LENGTH_LONG).show();
                break;
            case R.id.FavouriteList:
                Toast.makeText(ShowNasaImage.this,"This Project Was Made By CAUSTIC",Toast.LENGTH_LONG).show();
                break;
            case R.id.SearchImage:
                Toast.makeText(ShowNasaImage.this,"This Project Was Made By BATMAN",Toast.LENGTH_LONG).show();

            case R.id.help:
                Toast.makeText(ShowNasaImage.this,"This Project Was Made By EINSTEIN",Toast.LENGTH_LONG).show();
                break;
            case R.id.BBC:
                Toast.makeText(ShowNasaImage.this,"This Project Was Made By LUFFY",Toast.LENGTH_LONG).show();

                break;
            case R.id.GUADRIAN:
                Toast.makeText(ShowNasaImage.this,"This Project Was Made By NARUTO",Toast.LENGTH_LONG).show();

                break;
            case R.id.NASALANGLAT:
                Toast.makeText(ShowNasaImage.this,"This Project Was Made By ONE FOR ALL",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_nasa_image, menu);

        return true;
    }

}