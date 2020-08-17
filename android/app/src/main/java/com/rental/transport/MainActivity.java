package com.rental.transport;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.rental.transport.service.NetworkService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private NetworkService network;
    private Long accountId;
    private Integer page = 0;
    private Integer size = 50;

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

        if (isNetworkAllow(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Is Not Connected", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
/*
    private void fillData() {
        // clear
        try {
            for (Transport transport : network.getTransportApi().getTransportList(page, size).execute().body()) {
                Log.d("Network", transport.getType());
            }
            Log.d("Network", network.getTransportApi().getTransportList(page, size).execute().body().toString());

        } catch (Exception e) {
//            msgAlertBox( "Network", "Нет связи. Проверьте подключение");
        }
    }
*/
    public boolean isNetworkAllow(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
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
/*
    @Override
    protected void onRestart() {
        super.onRestart();
//        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        fillData();
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getPermissions()) {
            String account = getAccount();
//            network = NetworkService.getInstance(account);
            try {
//                accountId = network.getRegistrationApi().postRegistration(account).execute().body();
            } catch (Exception e) {
                msgAlertBox( "Network", "Нет связи. Проверьте подключение");
            }

//            fillData();
        }

        List<ListItem> image_details = getListData();
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new CustomListAdapter(this, image_details));

        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                ListItem listItem = (ListItem) o;
                Toast.makeText(MainActivity.this, "Selected :" + " " + listItem, Toast.LENGTH_LONG).show();
            }
        });
    }

    private  List<ListItem> getListData() {
        List<ListItem> list = new ArrayList<ListItem>();
        Random random = new Random();
        for(Integer id = 0; id < 100; id ++) {
            list.add(new ListItem("Аллегра " + id, "ic_launcher", random.nextInt()));
        }

        return list;
    }
}