package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class NasaImageOfTheDay extends AppCompatActivity {

    private TextView urlImage;
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
        urlImage = findViewById(R.id.urlImage);

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
            CallJson callJson = new CallJson();
            callJson.execute("https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=2020-02-21");
            datePickerDialog.show();
        });

    }

    public class CallJson extends AsyncTask<String, Integer, String> {

        String imageURL;


        @Override
        protected String doInBackground(String... strings) {
            try {
                URL UVurl = new URL(strings[0]);
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                InputStream inStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                JSONObject jObject = new JSONObject(result);
//                imageURL = String.valueOf(jObject.getDouble("url"));
                imageURL = jObject.getString("url");
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            intent = new Intent(NasaImageOfTheDay.this, ShowNasaImage.class);

            clickHere.setOnClickListener(e -> {
                // String passDate = showDate.getText().toString();
                intent.putExtra("date", date);
                intent.putExtra("IMAGE_URL", imageURL);
                startActivity(intent);

            });
            urlImage.setText(imageURL);

        }
    }
}
