package com.example.finalproject.NasaLonLat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class NasaImageActivity extends AppCompatActivity {
    private static final String API_KEY = "AhiAkUp5OJNThWRDqdFnxmsd2SCgIwaFn_k9Q2UEroZ69tQxE6zV1rY5klvlJLne";
    private ImageView imageView;
    private ProgressBar progressBar;
    private ContactLonLat bingImage = new ContactLonLat();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.toolbar_title);
            getSupportActionBar().setSubtitle(R.string.toolbar_subtitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageView = findViewById(R.id.bing_picture);
        progressBar = findViewById(R.id.progressBar);




        //Button favoritesButton = findViewById(R.id.addToFavList);
        //favoritesButton.setOnClickListener(this::addToFavorites);
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            try {
                String longitude = ((EditText) findViewById(R.id.EditLon)).getText().toString();
                String latitude = ((EditText) findViewById(R.id.EditLat)).getText().toString();
                bingImage.setLatitude(Double.parseDouble(latitude));
                bingImage.setLongitude(Double.parseDouble(longitude));
                NasaImageQuery nasaImageQuery = new NasaImageQuery();
                nasaImageQuery.execute(String.format(" http://dev.virtualearth.net/REST/V1/Imagery/Map/Birdseye/%s,%s/20?dir=180&ms=500,500&key="+API_KEY,
                        latitude, longitude));
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.bad_input, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void addToFavorites(View v) {
        if (bingImage.getPath() == null) {
            Snackbar.make(v, R.string.null_image, Snackbar.LENGTH_LONG).show();
            return;
        }

        BingDatabaseHelper bingDatabaseHelper = new BingDatabaseHelper(v.getContext());
        long id = bingDatabaseHelper.insertData(bingImage);
        bingImage.setId(id);
        bingImage.setPath(id + ".jpg");
        bingDatabaseHelper.update(bingImage);
        bingDatabaseHelper.close();

        try (FileOutputStream out = openFileOutput(bingImage.getPath(), Context.MODE_PRIVATE)) {
            ((BitmapDrawable) imageView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out);
            Snackbar.make(v, R.string.addedToFav, Snackbar.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bing_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.actionHelp:
                SharedPreferences sharedPreferences = this.getSharedPreferences("Final Project Winter 2020", MODE_PRIVATE);
                int timesOpened = sharedPreferences.getInt("Times-Opened", 0) + 1;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Times-Opened", timesOpened);
                editor.apply();

                AlertDialog.Builder builder = new AlertDialog.Builder(NasaImageActivity.this);
                builder.setTitle(R.string.bing_help)
                        .setMessage(getString(R.string.info_message1) +
                                "\n" + getString(R.string.info_message2) + " " + timesOpened + " " + getString(R.string.info_message3))
                        .setPositiveButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.actionFavorites:
                startActivity(new Intent(this, NasaDisplayImageryDB.class));
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    @SuppressLint("StaticFieldLeak")
    private class NasaImageQuery extends AsyncTask<String, Integer, String> {

        private Bitmap image;
        private String errorMessage = null;

        @Override
        protected String doInBackground(String... strings) {
            try {
                publishProgress(0);

                image = null;
                URL imageUrl = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                }
                bingImage.setPath("imageFound");

                publishProgress(100);
            } catch (IOException | NullPointerException e) {
                Log.e("NasaImgSelectorActivity", Objects.requireNonNull(e.getLocalizedMessage()));
            }

            return getString(R.string.finished);
        }

        @Override
        protected void onPostExecute(String s) {
            if (errorMessage == null && image != null) {
                imageView.setImageBitmap(image);
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.image_holder));
                Toast.makeText(NasaImageActivity.this, R.string.null_image, Toast.LENGTH_LONG).show();
            }

            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }
    }
}
