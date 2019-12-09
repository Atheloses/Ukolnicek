package com.example.ukolnicek;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private static int REQUEST_CODE_PICK_ACCOUNT = 1;
    private Account[] accounts;
    private AccountManager am;
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

                    Intent MyIntent = new Intent();
                    MyIntent.putExtra("login",Login);
                    setResult(RESULT_OK,MyIntent);
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Account not chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
