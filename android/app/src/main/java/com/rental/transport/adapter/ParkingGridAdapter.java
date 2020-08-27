package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Parking;

import java.util.List;

public class ParkingGridAdapter extends BaseAdapter {

    private Context context;
    private List<Parking> data;

    public ParkingGridAdapter(Context context, List<Parking> data) {

        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int id) {

        return data.get(id);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int id, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.parking_element, null);

        TextView name = item.findViewById(R.id.parking_name);
        TextView address = item.findViewById(R.id.parking_address);

        Parking element = data.get(id);

        if ((element.getName()    != null) && (!element.getName()   .isEmpty())) name   .setText(element.getName()   );
        if ((element.getAddress() != null) && (!element.getAddress().isEmpty())) address.setText(element.getAddress());

        return item;
    }
}
