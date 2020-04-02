package com.example.finalproject.NasaLonLat;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalproject.R;

public class BlankFragment extends Fragment {
    Intent intent;

    public BlankFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        intent = getActivity().getIntent();
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        TextView textView = view.findViewById(R.id.fragText);
        String id = intent.getStringExtra(DataList.ITEM_POSITION);
        textView.setText("Database ID : " + id);
        return view;
    }
}