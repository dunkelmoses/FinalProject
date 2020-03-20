package com.example.finalproject.NasaImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_of_the_day);
        //findUrlImage = findViewById(R.id.findUrlImage);
        showDate = findViewById(R.id.DateTextView);
        enterDate = findViewById(R.id.EnterTheDate);
        clickHere = findViewById(R.id.ClickToSee);

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
                break;
            case R.id.FavouriteList:
                Intent intent = new Intent(NasaImageOfTheDay.this,ImagesList.class);
                startActivity(intent);
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
}
