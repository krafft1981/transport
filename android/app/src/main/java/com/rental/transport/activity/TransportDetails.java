package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Transport;

import lombok.Setter;

public class TransportDetails extends Fragment {

    @Setter
    private Transport transport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transport_details, container,false);
        TextView type = (TextView) root.findViewById(R.id.transport_type);
        type.setText(transport.getType());

        TextView name = (TextView) root.findViewById(R.id.transport_name);
        name.setText(transport.getName());

        TextView capacity = (TextView) root.findViewById(R.id.transport_capacity);
        capacity.setText(transport.getCapacity().toString());

//        HorizontalScrollView customerScrollView = (HorizontalScrollView) root.findViewById(R.id.customer_transport_horizontal_scroll);
/*
            customerScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        ((MainActivity) getActivity()).loadFragment("CustomerDetails");
                    }

                    return false;
                }
            });
*/

        if (transport.getImages() != null && transport.getImages().isEmpty()) {
            LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.transport_images);
            ImageView iv = new ImageView (getContext());
            iv.setBackgroundResource(R.drawable.samokat);
            linearLayout.addView(iv);
        }
        else {
            for (Long imageId : transport.getImages()) {

            }
        }

        Button buttonRent = (Button) root.findViewById(R.id.transport_button_rent);
        View.OnClickListener oclBtnRent = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment("OrderCreate");
            }
        };
        buttonRent.setOnClickListener(oclBtnRent);

        return root;
    }
}
