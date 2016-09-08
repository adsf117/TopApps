package com.pruebaandroid.topapps.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pruebaandroid.topapps.DataObjects.Category;
import com.pruebaandroid.topapps.R;

import java.util.ArrayList;

/**
 * Created by Andres on 06/09/2016.
 */
public class CategorysAdapter extends BaseAdapter {

    private ArrayList<Category> ListaItems;
    private LayoutInflater inflater;

    public CategorysAdapter(Context context,ArrayList<Category> results) {
        this.ListaItems = results;
        this.inflater = LayoutInflater.from(context);
    }

    public ArrayList<Category>  getData() {
        return ListaItems;
    }
    public int getCount() {
        return ListaItems.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View retval = this.inflater.inflate(R.layout.item_categorys, null);
        TextView title = (TextView) retval.findViewById(R.id.title);
        title.setText(ListaItems.get(position).getName());

        return retval;
    }


}
