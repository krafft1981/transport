package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.TransportGridAdapter;
import com.rental.transport.model.Transport;
import com.rental.transport.service.NetworkService;

import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportFragment extends Fragment {

    private Integer page = 0;
    private Integer size = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transport_fragment, container,false);
        GridView grid = (GridView) root.findViewById(R.id.transport_gridview);

        NetworkService
                .getInstance()
                .getTransportApi()
                .doGetTransportList(page, size)
                .enqueue(new Callback<List<Transport>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Transport>> call, @NonNull Response<List<Transport>> response) {
                        List<Transport> data = response.body();
                        if (data != null) {
                            grid.setAdapter(new TransportGridAdapter(getActivity(), data));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Transport>> call, @NonNull Throwable t) {
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Transport transport = (Transport)parent.getAdapter().getItem(position);

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new TransportDetails())
                        .commit();
            }
        });

        grid.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

//                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
//                    if (!isLoading && hasMoreItems && (lastVisibleItem == totalItemCount)) {
//                        isLoading = true;
//                    }
//                }

//                Toast
//                        .makeText(getActivity(), firstVisibleItem + " " + visibleItemCount + " " + totalItemCount, Toast.LENGTH_SHORT)
//                        .show();
            }
        });

        return root;
    }
}
