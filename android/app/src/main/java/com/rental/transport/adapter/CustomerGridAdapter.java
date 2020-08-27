package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Customer;

import java.util.List;

public class CustomerGridAdapter extends BaseAdapter {

    private Context context;
    private List<Customer> data;

    public CustomerGridAdapter(Context context, List<Customer> data) {

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
        View item = inflater.inflate(R.layout.customer_element, null);

        TextView phone  = item.findViewById(R.id.customer_phone);
        TextView fio    = item.findViewById(R.id.customer_fio);
        ImageView image = item.findViewById(R.id.gridview_image);

        Customer element = data.get(id);

        StringBuilder data = new StringBuilder();
        data.append(element.getFamily());
        data.append(" ");
        data.append(element.getFirstName());
        data.append(" ");
        data.append(element.getLastName());

        phone.setText(element.getPhone());
        fio.setText(data.toString());

        return item;
    }
}
