package com.example.finalproject.NasaImage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class ImagesList extends AppCompatActivity {
    ListView theList;
    SQLiteDatabase db;
    DatabaseNasaImage Mydb;
    TextView textView;
    ArrayList<ContactNasaImages> contactsList = new ArrayList<>();
    MyOwnAdapter adapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_list);
        theList = findViewById(R.id.the_list);
        textView = findViewById(R.id.ttt);
        Mydb = new DatabaseNasaImage(this);
        loadDataFromDatabase();

        adapter = new MyOwnAdapter();
        theList.setAdapter(adapter);
    }

    private void loadDataFromDatabase()
    {
        //get a database connection:
        Mydb = new DatabaseNasaImage(this);
        db = Mydb.getWritableDatabase();


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {DatabaseNasaImage.COL_ID, DatabaseNasaImage.COL_DATE, DatabaseNasaImage.COL_REGURL
        ,DatabaseNasaImage.COL_HDURL};
        //query all the results from the database:
        Cursor results = db.query(false, DatabaseNasaImage.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int dateColumnIndex = results.getColumnIndex(DatabaseNasaImage.COL_DATE);
        int regUrlColumnIndex = results.getColumnIndex(DatabaseNasaImage.COL_REGURL);
        int hdUrlColIndex = results.getColumnIndex(DatabaseNasaImage.COL_HDURL);
        int idColIndex = results.getColumnIndex(DatabaseNasaImage.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String date = results.getString(dateColumnIndex);
            String regUrl = results.getString(regUrlColumnIndex);
            String hdUrl = results.getString(hdUrlColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            contactsList.add(new ContactNasaImages(id, date, regUrl,hdUrl));
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

            //get the TextViews

            ImageView rowUrl = (ImageView) newView.findViewById(R.id.imageNasa);
            TextView textView = (TextView) newView.findViewById(R.id.date);
            textView.setText(thisRow.getDate());

//                Picasso.with(ImagesList.this)
//                        .load(thisRow.getRegUrl())
//                        .centerCrop()
//                        .fit()
//                        .into(rowUrl);
            Picasso.with(ImagesList.this).load(thisRow.getRegUrl()).into(rowUrl);


            //update the text fields:
//            rowUrl.setText("id:" + thisRow.getRegUrl());
//            rowUrl.setImageDrawable(thisRow.getRegUrl());
            //return the row:
            return newView;
        }

        //last week we returned (long) position. Now we return the object's database id that we get from line 73
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }
}