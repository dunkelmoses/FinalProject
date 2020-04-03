package com.example.finalproject.NasaLonLat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;


public class FavList extends Fragment {


    public FavList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav1, container, false);
        ContactLonLat bingImage = (ContactLonLat) getArguments().getSerializable("NASA-IMAGE");

        if(bingImage != null) {
            TextView lonValue = view.findViewById(R.id.longitude);
            lonValue.setText(String.valueOf(bingImage.getLongitude()));

            TextView latValue = view.findViewById(R.id.latitude);
            latValue.setText(String.valueOf(bingImage.getLatitude()));

            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setImageBitmap(NasaDisplayImageryDB.loadImage(view.getContext(), bingImage));
        }


        return view;
    }
}
