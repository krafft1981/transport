package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;

public class OrderDetails extends Fragment {

    private Integer page = 0;
    private Integer size = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.order_details, container,false);

        ProgressBar pb = (ProgressBar) getActivity().findViewById(R.id.progress);
        pb.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);

        return root;
    }
}
