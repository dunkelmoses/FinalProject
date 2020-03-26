package com.example.finalproject.NasaLonLat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.finalproject.R;

public class Empty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blank);
        getSupportFragmentManager().beginTransaction().add(R.id.fram, new BlankFragment()).commit();
    }
}