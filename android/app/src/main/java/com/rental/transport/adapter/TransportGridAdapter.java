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
import java.util.Random;

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

        Transport info = data.get(id);
        TextView description = item.findViewById(R.id.transport_description);

        StringBuilder builder = new StringBuilder();
        builder.append(info.getType());
        builder.append(" ");
        builder.append(info.getName());
        description.setText(builder.toString());

        if (info.getImages().isEmpty()) {

            Integer p[] = {
                    R.drawable.i0,
                    R.drawable.i1,
                    R.drawable.i2,
                    R.drawable.i3,
                    R.drawable.i4,
                    R.drawable.i5,
                    R.drawable.i6,
                    R.drawable.i7,
                    R.drawable.i8,
                    R.drawable.i9,
                    R.drawable.i10,
                    R.drawable.i11,
                    R.drawable.i12,
                    R.drawable.i13,
                    R.drawable.i14,
                    R.drawable.i15,
                    R.drawable.i16
            };

            ImageView image = item.findViewById(R.id.gridview_image);
            Random rand = new Random();
            image.setImageResource(p[rand.nextInt(p.length)]);
        }

        else {
            Random rand = new Random();
            Long imageId = info.getImages().get(rand.nextInt(info.getImages().size()));

//            ImageService
//                    .getInstance(context)
//                    .setImage(imageId, R.drawable.i13, (ImageView)item.findViewById(R.id.gridview_image));
        }

        return item;
    }
}
