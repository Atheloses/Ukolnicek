package com.example.tasks.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tasks.DBHelper;
import com.example.tasks.OnSwipeTouchListener;
import com.example.tasks.R;
import com.example.tasks.ui.tasks.TasksEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private int position;
    private View view;
    private Context context;
    private DBHelper db;
    private ArrayList<TasksEntry> tasks;
    private TextView homeText;
    private TextView nameText;
    private TextView deadlineText;
    private TextView finishedText;
    private LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);

        context = getContext();
        position = 0;

        db = new DBHelper(context);
        tasks = db.getTasks();

        int id = -1;
        if(getArguments()!=null) //Why is this empty.. whatever
            id = getArguments().getInt("ID");
        if(id>0){
            for (int i =0;i< tasks.size();i++) {
                if (tasks.get(i).ID == id){
                    position = i;
                    break;
                }
            }
        }

        fillTask(position);

        view.findViewById(R.id.home_fragment).setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {
                //Toast.makeText(context, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                fillTask(position-1);
            }
            public void onSwipeLeft() {
                fillTask(position+1);
            }
            public void onSwipeBottom() {
                //Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }

    private void fillTask(int position){
        homeText = view.findViewById(R.id.home_text);
        nameText = view.findViewById(R.id.home_name);
        deadlineText = view.findViewById(R.id.home_deadline);
        finishedText = view.findViewById(R.id.home_finished);
        linearLayout = view.findViewById(R.id.home_layout);

        if(tasks.size()<1){
            homeText.setText("Nenalezeny žádné úkoly");
            linearLayout.setVisibility(View.INVISIBLE);
            return;
        }
        else
            linearLayout.setVisibility(View.VISIBLE);

        if(position>tasks.size()-1)
            position=position%tasks.size();

        if(position<0)
            position=tasks.size()+position%tasks.size();

        TasksEntry task = tasks.get(position);
        homeText.setText("Úkol "+(position+1)+"/"+tasks.size());
        nameText.setText("Název úkolu: "+task.Name);
        deadlineText.setText("Termín úkolu: "+ DateFormat.format("dd/MM/yyyy HH:mm", task.Time).toString());
        if(task.Finished)
            finishedText.setText("Úkol je splněn");
        else
            finishedText.setText("Úkol není splněn");
        this.position = position;
    }
}