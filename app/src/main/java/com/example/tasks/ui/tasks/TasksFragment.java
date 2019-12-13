package com.example.tasks.ui.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tasks.DBHelper;
import com.example.tasks.MainActivity;
import com.example.tasks.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    private DBHelper db;
    private ListView itemListView;
    private Context context;
    private View view;
    private boolean LightMode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.tasks_fragment, container, false);

        context = getContext();

        db = new DBHelper(context);

        itemListView = view.findViewById(R.id.listView1);
        populateListView();
        LightMode = ((MainActivity)getActivity()).LightMode;

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTaskDetailActivity(0);
            }
        });

    }

    @Override
    public void onResume()
    {
        super.onResume();
        populateListView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_tasks_1)
        {
            startTaskDetailActivity(0);
        }

        if (id == R.id.item_tasks_2)
        {
            Snackbar.make(view.findViewById(R.id.tasks_fragment), getString(R.string.text_deleted_rows)+ " "+db.removeAllTasks(), Snackbar.LENGTH_LONG).show();
            try {
                populateListView();
                Fragment frg = null;
                frg = getFragmentManager().findFragmentById(R.id.tasks_fragment);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateListView(){
        ArrayList<TasksEntry> arrayList = db.getAllTasks();

        TasksAdapter adapter = new TasksAdapter(context,
                R.layout.tasks_entry_layout, arrayList);

        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                TasksAdapter.TasksEntryHolder holder = (TasksAdapter.TasksEntryHolder)arg1.getTag();
                startTaskDetailActivity(Integer.parseInt(String.valueOf(holder.txtName.getTag())));
            }
        });
    }

    private void startTaskDetailActivity(int id){
        Intent myIntent = new Intent(context, TasksDetailActivity.class);
        myIntent.putExtra("LightMode", LightMode);
        myIntent.putExtra("id",id);
        startActivity(myIntent);
    }
}