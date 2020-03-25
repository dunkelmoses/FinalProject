package com.example.finalproject.NasaImage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalproject.R;

public class FragmentTest extends Fragment {
    private Bundle dataPass;
    Intent intent;
    public FragmentTest() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        intent = getActivity().getIntent();
        View view = inflater.inflate(R.layout.fragment_fragment_test, container, false);
//        dataPass = getArguments();
        TextView textView = view.findViewById(R.id.fragText);
//        int id = dataPass.getInt(ImagesList.ITEM_POSITION);
        String id = intent.getStringExtra(ImagesList.ITEM_POSITION);
        textView.setText("The ID in Database is: " + id);
        return view;
    }
}

