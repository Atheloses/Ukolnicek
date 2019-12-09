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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFS = "ukolnicek_prefs";
    public static final String MY_PREFS_LOGIN = "ukolnicek_prefs";
    private static int REQUEST_CODE_PICK_ACCOUNT = 1;
    private String [] permissions = {"android.permission.INTERNET", "android.permission.GET_ACCOUNTS"};
    private Context context;
    private Account[] accounts;
    private AccountManager am;
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

        loadPrefs();

        if (Login != null && !Login.isEmpty())
            loggedInPage();
        else
            loginPage();
    }

    private void loadPrefs(){
        SharedPreferences prefs = getSharedPreferences(MainActivity.MY_PREFS, MODE_PRIVATE);
        Login = prefs.getString(MainActivity.MY_PREFS_LOGIN,"");
        invalidateOptionsMenu();
    }

    private void savePrefs(){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putString(MainActivity.MY_PREFS_LOGIN,Login);
        invalidateOptionsMenu();
        editor.apply();
    }

    private void loggedInPage(){
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
    }

    private void loginPage(){
        setContentView(R.layout.activity_login);
    }

    public void ButtonPrihlasit(View view) {
        am = AccountManager.get(this);
        accounts = am.getAccounts();


        Intent intent = AccountManager.newChooseAccountIntent(
                null,
                null,
                new String[] {"com.google", "com.google.android.legacyimap"},
                null,
                null,
                null,
                null);

        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {


                String emailName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Account theOneAcc = null;
                for(Account acc : accounts){
                    if(emailName.equals(acc.name))
                        theOneAcc = acc;
                }

                if(theOneAcc!=null) {
                    Toast.makeText(this, "found: " + emailName, Toast.LENGTH_LONG).show();
                    Login = theOneAcc.name;
                    savePrefs();
                    loggedInPage();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Account not chosen", Toast.LENGTH_SHORT).show();
            }
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
        setContentView(R.layout.activity_login);
    }

    public void TestButton(View view) {
    }
}
