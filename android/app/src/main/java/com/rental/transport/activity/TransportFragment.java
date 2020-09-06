package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
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
    private Integer size = 100;

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Toast
                .makeText(getActivity(), "Search menu created", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transport_fragment, container, false);
        View details = inflater.inflate(R.layout.transport_details, container, false);

        GridView grid = (GridView) root.findViewById(R.id.transport_gridview);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Transport element = (Transport)parent.getAdapter().getItem(position);
                TextView type = (TextView) details.findViewById(R.id.transportType);
//                type.setText(element.getType());
/*
                TextView name = (TextView) details.findViewById(R.id.transportName);
                TextView capacity = (TextView) details.findViewById(R.id.transportCapacity);

                type.setText(element.getType() == null ? "Не заполнено" : element.getType());
                name.setText(element.getName() == null ? "Не заполнено" : element.getName());
                capacity.setText(element.getCapacity() == null ? "Не заполнено" : element.getCapacity().toString());
*/
                ((MainActivity) getActivity()).loadFragment("TransportDetails");
            }
        });

        ProgressBar progress = (ProgressBar) getActivity().findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        NetworkService
                .getInstance()
                .getTransportApi()
                .doGetTransportList(page, size)
                .enqueue(new Callback<List<Transport>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Transport>> call, @NonNull Response<List<Transport>> response) {

                        progress.setVisibility(View.GONE);

                        List<Transport> info = response.body();
                        if (info != null) {
                            TransportGridAdapter adapter = new TransportGridAdapter(getActivity(), info);
                            grid.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Transport>> call, @NonNull Throwable t) {

                        progress.setVisibility(View.GONE);

                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });

        SearchView search = (SearchView)  root.findViewById(R.id.search_transport);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do your search
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (newText.isEmpty()) clearSearch();
                return false;
            }
        });

        return root;
    }
}
