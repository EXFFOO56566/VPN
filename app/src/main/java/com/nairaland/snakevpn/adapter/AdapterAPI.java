package com.nairaland.snakevpn.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nairaland.snakevpn.R;
import com.nairaland.snakevpn.model.Api;
import com.nairaland.snakevpn.view.MainActivity;

import java.util.List;

public class AdapterAPI extends RecyclerView.Adapter<AdapterAPI.ViewHolder> {

    private static final String APP_PREFS_NAME = "AtheiaVPNPreference";

    private static final String SERVER_COUNTRY = "server_country";
    private static final String SERVER_FLAG = "server_flag";
    private static final String SERVER_OVPN = "server_ovpn";
    private static final String SERVER_OVPN_USER = "server_ovpn_user";
    private static final String SERVER_OVPN_PASSWORD = "server_ovpn_password";

    public String fl, flag;

    String flagUrl;

    Context context;

    List<Api> apiList;

    public AdapterAPI(Context context, List<Api> apiList) {
        this.context = context;
        this.apiList = apiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.server_list_view_api, parent, false);

        return new AdapterAPI.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Api model = apiList.get(position);

        holder.country.setText(model.getField6());

        flag = model.getField7();

        if (flag.equals("AT")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/austria/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("AR")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/argentina/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("AU")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/australia/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("AE")){
            Glide.with(context).load("https://www.countryflags.com/wp-content/uploads/united-arab-emirates-flag-png-large.png").into(holder.icon);
        }
        else if (flag.equals("BE")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/belgium/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("BH")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/bahrain/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("BR")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/brazil/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("BY")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/belarus/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("BG")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/bulgaria/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("CA")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/canada/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("CL")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/chile/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("CN")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/china/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("CO")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/colombia/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("CR")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/costa-rica/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("HR")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/croatia/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("CZ")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/czech-republic/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("DK")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/denmark/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("EC")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/ecuador/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("EG")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/egypt/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("EE")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/estonia/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("FI")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/finland/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("FR")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/france/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("DE")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/germany/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("GE")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/greece/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("HK")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/hongkong/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("HU")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/hungary/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("IS")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/iceland/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("ID")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/indonesia/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("IT")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/italy/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("JP")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/japan/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("jp")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/japan/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("KZ")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/kazakhstan/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("MY")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/malaysia/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("NL")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/netherlands/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("PE")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/peru/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("PH")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/philippines/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("SG")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/singapore/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("KR")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/south-korea/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("ES")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/spain/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("TW")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/taiwan/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("TH")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/thailand/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("GB")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/united-kingdom/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("US")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/united-states-of-america/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("VN")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/vietnam/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("RU")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/russia/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("MX")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/mexico/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("IN")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/india/flag-400.png").into(holder.icon);
        }
        else if (flag.equals("RS")){
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/serbia/flag-400.png").into(holder.icon);
        }
        else{
            Glide.with(context).load("https://cdn.countryflags.com/thumbs/monaco/flag-400.png").into(holder.icon);
        }

        Glide.with(context).load("https://cdn.statically.io/gh/kodlitecom/Analogy/2d4a3914/css/quality_high.png").into(holder.signal);

        holder.serverItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = model.getField7();

                if (flag.equals("AT")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/austria/flag-400.png";
                }
                else if (flag.equals("AR")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/argentina/flag-400.png";
                }
                else if (flag.equals("AU")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/australia/flag-400.png";
                }
                else if (flag.equals("AE")){
                    flagUrl = "https://www.countryflags.com/wp-content/uploads/united-arab-emirates-flag-png-large.png";
                }
                else if (flag.equals("BE")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/belgium/flag-400.png";
                }
                else if (flag.equals("BH")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/bahrain/flag-400.png";
                }
                else if (flag.equals("BR")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/brazil/flag-400.png";
                }
                else if (flag.equals("BY")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/belarus/flag-400.png";
                }
                else if (flag.equals("BG")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/bulgaria/flag-400.png";
                }
                else if (flag.equals("CA")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/canada/flag-400.png";
                }
                else if (flag.equals("CL")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/chile/flag-400.png";
                }
                else if (flag.equals("CN")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/china/flag-400.png";
                }
                else if (flag.equals("CO")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/colombia/flag-400.png";
                }
                else if (flag.equals("CR")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/costa-rica/flag-400.png";
                }
                else if (flag.equals("HR")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/croatia/flag-400.png";
                }
                else if (flag.equals("CZ")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/czech-republic/flag-400.png";
                }
                else if (flag.equals("DK")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/denmark/flag-400.png";
                }
                else if (flag.equals("EC")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/ecuador/flag-400.png";
                }
                else if (flag.equals("EG")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/egypt/flag-400.png";
                }
                else if (flag.equals("EE")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/estonia/flag-400.png";
                }
                else if (flag.equals("FI")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/finland/flag-400.png";
                }
                else if (flag.equals("FR")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/france/flag-400.png";
                }
                else if (flag.equals("DE")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/germany/flag-400.png";
                }
                else if (flag.equals("GE")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/greece/flag-400.png";
                }
                else if (flag.equals("HK")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/hongkong/flag-400.png";
                }
                else if (flag.equals("HU")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/hungary/flag-400.png";
                }
                else if (flag.equals("IS")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/iceland/flag-400.png";
                }
                else if (flag.equals("ID")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/indonesia/flag-400.png";
                }
                else if (flag.equals("JP")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/japan/flag-400.png";
                }
                else if (flag.equals("jp")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/japan/flag-400.png";
                }
                else if (flag.equals("KZ")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/kazakhstan/flag-400.png";
                }
                else if (flag.equals("IT")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/italy/flag-400.png";
                }
                else if (flag.equals("MY")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/malaysia/flag-400.png";
                }
                else if (flag.equals("NL")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/netherlands/flag-400.png";
                }
                else if (flag.equals("PE")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/peru/flag-400.png";
                }
                else if (flag.equals("PH")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/philippines/flag-400.png";
                }
                else if (flag.equals("SG")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/singapore/flag-400.png";
                }
                else if (flag.equals("KR")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/south-korea/flag-400.png";
                }
                else if (flag.equals("ES")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/spain/flag-400.png";
                }
                else if (flag.equals("TW")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/taiwan/flag-400.png";
                }
                else if (flag.equals("TH")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/thailand/flag-400.png";
                }
                else if (flag.equals("GB")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/united-kingdom/flag-400.png";
                }
                else if (flag.equals("US")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/united-states-of-america/flag-400.png";
                }
                else if (flag.equals("VN")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/vietnam/flag-400.png";
                }
                else if (flag.equals("RU")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/russia/flag-400.png";
                }
                else if (flag.equals("MX")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/mexico/flag-400.png";
                }
                else if (flag.equals("IN")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/india/flag-400.png";
                }
                else if (flag.equals("RS")){
                    flagUrl = "https://cdn.countryflags.com/thumbs/serbia/flag-400.png";
                }
                else{
                    flagUrl = "https://cdn.countryflags.com/thumbs/monaco/flag-400.png";
                }

                final String country = model.getField6();
                final String ovpn = model.getField15();
                final String ovpnUserName = "vpn";
                final String ovpnUserPassword = "vpn";

                SharedPreferences mPreference = context.getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor mPrefEditor = mPreference.edit();
                mPrefEditor.putString(SERVER_COUNTRY, country);
                mPrefEditor.putString(SERVER_FLAG, flagUrl);
                mPrefEditor.putString(SERVER_OVPN, ovpn);
                mPrefEditor.putString(SERVER_OVPN_USER, ovpnUserName);
                mPrefEditor.putString(SERVER_OVPN_PASSWORD, ovpnUserPassword);
                mPrefEditor.apply();

                SharedPreferences pref = context.getSharedPreferences("key", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefs = pref.edit();
                prefs.putString("adapter", "api");
                prefs.apply();

                Toast.makeText(context, "Server Selected", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return apiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon, signal;
        TextView country;
        RelativeLayout serverItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serverItemLayout = itemView.findViewById(R.id.serverItemLayout);
            icon = itemView.findViewById(R.id.iconImg);
            signal = itemView.findViewById(R.id.signal);
            country = itemView.findViewById(R.id.countryTv);
        }
    }
}
