package com.example.ukolnicek;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFS = "ukolnicek_prefs";
    public static final String MY_PREFS_LOGIN = "ukolnicek_prefs";
    private static int REQUEST_CODE_PICK_ACCOUNT = 1;
    private String [] permissions = {"android.permission.INTERNET", "android.permission.GET_ACCOUNTS"};
    private Context context;
    private Account[] accounts;
    private AccountManager am;
    private String Login = "";

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
            setContentView(R.layout.activity_main);
        else
            setContentView(R.layout.activity_login);
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
                    setContentView(R.layout.activity_main);
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Account not chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO: ADD ICONS TO MENU
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuItem logout = menu.findItem(R.id.menuLogout);
        if (Login != null && !Login.isEmpty()){
            logout.setEnabled(true);
            // logout.getIcon().setAlpha(255);
        }
        else{
            logout.setEnabled(false);
            // logout.getIcon().setAlpha(130);
        }

        return true;
    }

    public void menuLogout(MenuItem item) {
        Login = "";
        savePrefs();
        setContentView(R.layout.activity_login);
    }
}
