package com.example.finalproject.bbc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.finalproject.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LatestNewsFragment extends Fragment {

   public static BBCNewsAdapter BNadapter;
    public static List<BBCNewsItem> newsItems;
    public static ProgressBar progressBar;
    public static ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbc_latest_news, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        listView = view.findViewById(R.id.listViewLatestNews);
        newsItems = new ArrayList<>();
        BNadapter = new BBCNewsAdapter(getActivity(), newsItems);
        listView.setAdapter(BNadapter);
        new LatestNewsAsync().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BBCDetailActivity.class);
                BBCNewsItem newsItem = newsItems.get(position);
                saveToSharedPreferences(newsItem);
                intent.putExtra("bbc_news_data", newsItem);
                startActivity(intent);
            }
        });
        return view;
    }

    void saveToSharedPreferences(BBCNewsItem newsItem){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("bbc_sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String link = newsItem.getLink();
        String title = newsItem.getTitle();
        String desc = newsItem.getDescription();
        String pubDate = newsItem.getPubDate();
        editor.putString("link",link);
        editor.putString("title",title);
        editor.putString("desc",desc);
        editor.putString("pubDate",pubDate);
        editor.apply();
    }


}
