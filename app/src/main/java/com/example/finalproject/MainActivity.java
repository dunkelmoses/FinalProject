package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.finalproject.NasaLonLat.NasaImageryDatabase;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton3 = findViewById(R.id.imageView3);


        imageButton3.setOnClickListener(imageButton1->
        {
            Intent intent = new Intent(this, NasaImageryDatabase.class);
            startActivity(intent);
        });

    }
}