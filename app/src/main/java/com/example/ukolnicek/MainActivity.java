package com.example.ukolnicek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFS = "ukolnicek_prefs";
    public static final String MY_PREFS_LOGIN = "ukolnicek_prefs";
    private String [] permissions = {"android.permission.INTERNET", "android.permission.GET_ACCOUNTS"};
    private Context context;
    private String Login = "";

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        loadPrefs();
        Login();
    }

    private void Login(){
        Intent myIntent = new Intent(getApplicationContext(),LoginActivity.class);
        myIntent.putExtra("login",Login);
        startActivityForResult(myIntent,69);
    }

    private void loadPrefs(){
        SharedPreferences prefs = getSharedPreferences(MainActivity.MY_PREFS, MODE_PRIVATE);
        Login = prefs.getString(MainActivity.MY_PREFS_LOGIN,"");

        invalidateOptionsMenu();
    }

    private void savePrefs(){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putString(MainActivity.MY_PREFS_LOGIN,Login);
        editor.apply();

        invalidateOptionsMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK){
                Login = data.getStringExtra("login");
                ((TextView)findViewById(R.id.nav_header_subtit)).setText(Login);
                savePrefs();
            }
            else
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

    public void TestButton(View view) {
    }
}
