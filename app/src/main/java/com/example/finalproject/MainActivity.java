package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton1 = findViewById(R.id.imageView1);


        imageButton1.setOnClickListener(imageButton1->
        {
            Intent intent = new Intent(this,Guardian.GuardianMainActivity.class);
            startActivity(intent);
        });



    }
}
