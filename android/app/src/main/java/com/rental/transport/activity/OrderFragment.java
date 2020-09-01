package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.OrderGridAdapter;
import com.rental.transport.model.Order;
import com.rental.transport.service.NetworkService;

import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private Integer page = 0;
    private Integer size = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.order_fragment, container,false);
        GridView grid = (GridView) root.findViewById(R.id.order_gridview);

        NetworkService
                .getInstance()
                .getOrderApi()
                .doGetOrderList(page, size)
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {

                        List<Order> data = response.body();
                        if (data != null) {
                            grid.setAdapter(new OrderGridAdapter(getActivity(), data));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Order element = (Order)parent.getAdapter().getItem(position);
                Toast
                        .makeText(getActivity(), element.toString(), Toast.LENGTH_LONG)
                        .show();

                View details = inflater.inflate(R.layout.order_details, container,false);

                ((MainActivity)getActivity()).loadFragment("OrderDetails");
            }
        });

        return root;
    }
}
