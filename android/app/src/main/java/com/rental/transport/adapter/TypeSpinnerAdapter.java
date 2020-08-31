package com.rental.transport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rental.transport.R;
import com.rental.transport.model.Type;

import java.util.List;

public class TypeSpinnerAdapter extends ArrayAdapter<Type> {

        private Context context;
        private List<Type> data;

        public TypeSpinnerAdapter(Context context, int resouceId, int textviewId, List<Type> data) {

                super(context, resouceId, textviewId, data);

                this.context = context;
                this.data = data;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

                Type type = data.get(position);
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View item = inflater.inflate(R.layout.transport_type_item, null,true);

                TextView txtTitle = (TextView) item.findViewById(R.id.title);
                txtTitle.setText(type.getName());

//                ImageView imageView = (ImageView) row.findViewById(R.id.icon);
//                imageView.setImageResource(R);

                return item;
        }
}
