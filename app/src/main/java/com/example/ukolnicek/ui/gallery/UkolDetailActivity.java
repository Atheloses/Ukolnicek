package com.example.ukolnicek.ui.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ukolnicek.DBHelper;
import com.example.ukolnicek.R;

public class UkolDetailActivity extends AppCompatActivity {


    private DBHelper mydb;
    TextView nameTextView;
    int id_update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ukol_detail_activity);

        nameTextView = findViewById(R.id.editTextName);
        mydb = new DBHelper(this);
        Intent i = getIntent();
        if(i !=null)
        {
            //ziskam ID, ktere se ma editovat/zobrazit/mazat - poslane z main aktivity
            int value = i.getIntExtra("id", 0);
            if (value >0)
            {
                //z db vytahnu zaznam pod hledanym ID a ulozim do id_update
                id_update = value;
                Cursor rs = mydb.getData(id_update);
                rs.moveToFirst();

                //z DB vytahnu jmeno zaznamu
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

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(id_update>0){
            getMenuInflater().inflate(R.menu.ukol_detail_menu, menu);
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

        }
        if (id == R.id.Delete_Contact)
        {
            mydb.deleteUkol(id_update);
            //TODO 3.0: zavolat z mydb metodu na odstraneni zaznamu
            finish();
        }

        return true;
    }

    public void saveButtonAction(View view)
    {

        if(id_update>0){
            mydb.updateUkol(id_update,nameTextView.getText().toString());
            //TODO 4.0: zavolat z mydb metodu na update zaznamu
            finish();
        }
        else{
            //vlozeni zaznamu
            if(mydb.insertUkol(nameTextView.getText().toString())){
                Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(getApplicationContext(), "not saved", Toast.LENGTH_SHORT).show();
            }
            finish();
        }

    }
}