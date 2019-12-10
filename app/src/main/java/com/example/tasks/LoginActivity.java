package com.example.tasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends Activity {
    private static final int REQUEST_CODE_PICK_ACCOUNT = 1;
    private Account[] accounts;
    private String Login = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login = getIntent().getExtras().getString("login","");

        if(!Login.isEmpty()){
            Intent MyIntent = new Intent();
            MyIntent.putExtra("login",Login);
            setResult(RESULT_OK,MyIntent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            View parentLayout = findViewById(android.R.id.content);
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                String emailName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Account theOneAcc = null;
                for(Account acc : accounts){
                    if(emailName.equals(acc.name))
                        theOneAcc = acc;
                }

                if(theOneAcc!=null) {
                    Login = theOneAcc.name;

                    Intent MyIntent = new Intent();
                    MyIntent.putExtra("login",Login);
                    setResult(RESULT_OK,MyIntent);
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(parentLayout, getString(R.string.text_account_not_found), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void onClickLogin(View view) {
        AccountManager am = AccountManager.get(this);
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
}
