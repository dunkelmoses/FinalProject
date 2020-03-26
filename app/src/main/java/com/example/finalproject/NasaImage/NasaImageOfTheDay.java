package com.example.finalproject.NasaImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.BBCNEWS;
import com.example.finalproject.Guardian;
import com.example.finalproject.MainActivity;
import com.example.finalproject.NasaDatabase;
import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class NasaImageOfTheDay extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private TextView showDate;
    private String date;
    private Button enterDate;
    private Button clickHere;
    private Intent intent;
    private DatePickerDialog datePickerDialog;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_of_the_day);
        showDate = findViewById(R.id.DateTextView);
        enterDate = findViewById(R.id.EnterTheDate);
        clickHere = findViewById(R.id.ClickToSee);

        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = prefs.getString("date", "reserve not found");
        TextView typeField = findViewById(R.id.DateTextView);
        typeField.setText(savedString);

        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        enterDate.setOnClickListener(d -> {

            datePickerDialog = new DatePickerDialog(
                    this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            date = year + "-" + month + "-" + dayOfMonth;
                            showDate.setText("The date you entered is : " + date);
                            saveSharedPrefs("The date you entered last time was "+ date);
                        }
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });

            clickHere.setOnClickListener(c -> {
                if (date!=null) {
                    intent = new Intent(NasaImageOfTheDay.this, ShowNasaImage.class);
                    intent.putExtra("date", date);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "You Must Enter a Date first", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.MainPage:
                Intent mainPage = new Intent(NasaImageOfTheDay.this, MainActivity.class);
                startActivity(mainPage);
                break;
            case R.id.FavouriteList:
                Intent intent = new Intent(NasaImageOfTheDay.this,ImagesList.class);
                startActivity(intent);
                break;
            case R.id.SearchImage:
                Intent search = new Intent(NasaImageOfTheDay.this,NasaImageOfTheDay.class);
                startActivity(search);
                break;
            case R.id.help:
                Toast.makeText(NasaImageOfTheDay.this,"This Project Was Made By Batman",Toast.LENGTH_LONG).show();
                break;
            case R.id.BBC:
                Intent bbc = new Intent(NasaImageOfTheDay.this, BBCNEWS.class);
                startActivity(bbc);
                break;
            case R.id.GUADRIAN:
                Intent gardian = new Intent(NasaImageOfTheDay.this, Guardian.class);
                startActivity(gardian);
                break;
            case R.id.NASALANGLAT:
                Intent nasaLongLat = new Intent(NasaImageOfTheDay.this, NasaDatabase.class);
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
                Toast.makeText(NasaImageOfTheDay.this,"This Project Was Made By WRAITH",Toast.LENGTH_LONG).show();
                break;
            case R.id.FavouriteList:
                Toast.makeText(NasaImageOfTheDay.this,"This Project Was Made By CAUSTIC",Toast.LENGTH_LONG).show();
                break;
            case R.id.SearchImage:
                Toast.makeText(NasaImageOfTheDay.this,"This Project Was Made By BATMAN",Toast.LENGTH_LONG).show();
                break;
            case R.id.help:
                Toast.makeText(NasaImageOfTheDay.this,"This Project Was Made By EINSTEIN",Toast.LENGTH_LONG).show();
                break;
            case R.id.BBC:
                Toast.makeText(NasaImageOfTheDay.this,"This Project Was Made By LUFFY",Toast.LENGTH_LONG).show();

                break;
            case R.id.GUADRIAN:
                Toast.makeText(NasaImageOfTheDay.this,"This Project Was Made By NARUTO",Toast.LENGTH_LONG).show();

                break;
            case R.id.NASALANGLAT:
                Toast.makeText(NasaImageOfTheDay.this,"This Project Was Made By ONE FOR ALL",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);

        return true;
    }
    private void saveSharedPrefs(String stringToSave)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("date", stringToSave);
        editor.commit();
    }
}
