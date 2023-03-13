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

import com.nairaland.snakevpn.R;
import com.nairaland.snakevpn.model.Server;
import com.nairaland.snakevpn.view.MainActivity;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {

    private static final String APP_PREFS_NAME = "AtheiaVPNPreference";

    private static final String SERVER_COUNTRY = "server_country";
    private static final String SERVER_FLAG = "server_flag";
    private static final String SERVER_OVPN = "server_ovpn";
    private static final String SERVER_OVPN_USER = "server_ovpn_user";
    private static final String SERVER_OVPN_PASSWORD = "server_ovpn_password";

    Context context;

    List<Server> serverList;

    public ServerAdapter(Context context, List<Server> serverList) {
        this.context = context;
        this.serverList = serverList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.server_list_view, parent, false);

        return new ServerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        final Server model = serverList.get(position);

        holder.serverCountry.setText(model.getCountry());
        holder.sub.setText(model.getSub());
        Glide.with(context).load(model.getFlagUrl()).into(holder.serverIcon);
        Glide.with(context).load(model.getSort()).into(holder.signal);

        holder.serverItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String country = model.getCountry();
                final String flagUrl = model.getFlagUrl();
                final String ovpn = model.getOvpn();
                final String ovpnUserName = model.getOvpnUserName();
                final String ovpnUserPassword = model.getOvpnUserPassword();

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
                prefs.putString("adapter", "firebase");
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
        return serverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout serverItemLayout;
        ImageView serverIcon, signal;
        TextView serverCountry, sub;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            serverItemLayout = itemView.findViewById(R.id.serverItemLayout);
            serverIcon = itemView.findViewById(R.id.iconImg);
            serverCountry = itemView.findViewById(R.id.countryTv);
            sub = itemView.findViewById(R.id.sub);
            signal = itemView.findViewById(R.id.signal);

        }
    }
}
