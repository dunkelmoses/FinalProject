package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton guardianButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guardianButton = findViewById(R.id.imageView1);


        guardianButton.setOnClickListener(imageButton1->
        {
            Intent intent = new Intent(this,Guardian.GuardianMainActivity.class);
            startActivity(intent);
        });



    }
}
