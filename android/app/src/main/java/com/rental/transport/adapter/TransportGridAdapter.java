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
import com.rental.transport.service.ImageService;

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

        if ((info.getType() != null) && (info.getName() != null)) {
            StringBuilder builder = new StringBuilder();
            builder.append(info.getType());
            builder.append(" ");
            builder.append(info.getName());
            description.setText(builder.toString());
        }

        if (info.getImages().size() > 0) {

            Random rand = new Random();
            Long imageId = info.getImages().get(rand.nextInt(info.getImages().size()));
            ImageService
                    .getInstance(context)
                    .setImage(imageId, R.drawable.samokat, (ImageView)item.findViewById(R.id.gridview_image));
        }

        else {
            ImageView image = item.findViewById(R.id.gridview_image);
            image.setImageResource(R.drawable.samokat);
        }

        return item;
    }
}
