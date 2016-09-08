package com.pruebaandroid.topapps.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pruebaandroid.topapps.DataObjects.Entry;
import com.pruebaandroid.topapps.R;
import com.pruebaandroid.topapps.Utils.ImageTransroundedcorner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Andres on 05/09/2016.
 */
public class AplicatiosAdapter extends ArrayAdapter<Entry> {

    public AplicatiosAdapter(Context context, ArrayList<Entry> results) {
        super(context, 0, results);

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_aplications, parent, false);
            holder = new ViewHolder();
            holder.itemNombre = (TextView) convertView.findViewById(R.id.lbl_nombre);
            holder.itemCompany = (TextView) convertView.findViewById(R.id.lbl_company);
            holder.itemFoto = (ImageView) convertView.findViewById(R.id.IVMenu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Entry currentEntry = getItem(position);
        holder.itemNombre.setText(currentEntry.getName());
        holder.itemCompany.setText(currentEntry.getArtist());

        Picasso.with(convertView.getContext())
                .load(currentEntry.getUrlimagen())
                //.placeholder(R.drawable.ic_placeholder)   // optional
                .error(R.drawable.ic_file_cloud_off)      // optional
                .transform(new ImageTransroundedcorner())
                .into(holder.itemFoto);


        return convertView;
    }

    static class ViewHolder {
        TextView itemNombre;
        TextView itemCompany;
        ImageView itemFoto;

    }
}