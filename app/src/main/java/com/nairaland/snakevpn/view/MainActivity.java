package com.nairaland.snakevpn.view;

import androidx.annotation.NonNull;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.nairaland.snakevpn.CheckInternetConnection;
import com.nairaland.snakevpn.R;

import com.nairaland.snakevpn.SharedPreference;
import com.nairaland.snakevpn.admob.NativeTemplateStyle;
import com.nairaland.snakevpn.admob.TemplateView;
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
import de.hdodenhof.circleimageview.CircleImageView;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity implements ChangeServer {

    private Server server;
    private CheckInternetConnection connection;
    private OpenVPNThread vpnThread = new OpenVPNThread();
    private OpenVPNService vpnService = new OpenVPNService();
    boolean vpnStart = false;
    private SharedPreference preference;

    String urls = "";

    String adapter, msg;

    ImageView vpnBtn, downholder, upholder;

    TextView logTv, durationTv, byteInTv, byteOutTv, serverTxt, premium, textconn;

    CircleImageView selectedServerIcon;

    RelativeLayout holder_server, getpro, bottomsheet, banner_menu;

    SharedPreferences pref, prefs, checking, trycon;

    public String place, inte, pai, banner, value, chserver, str, nati;

    boolean prem;

    public static final String PREF_FILE= "MyPref";
    public static final String SUBSCRIBE_KEY= "subscribe";

    private InterstitialAd mInterstitialAd;

    private String unityGameID = "4075387"; //
    private Boolean testMode = true;
    private String inters_id = "inters";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int SDK_INT = android.os.Build.VERSION.SDK_INT; // Allow connection network on main activity
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        bottomsheet = findViewById(R.id.bottom_sheet); // Init bottomsheet
        vpnBtn = findViewById(R.id.vpnBtn); // Init
        logTv = findViewById(R.id.logTv);
        durationTv = findViewById(R.id.durationTv);
        byteInTv = findViewById(R.id.byteInTv);
        byteOutTv = findViewById(R.id.byteOutTv);
        selectedServerIcon = findViewById(R.id.selectedServerIcon);
        serverTxt = findViewById(R.id.txt_server);
        downholder = findViewById(R.id.downholder);
        upholder = findViewById(R.id.upholder);
        holder_server = findViewById(R.id.holder_server);
        banner_menu = findViewById(R.id.banner_menu);
        getpro = findViewById(R.id.getpro);
        premium = findViewById(R.id.text_premium);
        textconn = findViewById(R.id.textconn);

        prefs = getSharedPreferences(PREF_FILE, 0);
        prem = prefs.getBoolean(SUBSCRIBE_KEY, false);

        pref = getSharedPreferences("key", MODE_PRIVATE);
        value = pref.getString("value", ""); // Get data from preferences with id "mark"
        inte = pref.getString("intersid", "");
        banner = pref.getString("bannerid", "");
        place = pref.getString("commercial", "");
        pai = pref.getString("paid", "");
        nati = pref.getString("nativeid", "");

        vpnBtn.setEnabled(false);

        UnityInterstitialAdsListener interstitialAdsListener = new UnityInterstitialAdsListener();
        UnityAds.initialize(this, unityGameID, testMode);

        if (value.equals("0")){

            showCase();

        }else {

            vpnBtn.setEnabled(false);

        }

        checking = getSharedPreferences("AtheiaVPNPreference", MODE_PRIVATE); // init preferences
        chserver = checking.getString("server_ovpn_user", "");

        if (chserver.equals("first")){ // check if preferences is null

            vpnBtn.setEnabled(true);

            vpnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(MainActivity.this, "You need to select server first", Toast.LENGTH_SHORT).show();

                }
            });

        }else {

            vpnBtn.setEnabled(true);

            vpnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pref = getSharedPreferences("key", MODE_PRIVATE);
                    place = pref.getString("commercial", "");

                    if (place.equals("yes") && !prem){

                        showInters();

                    }

                    if (vpnStart) {
                        confirmDisconnect();
                    }else {
                        prepareVpn();
                    }

                }
            });

        }

        isServiceRunning(); // Checking is vpn already running or not
        VpnStatus.initLogCache(getCacheDir());

        tutorial();

        initializeAll();

        if (pai.equals("no")){
            getpro.setVisibility(View.GONE);
        }else {
            getpro.setVisibility(View.VISIBLE);
        }

        if (prem){
            premium.setText("SantuyVPN Lite PRO");
        }else {
            premium.setVisibility(View.GONE);
        }

        if (place.equals("yes") && !prem){

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                    createPersonalizedAd();

                    AdLoader adLoader = new AdLoader.Builder(MainActivity.this, nati)
                            .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                                @Override
                                public void onNativeAdLoaded(NativeAd NativeAd) {
                                    // Show the ad.

                                    Log.d(TAG, "nativeadLoaded");

                                    if (isDestroyed()) {
                                        NativeAd.destroy();
                                        return;
                                    }

                                    NativeTemplateStyle styles = new
                                            NativeTemplateStyle.Builder().build();

                                    TemplateView template = findViewById(R.id.my_template);
                                    template.setStyles(styles);
                                    template.setVisibility(View.VISIBLE);
                                    template.setNativeAd(NativeAd);
                                }
                            })
                            .withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(LoadAdError adError) {

                                    Log.d(TAG, "nativeadLoaded"); // Handle the failure by logging, altering the UI, and so on.

                                }
                            })
                            .withNativeAdOptions(new NativeAdOptions.Builder()
                                    .build()) // Methods in the NativeAdOptions.Builder class can be
                            .build();
                    adLoader.loadAd(new AdRequest.Builder().build());
                }
            });

        }

        getpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, MembershipActivity.class));

            }
        });

        holder_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, ServerActivity.class));

            }
        });

    }

    private void createPersonalizedAd() {

        AdRequest adRequest = new AdRequest.Builder().build();

        createInterstitialAd(adRequest);

    }

    private void createInterstitialAd(AdRequest adRequest){

        InterstitialAd.load(MainActivity.this,inte, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                mInterstitialAd = interstitialAd;
                mInterstitialAd = interstitialAd;
                Log.d("...admob", "onAdLoaded");

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("admob", "The ad was dismissed.");
                        createPersonalizedAd();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("admob", "The ad failed to show.");
                        createPersonalizedAd();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("admob", "The ad was shown.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d("...admob", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

    }

    private class UnityInterstitialAdsListener implements IUnityAdsListener {

        @Override
        public void onUnityAdsReady(String s) {

        }

        @Override
        public void onUnityAdsStart(String s) {

        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {

        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

        }
    }

    private void showInters(){

        /*if (mInterstitialAd != null) {
            mInterstitialAd.show(MainActivity.this);
        } else {
            Log.d("...admob", "The interstitial ad wasn't ready yet.");
        }*/

        UnityAds.show(MainActivity.this, inters_id);

    }

    /**
     * Initialize all variable and object
     */
    private void initializeAll() {

        preference = new SharedPreference(MainActivity.this);
        server = preference.getServer();

        // Update current selected server icon
        updateCurrentServerIcon(server.getFlagUrl());
        updateCurrentServer(server.getCountry());

        connection = new CheckInternetConnection();
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
    }

    /**
     * Show show disconnect confirm dialog
     */
    public void confirmDisconnect(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getString(R.string.connection_close_confirm));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stopVpn();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Prepare for vpn connect with required permission
     */
    private void prepareVpn() {

        if (!vpnStart) {

            if (getInternetStatus()) {

                Intent intent = VpnService.prepare(MainActivity.this); // Checking permission for network monitor

                if (intent != null) {
                    startActivityForResult(intent, 1);
                } else startVpn();//have already permission

                status("connecting"); // Update confection status

            } else {

                showToast("you have no internet connection !!"); // No internet connection available
            }

        } else if (stopVpn()) {

            showToast("Disconnect Successfully"); // VPN is stopped, show a Toast message.

        }
    }

    /**
     * Stop vpn
     * @return boolean: VPN status
     */
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

    /**
     * Taking permission for network access
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            startVpn(); //Permission granted, start the VPN

        } else {

            showToast("Permission Deny !! ");
        }
    }

    /**
     * Internet connection status.
     */
    public boolean getInternetStatus() {

        return connection.netCheck(MainActivity.this);


    }

    /**
     * Get service status
     */
    public void isServiceRunning() {

        setStatus(vpnService.getStatus());

    }

    /**
     * Start the VPN
     */
    private void startVpn() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() { // Do work

                    @Override
                    public void run() {

                        urls = getSiteString(server.getOvpn());

                    }
                });

                runOnUiThread(new Runnable() {  // After finish work

                    @Override
                    public void run() {

                        logTv.setText("Connecting...");
                        vpnStart = true;

                    }
                });

            }
        }).start();
    }

    private String getSiteString(String site){

        String config = "";

        try {

            trycon = getSharedPreferences("key", MODE_PRIVATE);
            adapter = trycon.getString("adapter", "");

            if (adapter.equals("firebase")){

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
                    OpenVpnApi.startVpn(MainActivity.this, config, server.getCountry(), server.getOvpnUserName(), server.getOvpnUserPassword());
                }
            }else if (adapter.equals("api")){

                str = Decrypt(server.getOvpn());

                OpenVpnApi.startVpn(MainActivity.this, str, server.getCountry(), server.getOvpnUserName(), server.getOvpnUserPassword());

            }


        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }

        return config;
    }

    public String Decrypt(String text){
        byte[] msg1 = Base64.decode(text, Base64.DEFAULT);
        msg = new String(msg1);

        return msg;
    }

    /**
     * Status change with corresponding vpn connection status
     * @param connectionState
     */
    public void setStatus(String connectionState) {
        if (connectionState!= null)

            switch (connectionState) {
                case "DISCONNECTED":
                    status("connect");
                    vpnStart = false;
                    vpnService.setDefaultStatus();
                    logTv.setText("Disconnected");
                    break;
                case "CONNECTED":
                    vpnStart = true;// it will use after restart this activity
                    status("connected");
                    logTv.setText("Connected");
                    break;
                case "WAIT":
                    logTv.setText("Waiting for Configuration");
                    break;
                case "AUTH":
                    logTv.setText("Server Authenticating");
                    break;
                case "RECONNECTING":
                    status("connecting");
                    logTv.setText("Reconnecting...");
                    break;
                case "NONETWORK":
                    logTv.setText("No Network Connection");
                    break;
                case "EXITING":
                    status("failed");
                    logTv.setText("Failed connect");
                    break;
            }

    }

    /**
     * Change button background color and text
     * @param status: VPN current status
     */
    public void status(String status) {

        if (status.equals("connect")) {

            vpnBtn.setBackgroundResource(R.drawable.but_connect);
            textconn.setText("CONNECT");

        } else if (status.equals("connecting")) {

            vpnBtn.setBackgroundResource(R.drawable.but_connect);
            textconn.setText("CONNECTING");

        } else if (status.equals("connected")) {

            vpnBtn.setBackgroundResource(R.drawable.but_connect);
            textconn.setText("DISCONNECT");

        } else if (status.equals("failed")) {

            vpnBtn.setBackgroundResource(R.drawable.but_connect);

        }
    }

    /**
     * Receive broadcast message
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                setStatus(intent.getStringExtra("state"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = "0 Mb";
                if (byteOut == null) byteOut = "0 Mb";
                updateConnectionStatus(duration, lastPacketReceive, byteIn, byteOut);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * Update status UI
     * @param duration: running time
     * @param lastPacketReceive: last packet receive time
     * @param byteIn: incoming data
     * @param byteOut: outgoing data
     */

    public void updateConnectionStatus(String duration, String lastPacketReceive, String byteIn, String byteOut) {
        durationTv.setText(duration);
        byteInTv.setText(byteIn);
        byteOutTv.setText(byteOut);
    }

    /**
     * Show toast message
     * @param message: toast message
     */
    public void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * VPN server country icon change
     * @param serverIcon: icon URL
     */
    public void updateCurrentServerIcon(String serverIcon) {
        Glide.with(getApplicationContext())
                .load(serverIcon)
                .into(selectedServerIcon);
    }

    public void updateCurrentServer(String serverText) {
        serverTxt.setText(serverText.toUpperCase());
    }

    /**
     * Change server when user select new server
     * @param server ovpn server details
     */
    @Override
    public void newServer(Server server) {
        this.server = server;
        updateCurrentServerIcon(server.getFlagUrl());
        updateCurrentServer(server.getCountry());

        if (vpnStart) { // Stop previous connection
            stopVpn();
        }

        prepareVpn();

    }

    private void tutorial() {


    }

    private void showCase() {

        new GuideView.Builder(MainActivity.this) // Set showcase1
                .setTitle("Connect Button")
                .setContentText("Click here to connect the VPN")
                .setGravity(Gravity.center) //optional
                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(vpnBtn)
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {

                        new GuideView.Builder(MainActivity.this) // Set showcase2
                                .setTitle("VPN Status")
                                .setContentText("VPN status ( connect, disconnect, waiting, authenticating ) show here")
                                .setGravity(Gravity.center) //optional
                                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                .setTargetView(logTv)
                                .setContentTextSize(12)//optional
                                .setTitleTextSize(14)//optional
                                .setGuideListener(new GuideListener() {
                                    @Override
                                    public void onDismiss(View view) {

                                        new GuideView.Builder(MainActivity.this) // Set showcase3
                                                .setTitle("Server Connect")
                                                .setContentText("Showing what server country you will be connect")
                                                .setGravity(Gravity.center) //optional
                                                .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                                .setTargetView(holder_server)
                                                .setContentTextSize(12)//optional
                                                .setTitleTextSize(14)//optional
                                                .setGuideListener(new GuideListener() {
                                                    @Override
                                                    public void onDismiss(View view) {

                                                        startActivity(new Intent(MainActivity.this, ServerActivity.class));

                                                    }
                                                })
                                                .build()
                                                .show();
                                    }
                                })
                                .build()
                                .show();

                    }
                })
                .build()
                .show();
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
