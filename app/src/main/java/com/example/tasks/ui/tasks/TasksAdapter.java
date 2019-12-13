package com.example.tasks.ui.tasks;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
            holder.alarmButton = (ImageButton) row.findViewById(R.id.alarmButton);
            holder.doneButton = (ImageButton) row.findViewById(R.id.doneButton);
            holder.listView_row = (LinearLayout)row.findViewById(R.id.listView_row);

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
        holder.doneButton.setVisibility(View.INVISIBLE);
        holder.doneButton.setTag(entry.ID);
        holder.alarmButton.setVisibility(View.INVISIBLE);
        holder.alarmButton.setTag(entry.ID);
        if(entry.Finished)
            holder.listView_row.setBackgroundColor(context.getColor(R.color.lightGreen));
        else
            holder.listView_row.setBackgroundColor(context.getColor(R.color.lightRed));

        return row;
    }

    static class TasksEntryHolder
    {
        ImageButton doneButton;
        ImageButton alarmButton;
        TextView txtName;
        TextView txtDesc;
        LinearLayout listView_row;
    }
}
