package com.rental.transport;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rental.transport.service.NetworkService;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NetworkService ns = NetworkService.getInstance();
    private String account;

    private String getAccount() {
        Context context = getApplicationContext();
        AccountManager accountManager = AccountManager.get(context);
        List<Account> accounts = Arrays.asList(accountManager.getAccountsByType("com.google"));

        if (accounts.size() > 0) {
            return accounts.get(0).name;
        }

        return "";
    }

    private Boolean getPermissions() {

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Boolean permissions = getPermissions();
        account = getAccount();

        try {
            ns.getRegistrationApi().postRegistration(account).execute();
        }
        catch (Exception e) {

        }
    }
}
