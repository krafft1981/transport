package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.rental.transport.model.Parking;
import com.rental.transport.R;

import java.util.List;

public class ParkingGridAdapter extends BaseAdapter {

    private Context context;
    private List<Parking> parkings;

    public ParkingGridAdapter(Context context, List<Parking> parkings) {
        this.context = context;
        this.parkings = parkings;
    }

    @Override
    public int getCount() {
        return parkings.size();
    }

    @Override
    public Object getItem(int id) {
        return parkings.get(id);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int id, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.transport_element, null);

//        TextView type   = item.findViewById(R.id);
//        TextView name   = item.findViewById(R.id.transport_name);

        Parking p = parkings.get(id);

/*
        if (t.getImage().length != 0) {
            byte[] data = Base64.decode(t.getImage(), 0);
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            image.setImageBitmap(bmp);
        }
*/

//        type.setText(p.getAddress());
//        name.setText(p.getDescription());

        return item;
    }
}
