package com.example.finalproject.bbc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.finalproject.Guardian.GuardianMainActivity;
import com.example.finalproject.NasaDatabase;
import com.example.finalproject.NasaImageOfTheDay;
import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;

public class BBCMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private Toolbar bar;
    private LatestNewsFragment latestNewsFrgmt;
    private NavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbcmain);

        drawer = findViewById(R.id.drawer);
        nav = findViewById(R.id.nav);

        //set toolbar as actionbar
        bar = findViewById(R.id.toolbar);
        bar.setTitle(R.string.button_text_BBC);
        setSupportActionBar(bar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, bar,
                R.string.bbc_drawer_open, R.string.bbc_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        //syncing state of action bar icon to open and close drawer
        actionBarDrawerToggle.syncState();

        //showing latest news fragment first as the activity gets started
        latestNewsFrgmt = new LatestNewsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                latestNewsFrgmt).commit();
        nav.setCheckedItem(R.id.drawerBBCLatestNews);
        nav.setNavigationItemSelectedListener(this);
        readSharedPreferences();
    }

    /**
     * Reads shared preferences
     * and starts the BBCDetailNewsActivity with the saved record in the
     * SharedPreferences
     * */
    void readSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("bbc_sp", Context.MODE_PRIVATE);
        String link = sharedPreferences.getString("link","");
        String title = sharedPreferences.getString("title","");
        String desc = sharedPreferences.getString("desc","");
        String pubDate = sharedPreferences.getString("pubDate","");
        if(!link.isEmpty() && !title.isEmpty() && !desc.isEmpty() && !pubDate.isEmpty()){
            BBCNewsItem newsItem= new BBCNewsItem();
            newsItem.setTitle(title);
            newsItem.setDescription(desc);
            newsItem.setPubDate(pubDate);
            newsItem.setLink(link);
            Intent intent = new Intent(this, BBCDetailActivity.class);
            intent.putExtra("bbc_news_data", newsItem);
            intent.putExtra("from_saved", "yes");
            startActivity(intent);
        }
    }

    /**
     * Handling click events of navigation drawer
     * */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawerBBCLatestNews:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        latestNewsFrgmt).commit();
                break;
            case R.id.drawerBBCSavedNews:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,
                        new SavedNewsFragment()).commit();
                break;
            case R.id.drawerMenuGuardian:
                Intent intent1 = new Intent(BBCMainActivity.this, GuardianMainActivity.class);
                startActivity(intent1);
                break;
            case R.id.drawerMenuNasaImage:
                Intent intent2 = new Intent(BBCMainActivity.this, NasaImageOfTheDay.class);
                startActivity(intent2);
                break;
            case R.id.drawerMenuNasaEarth:
                Intent intent3 = new Intent(BBCMainActivity.this, NasaDatabase.class);
                startActivity(intent3);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Handling click events of toolbar menu
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuGuardian:
                Intent intent3 = new Intent(this, GuardianMainActivity.class);
                startActivity(intent3);
                return true;
            case R.id.menuImage:
                Intent intent1 = new Intent(this, NasaImageOfTheDay.class);
                startActivity(intent1);
                return true;
            case R.id.menuEarth:
                Intent intent2 = new Intent(this, NasaDatabase.class);
                startActivity(intent2);
                return true;
            case R.id.menuHelp:
                viewHelpDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Shows help Dialog.
     * */
    private void viewHelpDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BBCMainActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.help));
        alertDialogBuilder.setMessage(getString(R.string.bbc_help));
        alertDialogBuilder.setPositiveButton(getString(R.string.bbc_got_it), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    /**
     * Inflating menu with the toolbar
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bbc_toolbar_menu, menu);
        return true;
    }

    /***
     * Closes the navigation drawer before finishing the activity.
     * */
    @Override
    public void onBackPressed() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
