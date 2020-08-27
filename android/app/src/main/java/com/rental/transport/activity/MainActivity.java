package com.rental.transport.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rental.transport.R;
import com.rental.transport.service.NetworkService;

import java.util.Arrays;
import java.util.List;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String getAccount() {
        Context context = getApplicationContext();
        AccountManager accountManager = AccountManager.get(context);
        List<Account> accounts = Arrays.asList(accountManager.getAccountsByType("com.google"));
        if (accounts.size() == 0) {
            return "";
        }

        return accounts.get(0).name;
    }

    Boolean checkPermissions() {

        String[] permissions  = {
                Manifest.permission.INTERNET,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    return false;
                }
                else {
                    // ?????
                }
            }
        }

        return true;
    }

    private Long accountId;

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                R.animator.card_flip_left_in, R.animator.card_flip_left_out);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (checkPermissions() == false) {
            // exit
        }

        String account = getAccount();
        if (account.isEmpty()) {
            // exit
        }

        NetworkService
                .getInstance(account)
                .getRegistrationApi()
                .doPostRegistration(account)
                .enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {
                        accountId = response.body();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {
                        Toast
                                .makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.transport:
                                loadFragment(new TransportFragment());
                                break;

                            case R.id.order:
                                loadFragment(new OrderFragment());
                                break;

                            case R.id.customer:
                                loadFragment(new CustomerFragment());
                                break;

                            case R.id.parking:
                                loadFragment(new ParkingFragment());
                                break;
                        }

                        return true;
                    }
                });

        loadFragment(new TransportFragment());
    }
}
