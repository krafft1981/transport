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

        TextView phone      = item.findViewById(R.id.customer_phone);
        ImageView image     = item.findViewById(R.id.gridview_image);
        TextView family     = item.findViewById(R.id.customer_family);
        TextView first_name = item.findViewById(R.id.customer_first_name);
        TextView last_name  = item.findViewById(R.id.customer_last_name);

        Customer element = data.get(id);

        if ((element.getFamily() != null) && (!element.getFamily().isEmpty()))
                family.setText(element.getFamily());

        if ((element.getFirstName() != null) && (!element.getFirstName().isEmpty()))
            first_name.setText(element.getFirstName());

        if ((element.getLastName() != null) && (!element.getLastName().isEmpty()))
            last_name.setText(element.getLastName());

        if ((element.getPhone() != null) && (!element.getPhone().isEmpty()))
            phone.setText(element.getPhone());

        return item;
    }
}
