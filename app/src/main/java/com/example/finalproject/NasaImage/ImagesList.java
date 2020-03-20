package com.example.finalproject.NasaImage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    TextView viewDate;
    ArrayList<ContactNasaImages> contactsList = new ArrayList<>();
    MyOwnAdapter adapter;
    Context context;
    String date;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_list);
        theList = findViewById(R.id.the_list);
        textView = findViewById(R.id.ttt);
        Mydb = new DatabaseNasaImage(this);
        loadDataFromDatabase();

        intent = getIntent();
        date = intent.getStringExtra("date");

        adapter = new MyOwnAdapter();
        theList.setAdapter(adapter);
        theList.setOnItemClickListener(( parent,  view,  position,  id) -> {
            showContact( position );
        });
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
            viewDate = (TextView) newView.findViewById(R.id.date);
            viewDate.setText(thisRow.getDate());
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
    protected  void showContact(int position){

        File file = new File(getPackageName());
        String x = "/data/data"+file.getAbsolutePath()+"/app_image/";
        ContactNasaImages selectedContact = contactsList.get(position);
        View contact_view = getLayoutInflater().inflate(R.layout.nasa_image_edit, null);
        String pathDeleteImage = x + viewDate.getText().toString()+".jpg";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You clicked on item " + (position+1))
                .setMessage("Would you like to delete the picture?" + pathDeleteImage)
                .setView(contact_view) //add the 3 edit texts showing the contact information
//                .setPositiveButton("Update", (click, b) -> {
////                    selectedContact.update(rowName.getText().toString(), rowEmail.getText().toString());
////                    updateContact(selectedContact);
//                    adapter.notifyDataSetChanged(); //the email and name have changed so rebuild the list
//                })
                .setNegativeButton("Delete", (click, b) -> {
                    deleteContact(selectedContact); //remove the contact from database
                    contactsList.remove(position); //remove the contact from contact list
                    adapter.notifyDataSetChanged(); //there is one less item so update the list
                    File deleteFile = new File(pathDeleteImage);
                    deleteFile.delete();
                })
                .setNeutralButton("dismiss", (click, b) -> { })
                .create().show();

    }
}