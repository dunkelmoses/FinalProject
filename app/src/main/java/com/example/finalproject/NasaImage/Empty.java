package com.example.finalproject.NasaImage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.finalproject.R;

public class Empty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        getSupportFragmentManager().beginTransaction().add(R.id.fram,new FragmentTest()).commit();
    }
}
