package com.example.finalproject.NasaLonLat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.finalproject.BBCNEWS;
import com.example.finalproject.Guardian;
import com.example.finalproject.MainActivity;
import com.example.finalproject.NasaImageOfTheDay;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;

public class NasaImageryDatabase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    //    private TextView enterLong, enterLat;
    private EditText editLong, editLat;
    private Button enter;
    private final String SEND_LAT = "LAT";
    private final String SEND_LON = "LON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_imagery_database);
        editLong = findViewById(R.id.EditLon);
        editLat = findViewById(R.id.EditLat);
        enter = findViewById(R.id.EnterTheData);
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        enter.setOnClickListener(d -> {
            String dataLat = editLat.getText().toString();
            String dataLon = editLong.getText().toString();

            if (dataLat!=null && dataLat != "" && !dataLat.isEmpty()
                    && dataLon!=null && dataLon != "" && !dataLon.isEmpty()
            ) {
                Intent intent = new Intent(NasaImageryDatabase.this, NasaDisplayImageryDatabase.class);
                intent.putExtra(SEND_LAT, dataLat);
                intent.putExtra(SEND_LON, dataLon);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "You Did Not Enter The Data", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.MainPage:
                Intent mainPage = new Intent(NasaImageryDatabase.this, MainActivity.class);
                startActivity(mainPage);
                break;
            case R.id.FavouriteList:
                Intent intent = new Intent(NasaImageryDatabase.this, DataList.class);
                startActivity(intent);
                break;
            case R.id.SearchImage:
                Intent search = new Intent(NasaImageryDatabase.this, NasaImageryDatabase.class);
                startActivity(search);
                break;
            case R.id.help:
                Toast.makeText(NasaImageryDatabase.this,"This Project Was Made By Batman",Toast.LENGTH_LONG).show();
                break;
            case R.id.BBC:
                Intent bbc = new Intent(NasaImageryDatabase.this, BBCNEWS.class);
                startActivity(bbc);
                break;
            case R.id.GUADRIAN:
                Intent gardian = new Intent(NasaImageryDatabase.this, Guardian.class);
                startActivity(gardian);
                break;
            case R.id.NASAIMAGE:
                Intent nasaLongLat = new Intent(NasaImageryDatabase.this, NasaImageOfTheDay.class);
                startActivity(nasaLongLat);
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
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.MainPage:
                Toast.makeText(NasaImageryDatabase.this,"This Project Was Made By WRAITH",Toast.LENGTH_LONG).show();
                break;
            case R.id.FavouriteList:
                Toast.makeText(NasaImageryDatabase.this,"This Project Was Made By CAUSTIC",Toast.LENGTH_LONG).show();
                break;
            case R.id.SearchImage:
                Toast.makeText(NasaImageryDatabase.this,"This Project Was Made By BATMAN",Toast.LENGTH_LONG).show();
                break;
            case R.id.help:
                Toast.makeText(NasaImageryDatabase.this,"This Project Was Made By EINSTEIN",Toast.LENGTH_LONG).show();
                break;
            case R.id.BBC:
                Toast.makeText(NasaImageryDatabase.this,"This Project Was Made By LUFFY",Toast.LENGTH_LONG).show();

                break;
            case R.id.GUADRIAN:
                Toast.makeText(NasaImageryDatabase.this,"This Project Was Made By NARUTO",Toast.LENGTH_LONG).show();

                break;
            case R.id.NASAIMAGE:
                Toast.makeText(NasaImageryDatabase.this,"This Project Was Made By ONE FOR ALL",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}