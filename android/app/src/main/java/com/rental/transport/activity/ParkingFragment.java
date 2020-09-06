package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.ParkingGridAdapter;
import com.rental.transport.model.Parking;
import com.rental.transport.service.NetworkService;

import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingFragment extends Fragment {

    private Integer page = 0;
    private Integer size = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.parking_fragment, container,false);
        GridView grid = (GridView) root.findViewById(R.id.parking_gridview);

        ProgressBar pb = (ProgressBar) getActivity().findViewById(R.id.progress);
        pb.setVisibility(View.VISIBLE);

        NetworkService
                .getInstance()
                .getParkingApi()
                .doGetParkingList(page, size)
                .enqueue(new Callback<List<Parking>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Parking>> call, @NonNull Response<List<Parking>> response) {

                        pb.setVisibility(View.GONE);

                        List<Parking> data = response.body();
                        if (data != null) {
                            grid.setAdapter(new ParkingGridAdapter(getActivity(), data));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Parking>> call, @NonNull Throwable t) {

                        pb.setVisibility(View.GONE);

                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Parking element = (Parking)parent.getAdapter().getItem(position);
                Toast
                        .makeText(getActivity(), element.toString(), Toast.LENGTH_LONG)
                        .show();

                View details = inflater.inflate(R.layout.parking_details, container,false);
/*
                ImageView image = (ImageView) details.findViewById(R.id.image);

                TextView name = (TextView) details.findViewById(R.id.parking_name);
                TextView address = (TextView) details.findViewById(R.id.parking_address);

                if (element.getName() != null)
                    name.setText(element.getName());

                if (element.getAddress() != null)
                    address.setText(element.getAddress());
*/
                ((MainActivity)getActivity()).loadFragment("ParkingDetails");
            }
        });

        return root;
    }
}
