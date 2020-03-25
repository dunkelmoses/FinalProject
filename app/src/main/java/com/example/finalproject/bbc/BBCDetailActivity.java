package com.example.finalproject.bbc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import com.example.finalproject.R;
import com.google.android.material.snackbar.Snackbar;

public class BBCDetailActivity extends AppCompatActivity {

    TextView text, desc, link, date;
    Button saveBtn, deleteBtn;
    CoordinatorLayout coordinatorLayout;
    BBCDatabase bbcDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbc_detail);
        Intent intent = getIntent();

        /**
         * checking if the activity is called from shared preferences data
         * (showing toast if it is)
         */
        if(intent.hasExtra("from_saved")){
            String from_saved = intent.getStringExtra("from_saved");
            if (from_saved != null && from_saved.equals("yes")) {
                Toast.makeText(this, getString(R.string.bbc_toast_last_viewed), Toast.LENGTH_LONG).show();
            }
        }
        /**
         * Reading intent value to show news details
         * */
        final BBCNewsItem bbcNewsItem = (BBCNewsItem) intent.getSerializableExtra("bbc_news_data");
        String title = bbcNewsItem.getTitle();
        String description = bbcNewsItem.getDescription();
        final String link = bbcNewsItem.getLink();
        String pubDate = bbcNewsItem.getPubDate();

        bbcDatabase = new BBCDatabase(this);

        text = findViewById(R.id.textViewTitle);
        desc = findViewById(R.id.textViewDesc);
        this.link = findViewById(R.id.textViewLink);
        date = findViewById(R.id.textViewPubDate);
        saveBtn = findViewById(R.id.buttonSave);
        deleteBtn = findViewById(R.id.buttonDelete);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        text.setText(title);
        desc.setText(description);
        this.link.setText(link);
        date.setText(pubDate);

        /**
         * if the record already exists, save button will not be visible.
         * otherwise delete button will not be visible.
         * */
        if(bbcDatabase.exists(link)){
            saveBtn.setVisibility(View.GONE);
        }else{
            deleteBtn.setVisibility(View.GONE);
        }

        /**
         * Saving the record to the database
         * */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bbcDatabase.saveNews(bbcNewsItem);
                saveBtn.setVisibility(View.GONE);
                deleteBtn.setVisibility(View.VISIBLE);

                //Showing snackbar
                Snackbar.make(coordinatorLayout, getString(R.string.bbc_snack_saved), Snackbar.LENGTH_SHORT).show();
            }
        });

        /**
         * deleting the record from the database
         * */
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bbcDatabase.deleteNews(link);
                deleteBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);

                //Showing snackbar
                Snackbar.make(coordinatorLayout, getString(R.string.bbc_snack_deleted), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
