package com.rental.transport.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.rental.transport.R;
import com.rental.transport.model.Transport;
import com.rental.transport.service.NetworkService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private NetworkService network;

    private Long accountId;

    private Integer page = 0;
    private Integer size = 50;

    private String getAccount() {
        Context context = getApplicationContext();
        AccountManager accountManager = AccountManager.get(context);
        List<Account> accounts = Arrays.asList(accountManager.getAccountsByType("com.google"));
        if (accounts.size() == 0) {
            msgAlertBox("Да заебало уже !", "Опять аккаунт пустой");
            return "";
        }

        return accounts.get(0).name;
    }

    void getPermissionsError() {

        String[] accessRight  = {
                Manifest.permission.INTERNET,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        for (String access : accessRight) {
            if (ContextCompat.checkSelfPermission(this, access) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
    }

    public void msgAlertBox(String title, String message) {

        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setTitle(title);
        dlgAlert.setMessage(message);
        dlgAlert.setIcon(android.R.drawable.ic_dialog_info);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermissionsError();

        GridView gridView = findViewById(R.id.gridview);

        String account = getAccount();
        Toast.makeText(getApplicationContext(), account, Toast.LENGTH_LONG).show();

        network = NetworkService.getInstance(account);
        network.getRegistrationApi().postRegistration(account).enqueue(
                new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        accountId = response.body();
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Испольуем аккаунт: " + t.toString(), Toast.LENGTH_LONG).show();
                        call.cancel();
                    }
                });

        CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), GridItemActivity.class);
                intent.putExtra("name", "Транспорт");
                intent.putExtra("image", R.drawable.watermelon);
                Toast.makeText(getApplicationContext(), "Пристегните ремни", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });
    }

    private class CustomAdapter extends BaseAdapter {

        List<Transport> transport = new ArrayList<>();

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.row_data,null);
            TextView name = view1.findViewById(R.id.name);
            ImageView image = view1.findViewById(R.id.image);
            name.setText("Транспорт");
            image.setImageResource(R.mipmap.ic_launcher);
            return view1;
        }

        public CustomAdapter() {
            network.getTransportApi().getTransportList(page, size).enqueue (
                    new Callback<List<Transport>>() {

                        @Override
                        public void onResponse(Call<List<Transport>> call, Response<List<Transport>> response) {
                            transport = response.body();
                        }

                        @Override
                        public void onFailure(Call<List<Transport>> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
