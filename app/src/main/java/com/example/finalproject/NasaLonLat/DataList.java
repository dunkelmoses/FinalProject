package com.example.finalproject.NasaLonLat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.finalproject.R;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;

public class DataList extends AppCompatActivity {
    ListView theList;
    SQLiteDatabase db;
    DatabaseNasaImagery Mydb;
    TextView viewDate, viewLon, viewLat;
    ArrayList<ContactLonLat> contactsList = new ArrayList<>();
    MyOwnAdapter adapter;
    Intent intent;
    File file;
    String path, dateString, pathImage;
    Button back;
    public static final String ITEM_POSITION = "POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        theList = findViewById(R.id.the_list);
        back = findViewById(R.id.back);
        Mydb = new DatabaseNasaImagery(this);
        loadDataFromDatabase();

//        intent = getIntent();

        back.setOnClickListener(b->{
            Intent mainPage = new Intent(DataList.this, NasaImageryDatabase.class);
            startActivity(mainPage);
        });
        adapter = new MyOwnAdapter();
        theList.setAdapter(adapter);
        theList.setOnItemClickListener((parent, view, position, id) -> {
            showContact(parent, view, position, id);
        });

        theList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to see fragment?")

                    .setPositiveButton("Yes", (click, arg) -> {
                        Intent intent = new Intent(DataList.this, Empty.class);
                        String s = String.valueOf(id);
                        intent.putExtra(ITEM_POSITION, s);
                        startActivity(intent);
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    //Show the dialog
                    .create().show();

            return true;
        });
    }

    private void loadDataFromDatabase() {
        //get a database connection:
        Mydb = new DatabaseNasaImagery(this);
        db = Mydb.getWritableDatabase();


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {DatabaseNasaImagery.COL_ID, DatabaseNasaImagery.COL_URL
                , DatabaseNasaImagery.COL_LON, DatabaseNasaImagery.COL_LAT};
        //query all the results from the database:
        Cursor results = db.query(false, DatabaseNasaImagery.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int regUrlColumnIndex = results.getColumnIndex(DatabaseNasaImagery.COL_URL);
        int idColIndex = results.getColumnIndex(DatabaseNasaImagery.COL_ID);
        int lonColIndex = results.getColumnIndex(DatabaseNasaImagery.COL_LON);
        int latColIndex = results.getColumnIndex(DatabaseNasaImagery.COL_LAT);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String url = results.getString(regUrlColumnIndex);
            String lon = results.getString(lonColIndex);
            String lat = results.getString(latColIndex);

            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            contactsList.add(new ContactLonLat(id, url, lon, lat));
        }
        //At this point, the contactsList array has loaded every row from the cursor.
    }

    protected class MyOwnAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contactsList.size();
        }

        public ContactLonLat getItem(int position) {
            return contactsList.get(position);
        }

        public View getView(int position, View old, ViewGroup parent) {
            View newView = getLayoutInflater().inflate(R.layout.nasa_image_row, parent, false);

            ContactLonLat thisRow = getItem(position);

            //get the TextViews


            ImageView rowUrl = (ImageView) newView.findViewById(R.id.imageNasa);
            viewDate = (TextView) newView.findViewById(R.id.date);
            viewLon = (TextView) newView.findViewById(R.id.lonRow);
            viewLat = (TextView) newView.findViewById(R.id.latRow);
            viewLon.setText("Longitude : " + thisRow.getLon());
            viewLat.setText("Latitude  : " + thisRow.getLat());

            Picasso.with(DataList.this).load(thisRow.getUrl()).into(rowUrl);
            return newView;
        }

        //last week we returned (long) position. Now we return the object's database id that we get from line 73
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }

    protected void deleteContact(ContactLonLat c) {
        db.delete(DatabaseNasaImagery.TABLE_NAME, DatabaseNasaImagery.COL_ID + "= ?", new String[]{Long.toString(c.getId())});
    }

    protected void showContact(AdapterView parent, View view, int position, long id) {

        file = new File(getPackageName());
        path = "/data/data" + file.getAbsolutePath() + "/app_imageLonLat/";

        ContactLonLat selectedContact = contactsList.get(position);
        View contact_view = getLayoutInflater().inflate(R.layout.activity_empty, null);
        dateString = viewDate.getText().toString();
        pathImage = path + dateString.substring(7) + ".jpg";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You clicked on item " + (position + 1))
                .setMessage("Do you want to delete this picture? ")
                .setView(contact_view) //add the 3 edit texts showing the contact information
                .setNegativeButton("Delete", (click, b) -> {
                    deleteContact(selectedContact); //remove the contact from database
                    contactsList.remove(position); //remove the contact from contact list
                    adapter.notifyDataSetChanged(); //there is one less item so update the list
                    File deleteFile = new File(pathImage);
                    deleteFile.delete();
                })
                .setNeutralButton("Cancel", (click, b) -> {
                })
                .create().show();
    }
}