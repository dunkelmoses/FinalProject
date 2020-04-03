package com.example.finalproject.NasaLonLat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalproject.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NasaDisplayImageryDB extends AppCompatActivity {

    private ArrayList<ContactLonLat> bingImages = new ArrayList<>();
    private myAdapter favAdapter;
    private FavList favList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bing_images);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.toolbar_title);
            getSupportActionBar().setSubtitle(R.string.toolbar_subtitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        favList();

        favAdapter = new myAdapter(this, R.layout.nasa_earth_image_row, bingImages);
        Log.e("onCreate", String.valueOf(bingImages.size()));

        ListView listView = findViewById(R.id.favList);
        listView.setAdapter(favAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putSerializable("BING-IMAGE", bingImages.get(i));

            boolean isLandScape = findViewById(R.id.frameLayout) != null;
            if (isLandScape) {
                favList = new FavList();
                favList.setArguments(dataToPass);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, favList)
                        .commit();
            } else {
                Intent intent = new Intent(NasaDisplayImageryDB.this, DataList.class);
                intent.putExtras(dataToPass);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(NasaDisplayImageryDB.this);
            builder.setTitle(R.string.remove_fav)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        BingDatabaseHelper bingDatabaseHelper = new BingDatabaseHelper(view.getContext());
                        bingDatabaseHelper.deleteData(bingImages.get(i));
                        bingDatabaseHelper.close();
                        deleteData(bingImages.get(i));
                        bingImages.remove(i);
                        favAdapter.notifyDataSetChanged();
                        if (findViewById(R.id.frameLayout) != null)
                            getSupportFragmentManager().beginTransaction().remove(favList).commit();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
    }


    private void deleteData(ContactLonLat nasaEarthImage) {
        File file = new File(getFilesDir(), nasaEarthImage.getPath());
        boolean result = file.delete();
        Toast.makeText(this, getString(R.string.file_delete) + " " + result, Toast.LENGTH_LONG).show();
    }


    private void favList() {
        BingDatabaseHelper bingDatabaseHelper = new BingDatabaseHelper(this);

        Cursor cursor = bingDatabaseHelper.getAll();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            ContactLonLat nasaEarthImage = new ContactLonLat(
                    cursor.getLong(cursor.getColumnIndex(BingDatabaseHelper.COL_ID)),
                    cursor.getDouble(cursor.getColumnIndex(BingDatabaseHelper.COL_LAT)),
                    cursor.getDouble(cursor.getColumnIndex(BingDatabaseHelper.COL_LON)),
                    cursor.getString(cursor.getColumnIndex(BingDatabaseHelper.COL_URL)));
            Log.e("NasaImageFavorites", nasaEarthImage.getPath());
            Log.e("NasaImageFavorites", String.valueOf(nasaEarthImage.getId()));
            Log.e("NasaImageFavorites", String.valueOf(nasaEarthImage.getLatitude()));
            bingImages.add(nasaEarthImage);
            cursor.moveToNext();
        }
        cursor.close();

        bingDatabaseHelper.close();
    }


    public static Bitmap loadImage(Context context, ContactLonLat nasaEarthImage) {
        if (nasaEarthImage.getPath() == null) {
            return null;
        }

        try (FileInputStream fis = context.openFileInput(nasaEarthImage.getPath())) {
            return BitmapFactory.decodeStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static class myAdapter extends ArrayAdapter<ContactLonLat> {


        myAdapter(@NonNull Context context, int resource, List<ContactLonLat> list) {
            super(context, resource, list);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = convertView;

            if (rowView == null && layoutInflater != null) {
                rowView = layoutInflater.inflate(R.layout.nasa_earth_image_row, parent, false);
            }

            assert rowView != null;
            TextView longitudeTextView = rowView.findViewById(R.id.longitude);
            TextView latitudeTextView = rowView.findViewById(R.id.latitude);
            ImageView imageView = rowView.findViewById(R.id.imageView);

            ContactLonLat bingImage = getItem(position);
            Log.e("myAdapter", String.valueOf(bingImage));
            if (bingImage != null) {
                longitudeTextView.setText(String.valueOf(bingImage.getLongitude()));
                latitudeTextView.setText(String.valueOf(bingImage.getLatitude()));
                imageView.setImageBitmap(loadImage(rowView.getContext(), bingImage));
            }

            return rowView;
        }
    }
}
