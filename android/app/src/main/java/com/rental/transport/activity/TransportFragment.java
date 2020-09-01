package com.rental.transport.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.adapter.TransportGridAdapter;
import com.rental.transport.adapter.TypeSpinnerAdapter;
import com.rental.transport.model.Transport;
import com.rental.transport.model.Type;
import com.rental.transport.service.NetworkService;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportFragment extends Fragment {

    private Integer page = 10;
    private Integer size = 100;

    private GridView grid;
    private Spinner spinner;
    private CheckBox myself;

    private void loadData() {

        Type selected = (Type)spinner.getSelectedItem();

        if (myself.isChecked()) {
            List<Transport> data = new ArrayList<>();
            TransportGridAdapter adapter = new TransportGridAdapter(getActivity(), data);
            grid.setAdapter(adapter);
        }
        else {
            NetworkService
                    .getInstance()
                    .getTransportApi()
                    .doGetTransportListByType(page, size, selected.getName())
                    .enqueue(new Callback<List<Transport>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Transport>> call, @NonNull Response<List<Transport>> response) {

                            List<Transport> data = response.body();
                            if (data != null) {
                                TransportGridAdapter adapter = new TransportGridAdapter(getActivity(), data);
                                grid.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Transport>> call, @NonNull Throwable t) {
                            Toast
                                    .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.transport_fragment, container,false);

        grid = (GridView) root.findViewById(R.id.transport_gridview);
        spinner = (Spinner) root.findViewById(R.id.type_list);
        myself = (CheckBox) root.findViewById(R.id.only_myself);

        NetworkService
                .getInstance()
                .getTypeApi()
                .doGetTypeList(0, 50)
                .enqueue(new Callback<List<Type>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Type>> call, @NonNull Response<List<Type>> response) {
                        List<Type> data = response.body();
                        if (data != null) {

                            TypeSpinnerAdapter adapter = new TypeSpinnerAdapter(getActivity(), R.layout.transport_type_item, R.id.title, data);
                            adapter.setDropDownViewResource(R.layout.transport_type_item);
                            spinner.setAdapter(adapter);
                            spinner.setSelection(0);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Type>> call, @NonNull Throwable t) {
                        Toast
                                .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Transport element = (Transport)parent.getAdapter().getItem(position);

                Toast
                        .makeText(getActivity(), element.toString(), Toast.LENGTH_LONG)
                        .show();

                View details = inflater.inflate(R.layout.transport_details, container,false);

/*
                ImageView image = (ImageView) details.findViewById(R.id.image);

                TextView type = (TextView) details.findViewById(R.id.transportType);
                TextView name = (TextView) details.findViewById(R.id.transportName);
                TextView capacity = (TextView) details.findViewById(R.id.transportCapacity);

                type.setText(element.getType() == null ? "Не заполнено" : element.getType());
                name.setText(element.getName() == null ? "Не заполнено" : element.getName());
                capacity.setText(element.getCapacity() == null ? "Не заполнено" : element.getCapacity().toString());
*/
                ((MainActivity)getActivity()).loadFragment("TransportDetails");
            }
        });

        myself.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                loadData();
            }
        });

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        spinner.setOnItemSelectedListener(itemSelectedListener);

        return root;
    }
}
