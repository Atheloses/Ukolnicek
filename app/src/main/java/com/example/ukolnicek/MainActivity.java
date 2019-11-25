package com.example.ukolnicek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private static int REQUEST_CODE_PICK_ACCOUNT = 1;
    private String [] permissions = {"android.permission.INTERNET", "android.permission.GET_ACCOUNTS"};
    private Context context;
    private String AuthToken;
    private Account[] accounts;
    private AccountManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
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


//        if(accounts.length>0){
//            Bundle options = new Bundle();
//
//            Toast.makeText(getApplicationContext(),accounts[0].name,Toast.LENGTH_LONG);
//
//            am.getAuthToken(
//                    accounts[0],                     // Account retrieved using getAccountsByType()
//                    "Manage your tasks",            // Auth scope
//                    options,                        // Authenticator-specific options
//                    this,                           // Your activity
//                    new OnTokenAcquired(),          // Callback called when a token is successfully acquired
//                    new Handler(new OnError()));    // Callback called if an error occurs
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                Bundle options = new Bundle();
                String emailName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Account theOneAcc = null;
                for(Account acc : accounts){
                    if(emailName.equals(acc.name))
                        theOneAcc = acc;
                }

                if(theOneAcc!=null) {
                    am.getAuthToken(
                            accounts[0],                     // Account retrieved using getAccountsByType()
                            "test",            // Auth scope
                            options,                        // Authenticator-specific options
                            this,                           // Your activity
                            new OnTokenAcquired(),          // Callback called when a token is successfully acquired
                            new Handler(new OnError()));
                    Toast.makeText(this, "found: " + emailName, Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Account not chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SignInAccount(String authToken) {
        try {
            URL url = new URL("https://www.googleapis.com/tasks/v1/users/@me/lists?key=" + "xxx");
            URLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("client_id", "xxx");
            conn.addRequestProperty("client_secret", "xxx");
            conn.setRequestProperty("Authorization", "OAuth " + authToken);

            final URLConnection finalConn = conn;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        InputStream in = new BufferedInputStream(finalConn.getInputStream());
                        String output = readStream(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ((HttpURLConnection) finalConn).disconnect();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        InputStreamReader isReader = new InputStreamReader(inputStream);
        //Creating a BufferedReader object
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while((str = reader.readLine())!= null){
            sb.append(str);
        }
        return sb.toString();
    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            // Get the result of the operation from the AccountManagerFuture.
            Bundle bundle = null;
            try {
                bundle = result.getResult();
                AuthToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                SignInAccount(AuthToken);
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            }

            // The token is a named value in the bundle. The name of the value
            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
            String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

        }
    }

    private class OnError implements Handler.Callback {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            return false;
        }
    }
}
