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
        clickHere.setOnClickListener(c->{
            intent = new Intent(NasaImageOfTheDay.this, ShowNasaImage.class);
            intent.putExtra("date", date);
            startActivity(intent);
        });

    }


}
