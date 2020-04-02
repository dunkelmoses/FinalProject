package com.example.finalproject.NasaLonLat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.MainActivity;
import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NasaDisplayImageryDatabase extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private Intent intent;
    private String lat, lon,imageURL;
    private Button addFav;
    private Bitmap saveImage;
    private BitmapDrawable bitmapDrawable;
    private ImageView imageView;
    private TextView  latData, lonData,wrongData;
    DatabaseNasaImagery db;
    private final String SEND_LAT = "LAT";
    private final String SEND_LON = "LON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_display_imagery);
        imageView = findViewById(R.id.ImageFromUrl);
        latData = findViewById(R.id.latData);
        lonData = findViewById(R.id.lonData);
        addFav = findViewById(R.id.AddToFav);
        wrongData = findViewById(R.id.WrongData);
        intent = getIntent();
        lat = intent.getStringExtra(SEND_LAT);
        lon = intent.getStringExtra(SEND_LON);

        CallJson callJson = new CallJson();
        callJson.execute();
        db = new DatabaseNasaImagery(this);

        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        tBar.setTitle("Nasa Imagery");
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
        String newLink1,newLink2,newLink3;

        @Override
        protected String doInBackground(String... strings) {
             newLink1 = "https://dev.virtualearth.net/REST/V1/Imagery/Map/Birdseye/";
             newLink2 = ",";
             newLink3 = "/20?dir=180&ms=500,500&key=AhiAkUp5OJNThWRDqdFnxmsd2SCgIwaFn_k9Q2UEroZ69tQxE6zV1rY5klvlJLne";
            try {
                URL url = new URL(newLink1+lon+newLink2+lat+newLink3);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inStream = connection.getInputStream();

            } catch (Exception e) {
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {

                lonData.setText("Longitude: " + lon);
                latData.setText("Latitude: " + lat);
                imageURL = newLink1+lon+newLink2+lat+newLink3;
                Picasso.with(NasaDisplayImageryDatabase.this).load(imageURL).into(imageView);
                addFav.setOnClickListener(add -> {
                    bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                    saveImage = bitmapDrawable.getBitmap();
                    saveToInternalStorage(saveImage);
                });
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//         path to /data/data/yourapp/app_data/image
        File directory = cw.getDir("imageLonLat", Context.MODE_PRIVATE);
//         Create imageDir
        File mypath = new File(directory, lon+lat + ".jpg");
        FileOutputStream fos = null;

        try {
            if (!mypath.exists()) {
                fos = new FileOutputStream(mypath);
//                 Use the compress method on the BitMap
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                add the data to databse
                db.inserData(imageURL,lon,lat);
                Toast.makeText(NasaDisplayImageryDatabase.this, "Image Added", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(NasaDisplayImageryDatabase.this, "Image Exist", Toast.LENGTH_LONG).show();
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
                Intent mainPage = new Intent(NasaDisplayImageryDatabase.this, MainActivity.class);
                startActivity(mainPage);
                break;
            case R.id.FavouriteList:
                Intent intent = new Intent(NasaDisplayImageryDatabase.this, DataList.class);
                startActivity(intent);
                break;
            case R.id.SearchImage:
                Intent search = new Intent(NasaDisplayImageryDatabase.this, NasaImageryDatabase.class);
                startActivity(search);
                break;


            case R.id.help:
                Toast.makeText(NasaDisplayImageryDatabase.this,"This Project Was Made By ",Toast.LENGTH_LONG).show();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item selected
            case R.id.MainPage:
                Toast.makeText(NasaDisplayImageryDatabase.this,"This Project Was Made By Team1",Toast.LENGTH_LONG).show();
                break;
            case R.id.FavouriteList:
                Toast.makeText(NasaDisplayImageryDatabase.this,"This Project Was Made By Team2",Toast.LENGTH_LONG).show();
                break;
            case R.id.SearchImage:
                Toast.makeText(NasaDisplayImageryDatabase.this,"This Project Was Made By Team3",Toast.LENGTH_LONG).show();
                break;
            case R.id.help:
                help();
                break;
            case R.id.BBC:
                Toast.makeText(NasaDisplayImageryDatabase.this,"This Project Was Made By Team5",Toast.LENGTH_LONG).show();

                break;
            case R.id.GUADRIAN:
                Toast.makeText(NasaDisplayImageryDatabase.this,"This Project Was Made By Team6",Toast.LENGTH_LONG).show();

                break;
            case R.id.NASAIMAGE:
                Toast.makeText(NasaDisplayImageryDatabase.this,"This Project Was Made By Team7",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private void help(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This is help")
                .setMessage("- If you want \n" +
                        "- to go back \n" +
                        "- click the arrow \n" +
                        "- in the bottom left" +
                        " - corner")
                .setPositiveButton("OK", (click, b) -> {

        })

                .create().show();
    }
}