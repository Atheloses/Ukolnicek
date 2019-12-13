package com.example.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tasks.ui.tasks.TasksEntry;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final String MY_PREFS = "tasks_prefs";
    private static final String MY_PREFS_LOGIN = "tasks_prefs_login";
    private static final String MY_PREFS_BACKGROUND = "tasks_prefs_background";
    private final String[] permissions = {"android.permission.INTERNET", "android.permission.GET_ACCOUNTS","android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};
    private String Login = "";
    public boolean LightMode = true;
    private Context context;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int requestCode = 200;
        requestPermissions(permissions, requestCode);

        loadPrefs();
        if (LightMode)
            setTheme(R.style.AppThemeLight);
        else
            setTheme(R.style.AppThemeDark);

        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_tasks, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Login();

        checkIntent();
    }

    private void checkIntent(){

        int id;
        Intent i = getIntent();
        if(i !=null)
        {
            int value = i.getIntExtra("id", 0);
            if (value >0)
            {

                id = value;
                DBHelper db = new DBHelper(context);
                TasksEntry task = db.getTask(id);
                Snackbar.make(findViewById(android.R.id.content), "ID: "+task.ID, 3000).show();
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.deleteNotificationChannel(i.getStringExtra("channel"));
            }
        }
    }

    private void Login() {
        Intent myIntent = new Intent(context, LoginActivity.class);
        myIntent.putExtra("login", Login);
        myIntent.putExtra("LightMode", LightMode);
        startActivityForResult(myIntent, 69);
    }

    private void loadPrefs() {
        SharedPreferences prefs = getSharedPreferences(MainActivity.MY_PREFS, MODE_PRIVATE);
        Login = prefs.getString(MainActivity.MY_PREFS_LOGIN, "");
        LightMode = prefs.getBoolean(MainActivity.MY_PREFS_BACKGROUND, true);

        invalidateOptionsMenu();
    }

    private void savePrefs() {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putString(MainActivity.MY_PREFS_LOGIN, Login);
        editor.putBoolean(MY_PREFS_BACKGROUND, LightMode);
        editor.apply();

        invalidateOptionsMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                View parentLayout = findViewById(android.R.id.content);
                Login = data.getStringExtra("login");
                ((TextView) findViewById(R.id.nav_header_subtitle)).setText(Login);
                savePrefs();
                Snackbar.make(parentLayout, getString(R.string.text_logged) + " " + Login, Snackbar.LENGTH_LONG).show();
            } else
                finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void menuLogout(MenuItem item) {
        Login = "";
        savePrefs();
        Login();
    }

    public void menuBackground(boolean lightMode) {
        LightMode = lightMode;
        savePrefs();

        finish();
        startActivity(new Intent(MainActivity.this, MainActivity.this.getClass()));
    }
}
