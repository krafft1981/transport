package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;

public class TransportDetails extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transport_details, container,false);

        Button buttonRent = (Button) root.findViewById(R.id.transport_rent);
        View.OnClickListener oclBtnRent = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).loadFragment("OrderCreate");
            }
        };

        buttonRent.setOnClickListener(oclBtnRent);

        HorizontalScrollView customerScrollView = (HorizontalScrollView) root.findViewById(R.id.customer_transport_horizontal_scroll);

        customerScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((MainActivity)getActivity()).loadFragment("CustomerDetails");
                }

                return false;
            }
        });


        return root;
    }
}
