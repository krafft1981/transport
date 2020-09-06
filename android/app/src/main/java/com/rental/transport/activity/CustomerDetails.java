package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;

public class CustomerDetails extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.customer_details, container,false);

        HorizontalScrollView parkingScrollView = (HorizontalScrollView) root.findViewById(R.id.customer_parking_horizontal_scroll);
        HorizontalScrollView transportScrollView = (HorizontalScrollView) root.findViewById(R.id.customer_transport_horizontal_scroll);

        parkingScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).loadFragment("ParkingDetails");
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
