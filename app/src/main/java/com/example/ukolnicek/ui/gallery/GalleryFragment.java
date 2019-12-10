package com.example.ukolnicek.ui.gallery;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ukolnicek.DBHelper;
import com.example.ukolnicek.R;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private DBHelper mydb;
    private ListView itemListView;
    private Context context;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = view.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        context = getContext();

        mydb = new DBHelper(context);
        itemListView = view.findViewById(R.id.listView1);
        populateListView();

        return view;
    }

    private void populateListView(){
        ArrayList<UkolEntry> arrayList = mydb.getAllUkol();

        UkolAdapter adapter = new UkolAdapter(context,
                R.layout.ukol_entry_layout, arrayList);

        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                UkolAdapter.UkolEntryHolder holder = (UkolAdapter.UkolEntryHolder)arg1.getTag();
                startDetailUkolActivity(Integer.parseInt(String.valueOf(holder.txtPopis.getTag())));
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
        inflater.inflate(R.menu.ukol_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_ukol_1)
        {
            startDetailUkolActivity(0);
        }

        return super.onOptionsItemSelected(item);
    }

    private void startDetailUkolActivity(int id){
        Intent myIntent = new Intent(context, UkolDetailActivity.class);
        myIntent.putExtra("id",id);
        startActivity(myIntent);
    }
}