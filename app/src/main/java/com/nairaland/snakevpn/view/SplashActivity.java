package com.nairaland.snakevpn.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.nairaland.snakevpn.BuildConfig;
import com.nairaland.snakevpn.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.android.billingclient.api.BillingClient.SkuType.SUBS;

public class SplashActivity extends AppCompatActivity implements PurchasesUpdatedListener  {

    TextView version;

    String versionname;

    ImageView logo;

    SharedPreferences pref;

    public String privacy, term, banner, inters, nativeid, commer, pay, keyy, checkcode;

    private static final String APP_PREFS_NAME = "key";
    private static final String PRIVACY = "privacy";
    private static final String TERM = "term";

    public static final String PREF_FILE= "MyPref";
    public static final String SUBSCRIBE_KEY= "subscribe";

    private BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MobileAds.initialize(this, new OnInitializationCompleteListener() { // Init admob
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        version = findViewById(R.id.text_version); // Init
        logo = findViewById(R.id.logo);

        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build(); // Init google play
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                    Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(SUBS);
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if(queryPurchases!=null && queryPurchases.size()>0){

                        pref = getSharedPreferences(PREF_FILE, 0);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putBoolean(SUBSCRIBE_KEY, true);
                        edit.apply();

                    }

                    else{

                        pref = getSharedPreferences(PREF_FILE, 0);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putBoolean(SUBSCRIBE_KEY, false);
                        edit.apply();

                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(getApplicationContext(),"Service Disconnected",Toast.LENGTH_SHORT).show();
            }
        });

        versionname = BuildConfig.VERSION_NAME; // Get version app
        version.setText("Version "+ versionname); // Set version app

        checkDatabase(); // Check database privacy and term

        checkCommercial();
    }

    private void checkCommercial() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tools").child("commercial").child("value");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                commer = snapshot.getValue().toString();

                if (commer.equals("yes")){

                    checkAds(); // Check Ads ID

                    SharedPreferences pref = getSharedPreferences("key", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("commercial", "yes");
                    edit.apply();

                }else {

                    SharedPreferences pref = getSharedPreferences("key", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("commercial", "no");
                    edit.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Tools").child("paid").child("value");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                pay = snapshot.getValue().toString();

                if (pay.equals("yes")){

                    SharedPreferences pref = getSharedPreferences("key", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("paid", "yes");
                    edit.apply();

                }else {

                    SharedPreferences pref = getSharedPreferences("key", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("paid", "no");
                    edit.apply();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        pref = getSharedPreferences("key", MODE_PRIVATE);
        checkcode = pref.getString("splash", "");

        if (checkcode != null && checkcode.equals("1")){ // If version code in database = in app

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();

                }
            }, 3000); // 3000 means 3 second

        }else{ // If version code in database != app, go to Updatescreen

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this, ConfigActivity.class));
                    finish();

                }
            }, 3000);
        }
    }

    private void checkDatabase() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tools").child("privacy").child("value"); // Init database

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                privacy = snapshot.getValue().toString(); // Get database value

                SharedPreferences pr = getSharedPreferences("key", MODE_PRIVATE); // Init preference
                String value = pr.getString("privacy", ""); // Get data from preferences with id "privacy"

                if (privacy.equals(value)){

                    Log.d("Data", privacy+value); // Send message in log

                }else {

                    SharedPreferences pf = getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE); // Set preferences "privacy"
                    SharedPreferences.Editor editor = pf.edit();
                    editor.putString(PRIVACY, privacy);
                    editor.apply();

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        DatabaseReference refterm = FirebaseDatabase.getInstance().getReference("Tools").child("term").child("value"); // Init database

        refterm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                term = snapshot.getValue().toString(); // Get database value

                SharedPreferences pr = getSharedPreferences("key", MODE_PRIVATE); // Init preference
                String value = pr.getString("term", ""); // Get data from preferences with id "term"

                if (term.equals(value)){

                    Log.d("Data", term+value);

                }else {

                    SharedPreferences tr = getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE); // Set preferences "term"
                    SharedPreferences.Editor edt = tr.edit();
                    edt.putString(TERM, term);
                    edt.apply();

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Tools").child("gplay").child("value");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                keyy = snapshot.getValue().toString();

                SharedPreferences sp = getSharedPreferences("key", MODE_PRIVATE);
                String val = sp.getString("gpkey", "");

                if (keyy.equals(val)){

                    Log.d("Data: ", keyy+val);

                }else {

                    SharedPreferences sps = getSharedPreferences("key", MODE_PRIVATE);
                    SharedPreferences.Editor edir = sps.edit();
                    edir.putString("gpkey", keyy);
                    edir.apply();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void checkAds(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ads").child("bannerid").child("value");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                banner = snapshot.getValue().toString();

                SharedPreferences bnr = getSharedPreferences("key", MODE_PRIVATE);
                String value = bnr.getString("bannerid", "");

                if (banner.equals(value)){

                    Log.d("Data :", banner+value);

                }else {

                    SharedPreferences br = getSharedPreferences("key", MODE_PRIVATE);
                    SharedPreferences.Editor edit = br.edit();
                    edit.putString("bannerid", banner);
                    edit.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Ads").child("intersid_lite").child("value");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                inters = snapshot.getValue().toString();

                SharedPreferences inte = getSharedPreferences("key", MODE_PRIVATE);
                String value = inte.getString("intersid", "");

                if (inters.equals(value)){

                    Log.d("data :", inters+value);
                }else {

                    SharedPreferences ter = getSharedPreferences("key", MODE_PRIVATE);
                    SharedPreferences.Editor edit = ter.edit();
                    edit.putString("intersid", inters);
                    edit.apply();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Ads").child("nativeid_lite").child("value");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                nativeid = snapshot.getValue().toString();

                SharedPreferences nat = getSharedPreferences("key", MODE_PRIVATE);
                String nati = nat.getString("nativeid", "");

                if (nativeid.equals(nati)){

                    Log.d("Data :", nativeid+nati);

                }else {

                    SharedPreferences nativ = getSharedPreferences("key", MODE_PRIVATE);
                    SharedPreferences.Editor editor = nativ.edit();
                    editor.putString("nativeid", nativeid);
                    editor.apply();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

    }
}