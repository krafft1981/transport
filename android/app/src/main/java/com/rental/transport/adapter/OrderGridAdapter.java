package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.rental.transport.R;
import com.rental.transport.model.Order;

import java.util.List;

public class OrderGridAdapter extends BaseAdapter {

    private Context context;
    private List<Order> data;

    public OrderGridAdapter(Context context, List<Order> data) {

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
        View item = inflater.inflate(R.layout.order_element, null);

//        TextView name = item.findViewById(R.id.order_name);
//        TextView address = item.findViewById(R.id.parking_address);

//        Order element = data.get(id);

        return item;
    }
}
