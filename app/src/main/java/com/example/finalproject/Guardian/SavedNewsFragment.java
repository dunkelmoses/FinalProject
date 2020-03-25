package com.example.finalproject.Guardian;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


import com.example.finalproject.R;

import java.util.List;

public class SavedNewsFragment extends Fragment {


    public SavedNewsFragment() {

    }


    private List<Article> articleList;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_news, container, false);
        listView = view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GuardianDetailActivity.class);
                intent.putExtra("article", articleList.get(position));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.i("TAG","onResume called");
        //Read the data from the database
        GuardianDB guardianDB = new GuardianDB(getActivity());

        articleList = guardianDB.getAllSavedArticle();
        ListArticleAdapter listArticleAdapter = new ListArticleAdapter(getActivity(), articleList);
        listView.setAdapter(listArticleAdapter);
    }
}
