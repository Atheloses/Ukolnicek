package com.example.tasks.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tasks.DBHelper;
import com.example.tasks.MainActivity;
import com.example.tasks.R;
import com.example.tasks.XMLParser;
import com.example.tasks.ui.tasks.TasksEntry;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsFragment extends Fragment {
    public static final int PICKFILE_RESULT_CODE = 1;

    private View view;
    private boolean LightMode;
    private DBHelper db;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_fragment, container, false);

        LightMode = ((MainActivity)getActivity()).LightMode;
        Switch switchButton = view.findViewById(R.id.backgroundSwitch);
        switchButton.setChecked(!LightMode);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(LightMode==isChecked)
                    ((MainActivity)getActivity()).menuBackground(!isChecked);
            }
        });

        Button expButton = view.findViewById(R.id.exportButton);
        expButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exportApp();
            }
        });

        Button impButton = view.findViewById(R.id.importButton);
        impButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(context.getExternalFilesDir(null).getAbsolutePath());
                chooseFile.setDataAndType(uri,"*/*");
                startActivityForResult(Intent.createChooser(chooseFile, "Choose a file"), PICKFILE_RESULT_CODE);
            }
        });


        context = getContext();

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    ArrayList<TasksEntry> importedTasks;
                    try {
                        db = new DBHelper(context);
                        importedTasks = XMLParser.parse(context.getContentResolver().openInputStream((Uri) data.getData()));
                        Snackbar.make(view, "Import '" + db.insertTasks(importedTasks) + "' úkolů proběhl úspěšně", 1500).show();
                    } catch (Exception e) {
                        Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void exportApp() {
        db = new DBHelper(context);
        try {
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            File storageDir = context.getExternalFilesDir(null);
            String fileName = "Ukolnicek_Export_" + timeStamp + ".xml";
            File file = new File(storageDir+"/"+fileName);
            Snackbar.make(view, "Export proběhl úspěšně: '"+fileName+"'",5000).show();
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(outputStream);
            myOutWriter.write(XMLParser.CreateXML(db.getAllTasks()));
            myOutWriter.close();
            outputStream.close();

        } catch (Exception e) {
            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}