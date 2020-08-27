package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Transport;

import java.util.List;

public class TransportGridAdapter extends BaseAdapter {

    private Context context;
    private List<Transport> data;

    public TransportGridAdapter(Context context, List<Transport> data) {

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
        View item = inflater.inflate(R.layout.transport_element, null);

        ImageView image = item.findViewById(R.id.gridview_image);
        TextView type   = item.findViewById(R.id.transport_type);
        TextView name   = item.findViewById(R.id.transport_name);

        Transport element = data.get(id);

        if ((element.getType() != null) && (!element.getType().isEmpty())) type.setText(element.getType());
        if ((element.getName() != null) && (!element.getName().isEmpty())) name.setText(element.getName());

        return item;
    }
}
