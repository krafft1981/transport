package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rental.transport.model.Transport;
import com.rental.transport.R;

import java.util.List;

public class TransportGridAdapter extends BaseAdapter {

    private Context context;
    private List<Transport> transports;

    public TransportGridAdapter(Context context, List<Transport> transports) {
        this.context = context;
        this.transports = transports;
    }

    @Override
    public int getCount() {
        return transports.size();
    }

    @Override
    public Object getItem(int id) {

        return transports.get(id);
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

        Transport t = transports.get(id);

/*
        if (t.getImage().length != 0) {
            byte[] data = Base64.decode(t.getImage(), 0);
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            image.setImageBitmap(bmp);
        }
*/

        type.setText(t.getType());
        name.setText(t.getName());

        return item;
    }
}
