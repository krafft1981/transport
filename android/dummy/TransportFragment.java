package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Transport;
import com.rental.transport.service.NetworkService;

import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.transport_fragment, container,false);

        NetworkService
                .getInstance()
                .getTransportApi()
                .doGetTransportList(0, 100)
                .enqueue(new Callback<List<Transport>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Transport>> call, @NonNull Response<List<Transport>> response) {
//                        gridView.setAdapter(new TransportGridAdapter(getActivity().getApplicationContext(), response.body()));
                        Toast
                                .makeText(getActivity(), response.body().size(), Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Transport>> call, @NonNull Throwable t) {
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
/*
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment transportDetails = new TransportDetails();
                fragmentTransaction.replace(R.id.container, transportDetails);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
*/
        return rootView;
    }
}
