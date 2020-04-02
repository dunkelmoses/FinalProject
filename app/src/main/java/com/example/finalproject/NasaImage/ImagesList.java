package com.example.finalproject.NasaImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.BBCNEWS;
import com.example.finalproject.Guardian;
import com.example.finalproject.MainActivity;
import com.example.finalproject.NasaDatabase;
import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ImagesList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ListView theList;
    private SQLiteDatabase db;
    private DatabaseNasaImage mydb;
    private TextView viewDate,viewMessage;
    private ArrayList<ContactNasaImages> contactsList = new ArrayList<>();
    private MyOwnAdapter adapter;
    private String date;
    private Intent intent;
    private Cursor results;
    public static final String ITEM_POSITION = "POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_list);
        theList = findViewById(R.id.the_list);
        mydb = new DatabaseNasaImage(this);
        loadDataFromDatabase();

        intent = getIntent();
        date = intent.getStringExtra("date");

        adapter = new MyOwnAdapter();
        theList.setAdapter(adapter);
        theList.setOnItemClickListener(( parent,  view,  position,  id) -> {
            showContact( parent,  view,  position,  id );
        });
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

    private void loadDataFromDatabase()
    {
        //get a database connection:
        mydb = new DatabaseNasaImage(this);
        db = mydb.getWritableDatabase();


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {DatabaseNasaImage.COL_ID, DatabaseNasaImage.COL_DATE, DatabaseNasaImage.COL_REGURL
        ,DatabaseNasaImage.COL_HDURL,DatabaseNasaImage.COL_MESG};
        //query all the results from the database:
        results = db.query(false, DatabaseNasaImage.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int dateColumnIndex = results.getColumnIndex(DatabaseNasaImage.COL_DATE);
        int messageColumnIndex = results.getColumnIndex(DatabaseNasaImage.COL_MESG);
        int regUrlColumnIndex = results.getColumnIndex(DatabaseNasaImage.COL_REGURL);
        int hdUrlColIndex = results.getColumnIndex(DatabaseNasaImage.COL_HDURL);
        int idColIndex = results.getColumnIndex(DatabaseNasaImage.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String date = results.getString(dateColumnIndex);
            String message = results.getString(messageColumnIndex);
            String regUrl = results.getString(regUrlColumnIndex);
            String hdUrl = results.getString(hdUrlColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            contactsList.add(new ContactNasaImages(id, date, regUrl,hdUrl,message));
        }
        //At this point, the contactsList array has loaded every row from the cursor.
    }
    protected class MyOwnAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contactsList.size();
        }

        public ContactNasaImages getItem(int position) {
            return contactsList.get(position);
        }

        public View getView(int position, View old, ViewGroup parent) {
            View newView = getLayoutInflater().inflate(R.layout.nasa_image_row, parent, false);

            ContactNasaImages thisRow = getItem(position);

            ImageView rowUrl = (ImageView) newView.findViewById(R.id.imageNasa);
            viewDate = (TextView) newView.findViewById(R.id.date);
            viewDate.setText(thisRow.getDate());
            viewMessage = (TextView) newView.findViewById(R.id.messages);
            viewMessage.setText(thisRow.getMessage());
            Picasso.with(ImagesList.this).load(thisRow.getRegUrl()).into(rowUrl);
            return newView;
        }

        //last week we returned (long) position. Now we return the object's database id that we get from line 73
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }

    protected void deleteContact(ContactNasaImages c)
    {
        db.delete(DatabaseNasaImage.TABLE_NAME, DatabaseNasaImage.COL_ID + "= ?", new String[] {Long.toString(c.getId())});

    }
    protected  void showContact(AdapterView parent, View view, int position, long id){

        File file = new File(getPackageName());
        String x = "/data/data"+file.getAbsolutePath()+"/app_image/";
        ContactNasaImages selectedContact = contactsList.get(position);
        View contact_view = getLayoutInflater().inflate(R.layout.nasa_image_edit, null);
        String pathDeleteImage = x + viewDate.getText().toString()+".jpg";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You clicked on item " + (position+1))
                .setMessage("Would you like to delete this picture?")
                .setView(contact_view) //add the 3 edit texts showing the contact information
                .setNegativeButton("Delete", (click, b) -> {
                    deleteContact(selectedContact); //remove the contact from database
                    contactsList.remove(position); //remove the contact from contact list
                    adapter.notifyDataSetChanged(); //there is one less item so update the list
                    File deleteFile = new File(pathDeleteImage);
                    deleteFile.delete();
                })
                .setPositiveButton("Fragment",(click, b)->{
                    Intent intent = new Intent(ImagesList.this,Empty.class);
                    String s = String.valueOf(id);
                    intent.putExtra(ITEM_POSITION,s);
                    startActivity(intent);

                })
                .setNeutralButton("dismiss", (click, b) -> { })
                .create().show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.MainPage:
                Intent mainPage = new Intent(ImagesList.this, MainActivity.class);
                startActivity(mainPage);
                break;
            case R.id.FavouriteList:
                Intent intent = new Intent(ImagesList.this,ImagesList.class);
                startActivity(intent);
                break;
            case R.id.SearchImage:
                Intent search = new Intent(ImagesList.this,NasaImageOfTheDay.class);
                startActivity(search);
                break;
            case R.id.help:
                Toast.makeText(ImagesList.this,"This Project Was Made By Batman",Toast.LENGTH_LONG).show();
                break;
            case R.id.BBC:
                Intent bbc = new Intent(ImagesList.this, BBCNEWS.class);
                startActivity(bbc);
                break;
            case R.id.GUADRIAN:
                Intent gardian = new Intent(ImagesList.this, Guardian.class);
                startActivity(gardian);
                break;
            case R.id.NASALANGLAT:
                Intent nasaLongLat = new Intent(ImagesList.this, NasaDatabase.class);
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
                Toast.makeText(ImagesList.this,"This Project Was Made By WRAITH",Toast.LENGTH_LONG).show();
                break;
            case R.id.FavouriteList:
                Toast.makeText(ImagesList.this,"This Project Was Made By CAUSTIC",Toast.LENGTH_LONG).show();
                break;
            case R.id.SearchImage:
                Toast.makeText(ImagesList.this,"This Project Was Made By BATMAN",Toast.LENGTH_LONG).show();

            case R.id.help:
                Toast.makeText(ImagesList.this,"This Project Was Made By EINSTEIN",Toast.LENGTH_LONG).show();
                break;
            case R.id.BBC:
                Toast.makeText(ImagesList.this,"This Project Was Made By LUFFY",Toast.LENGTH_LONG).show();

                break;
            case R.id.GUADRIAN:
                Toast.makeText(ImagesList.this,"This Project Was Made By NARUTO",Toast.LENGTH_LONG).show();

                break;
            case R.id.NASALANGLAT:
                Toast.makeText(ImagesList.this,"This Project Was Made By ONE FOR ALL",Toast.LENGTH_LONG).show();
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