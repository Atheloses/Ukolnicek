package com.example.tasks.ui.tasks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasks.DBHelper;
import com.example.tasks.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class TasksDetailActivity extends AppCompatActivity{


    private DBHelper db;
    private TextView nameTextView;
    private DatePicker deadlineDatePicker;
    private TimePicker deadlineTimePicker;
    private int id_update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras().getBoolean("LightMode",true))
            setTheme(R.style.AppThemeLight_ActionBar);
        else
            setTheme(R.style.AppThemeDark_ActionBar);

        setContentView(R.layout.tasks_detail_activity);

        nameTextView = findViewById(R.id.editTextName);

        deadlineDatePicker = findViewById(R.id.editTextDeadline);
        deadlineTimePicker = findViewById(R.id.editTextDeadlineTime);
        Calendar today = Calendar.getInstance();
        deadlineTimePicker.setIs24HourView(true);
        deadlineTimePicker.setHour(today.get(Calendar.HOUR_OF_DAY));
        deadlineTimePicker.setMinute(today.get(Calendar.MINUTE));
        deadlineDatePicker.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

        db = new DBHelper(this);
        Intent i = getIntent();
        if(i !=null)
        {
            int value = i.getIntExtra("id", 0);
            if (value >0)
            {
                id_update = value;
                Cursor rs = db.getData(id_update);
                rs.moveToFirst();

                String nameDB = rs.getString(rs.getColumnIndex("name"));

                if (!rs.isClosed())
                {
                    rs.close();
                }
                Button b = findViewById(R.id.buttonSave);
                b.setVisibility(View.INVISIBLE);

                nameTextView.setText(nameDB);
                nameTextView.setEnabled(false);
                nameTextView.setFocusable(false);
                nameTextView.setClickable(false);
                deadlineTimePicker.setEnabled(false);
                deadlineTimePicker.setFocusable(false);
                deadlineTimePicker.setClickable(false);
                deadlineDatePicker.setEnabled(false);
                deadlineDatePicker.setFocusable(false);
                deadlineDatePicker.setClickable(false);


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(id_update>0){
            getMenuInflater().inflate(R.menu.tasks_detail_menu, menu);
            super.onCreateOptionsMenu(menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Edit_Contact) {
            Button b = findViewById(R.id.buttonSave);
            b.setVisibility(View.VISIBLE);

            nameTextView.setEnabled(true);
            nameTextView.setFocusableInTouchMode(true);
            nameTextView.setClickable(true);
            deadlineTimePicker.setEnabled(true);
            deadlineTimePicker.setFocusable(true);
            deadlineTimePicker.setClickable(true);
            deadlineDatePicker.setEnabled(true);
            deadlineDatePicker.setFocusable(true);
            deadlineDatePicker.setClickable(true);

        }
        if (id == R.id.Delete_Contact)
        {
            db.deleteTasks(id_update);
            finish();
        }

        return true;
    }

    public void saveButtonAction(View view)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, deadlineDatePicker.getDayOfMonth());
        cal.set(Calendar.MONTH, deadlineDatePicker.getMonth());
        cal.set(Calendar.YEAR, deadlineDatePicker.getYear());
        cal.set(Calendar.HOUR_OF_DAY, deadlineTimePicker.getHour());
        cal.set(Calendar.MINUTE, deadlineTimePicker.getMinute());
        cal.set(Calendar.SECOND,0);

        if(id_update>0){
            db.updateTasks(id_update,nameTextView.getText().toString(),cal.getTime());
            finish();
        }
        else{
            if(db.insertTask(nameTextView.getText().toString(),cal.getTime()))
                Snackbar.make(view, getString(R.string.text_created_new_succ), Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(view, getString(R.string.text_created_new_fail), Snackbar.LENGTH_LONG).show();
            finish();
        }

    }
}