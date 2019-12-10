package com.example.ukolnicek.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ukolnicek.R;

import java.util.List;

public class UkolAdapter extends ArrayAdapter<UkolEntry> {

    Context context;
    int layoutResourceId;
    List<UkolEntry> data = null;


    public UkolAdapter(Context context, int layoutResourceId, List<UkolEntry> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UkolEntryHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new UkolEntryHolder();
            holder.txtNazev = (TextView)row.findViewById(R.id.txtNazev);
            holder.txtPopis = (TextView)row.findViewById(R.id.txtPopis);

            row.setTag(holder);
        }
        else
        {
            holder = (UkolEntryHolder)row.getTag();
        }

        UkolEntry entry = data.get(position);
        holder.txtNazev.setText("Název: "+entry.Nazev);
        holder.txtPopis.setText("ID: "+entry.ID.toString());
        holder.txtPopis.setTag(entry.ID.toString());

        return row;
    }

    static class UkolEntryHolder
    {
        TextView txtNazev;
        TextView txtPopis;
    }
}
