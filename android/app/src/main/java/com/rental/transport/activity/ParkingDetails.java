package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;

public class ParkingDetails extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.parking_details, container,false);

        HorizontalScrollView imageScrollView = (HorizontalScrollView) root.findViewById(R.id.parking_image_horizontal_scroll);
        HorizontalScrollView accountScrollView = (HorizontalScrollView) root.findViewById(R.id.parking_account_horizontal_scroll);
        HorizontalScrollView transportScrollView = (HorizontalScrollView) root.findViewById(R.id.parking_transport_horizontal_scroll);

        imageScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).loadFragment("FullViewImage");
                }

                return false;
            }
        });

        accountScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).loadFragment("CustomerDetails");
                }

                return false;
            }
        });

        transportScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).loadFragment("TransportDetails");
                }

                return false;
            }
        });

        return root;
    }
}
