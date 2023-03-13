package com.nairaland.snakevpn.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nairaland.snakevpn.CheckInternetConnection;
import com.nairaland.snakevpn.R;
import com.nairaland.snakevpn.SharedPreference;
import com.nairaland.snakevpn.interfaces.ChangeServer;
import com.nairaland.snakevpn.model.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import de.blinkt.openvpn.core.VpnStatus;

public class ConfigActivity extends AppCompatActivity implements ChangeServer {

    private Server server;
    private CheckInternetConnection connection;
    private OpenVPNThread vpnThread = new OpenVPNThread();
    private OpenVPNService vpnService = new OpenVPNService();
    boolean vpnStart = false;
    private SharedPreference preference;

    String urls = "";

    ImageView vpnBtn, next;

    TextView privacy, term;

    String privacytx, termtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        int SDK_INT = android.os.Build.VERSION.SDK_INT; // Allow connection network on main activity
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        vpnBtn = findViewById(R.id.vpnBtn); // Init
        next = findViewById(R.id.next);
        privacy = findViewById(R.id.privacy);
        term = findViewById(R.id.term);

        SharedPreferences preference = getSharedPreferences("key", Context.MODE_PRIVATE); // Get data from preference
        privacytx = preference.getString("privacy", "");
        termtx = preference.getString("term", "");

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ConfigActivity.this, DetailActivity.class); // Go to detail activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("link", privacytx);
                startActivity(intent);

            }
        });

        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ConfigActivity.this, DetailActivity.class); // Go to detail activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("link", termtx);
                startActivity(intent);

            }
        });

        vpnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences tr = getSharedPreferences("key", Context.MODE_PRIVATE); // Set preferences "term"
                SharedPreferences.Editor edt = tr.edit();
                edt.putString("splash", "1");
                edt.putString("value", "0");
                edt.apply();

                if (vpnStart) {
                    confirmDisconnect();
                }else {
                    prepareVpn();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ConfigActivity.this, MainActivity.class));
                finish();

            }
        });

        isServiceRunning(); // Checking is vpn already running or not
        VpnStatus.initLogCache(ConfigActivity.this.getCacheDir());

        initializeAll();
    }

    private void initializeAll() {

        preference = new SharedPreference(ConfigActivity.this);
        server = preference.getServer();

        connection = new CheckInternetConnection();
        LocalBroadcastManager.getInstance(ConfigActivity.this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
    }

    public void confirmDisconnect(){

        AlertDialog.Builder builder = new AlertDialog.Builder(ConfigActivity.this);
        builder.setMessage(ConfigActivity.this.getString(R.string.connection_close_confirm));

        builder.setPositiveButton(ConfigActivity.this.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stopVpn();
            }
        });
        builder.setNegativeButton(ConfigActivity.this.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog dialog = builder.create(); // Create the AlertDialog
        dialog.show();
    }

    private void prepareVpn() {

        if (!vpnStart) {

            if (getInternetStatus()) {

                Intent intent = VpnService.prepare(ConfigActivity.this); // Checking permission for network monitor

                if (intent != null) {
                    startActivityForResult(intent, 1);
                } else startVpn();//have already permission

                status("connecting"); // Update confection status

            } else {

                // No internet connection available
                showToast("you have no internet connection !!");
            }

        } else if (stopVpn()) {

            // VPN is stopped, show a Toast message.
            showToast("Disconnect Successfully");
        }
    }

    public boolean stopVpn() {
        try {
            vpnThread.stop();

            status("connect");
            vpnStart = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            startVpn(); //Permission granted, start the VPN

        } else {

            showToast("Permission Deny !! ");
        }
    }

    public boolean getInternetStatus() {

        return connection.netCheck(ConfigActivity.this);


    }

    public void isServiceRunning() {

        setStatus(vpnService.getStatus());

    }

    private void startVpn() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                // Do work
                ConfigActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        urls = getSiteString(server.getOvpn());

                    }
                });

                // After finish work
                ConfigActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        vpnStart = true;

                    }
                });

            }
        }).start();
    }

    private String getSiteString(String site){

        String config = "";

        try {

            URL url = new URL(site);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader( new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line;

                while (true) {
                    line = br.readLine();
                    if (line == null) break;
                    config += line + "\n";
                }

                br.readLine();
                OpenVpnApi.startVpn(ConfigActivity.this, config, server.getCountry(), server.getOvpnUserName(), server.getOvpnUserPassword());
            }


        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }

        return config;
    }

    public void setStatus(String connectionState) {
        if (connectionState!= null)

            switch (connectionState) {
                case "DISCONNECTED":
                    status("connect");
                    vpnStart = false;
                    vpnService.setDefaultStatus();
                    break;
                case "CONNECTED":
                    vpnStart = true;// it will use after restart this activity
                    status("connected");
                    break;
                case "WAIT":
                    break;
                case "AUTH":
                    break;
                case "RECONNECTING":
                    status("connecting");
                    break;
                case "NONETWORK":
                    break;
            }

    }

    public void status(String status) {

        if (status.equals("connect")) {

            vpnBtn.setBackgroundResource(R.drawable.button_agree);
            vpnBtn.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);

        } else if (status.equals("connecting")) {

            stopVpn();

            vpnBtn.setBackgroundResource(R.drawable.button_agree);
            vpnBtn.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);


        } else if (status.equals("connected")) {

            vpnBtn.setBackgroundResource(R.drawable.button_agree);

        } else if (status.equals("tryDifferentServer")) {

            vpnBtn.setBackgroundResource(R.drawable.button_agree);

        } else if (status.equals("loading")) {

            vpnBtn.setBackgroundResource(R.drawable.button_agree);

        } else if (status.equals("invalidDevice")) {

            vpnBtn.setBackgroundResource(R.drawable.button_agree);

        } else if (status.equals("authenticationCheck")) {

            vpnBtn.setBackgroundResource(R.drawable.button_agree);

        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                setStatus(intent.getStringExtra("state"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    public void showToast(String message) {
        Toast.makeText(ConfigActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void newServer(Server server) {
        this.server = server;

        if (vpnStart) { // Stop previous connection
            stopVpn();
        }

        prepareVpn();

    }

    @Override
    public void onResume() {

        if (server == null) {
            server = preference.getServer();
        }

        super.onResume();

    }

    /**
     * Save current selected server on local shared preference
     */
    @Override
    public void onStop() {

        if (server != null) {
            preference.saveServer(server);
        }

        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}