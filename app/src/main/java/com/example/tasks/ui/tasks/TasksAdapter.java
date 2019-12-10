package com.example.tasks.ui.tasks;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tasks.R;

import java.util.List;

class TasksAdapter extends ArrayAdapter<TasksEntry> {

    private final Context context;
    private final int layoutResourceId;
    private List<TasksEntry> data;


    public TasksAdapter(Context context, int layoutResourceId, List<TasksEntry> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TasksEntryHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TasksEntryHolder();
            holder.txtName = (TextView)row.findViewById(R.id.txtName);
            holder.txtDesc = (TextView)row.findViewById(R.id.txtDesc);

            row.setTag(holder);
        }
        else
        {
            holder = (TasksEntryHolder)row.getTag();
        }

        TasksEntry entry = data.get(position);
        holder.txtName.setText(context.getString(R.string.row_name, entry.Name));
        holder.txtDesc.setText(context.getString(R.string.row_desc,(CharSequence) DateFormat.format("dd/MM/yyyy HH:mm", entry.Time).toString()));
        holder.txtName.setTag(entry.ID);

        return row;
    }

    static class TasksEntryHolder
    {
        TextView txtName;
        TextView txtDesc;
    }
}
