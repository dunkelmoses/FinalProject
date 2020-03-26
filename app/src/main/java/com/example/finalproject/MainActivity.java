package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.example.finalproject.NasaLonLat.NasaImageryDatabase;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton1,imageButton2,imageButton3,imageButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton1 = findViewById(R.id.imageView1);
        imageButton2 = findViewById(R.id.imageView2);
        imageButton3 = findViewById(R.id.imageView3);
        imageButton4 = findViewById(R.id.imageView4);

        imageButton1.setOnClickListener(imageButton1->
        {
            Intent intent = new Intent(this,Guardian.class);
            startActivity(intent);
        });

        imageButton2.setOnClickListener(imageButton1->
        {
            Intent intent = new Intent(this, NasaImageOfTheDay.class);
            startActivity(intent);
        });

        imageButton3.setOnClickListener(imageButton1->
        {
            Intent intent = new Intent(this, NasaImageryDatabase.class);
            startActivity(intent);
        });

        imageButton4.setOnClickListener(imageButton1->
        {
            Intent intent = new Intent(this,BBCNEWS.class);
            startActivity(intent);
        });
    }
}