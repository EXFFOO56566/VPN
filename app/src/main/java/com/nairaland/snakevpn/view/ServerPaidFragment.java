package com.nairaland.snakevpn.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;
import com.nairaland.snakevpn.R;
import com.nairaland.snakevpn.adapter.ServerAdapter;
import com.nairaland.snakevpn.model.Server;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ServerPaidFragment extends Fragment {

    public static ServerPaidFragment getInstance(){
        ServerPaidFragment serverPaidFragment = new ServerPaidFragment();
        return serverPaidFragment;
    }

    RecyclerView recyclerView;

    ServerAdapter serverAdapter;
    List<Server> serverList;

    SharedPreferences prefs;

    boolean prem;

    public static final String PREF_FILE= "MyPref";
    public static final String SUBSCRIBE_KEY= "subscribe";

    RelativeLayout part;

    TextView premium;

    DatabaseReference reference;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_server_paid, container, false);

        recyclerView = root.findViewById(R.id.recycler); // Init
        part = root.findViewById(R.id.part);
        premium = root.findViewById(R.id.getpremium);

        prefs = getActivity().getSharedPreferences(PREF_FILE, 0); // Get data from shared preferences
        prem = prefs.getBoolean(SUBSCRIBE_KEY, false);

        recyclerView.setHasFixedSize(true); // Set Recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()); // Make layout for recycler
        recyclerView.setLayoutManager(linearLayoutManager); // Set layout for recycler
        serverList = new ArrayList<>(); // Make array for recycler
        serverAdapter = new ServerAdapter(getContext(), serverList); // Set adapter for recycler
        recyclerView.setAdapter(serverAdapter);

        if (prem){ // Logic if prem value = true, will doing this job
            part.setVisibility(View.GONE);

        }else { // When false, doing this job
            part.setVisibility(View.VISIBLE);
        }

        premium.setOnClickListener(new View.OnClickListener() { // Make onclick listener
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MembershipActivity.class));
            }
        });

        readData(); // Go to method

        return root;
    }

    private void readData(){

        reference = FirebaseDatabase.getInstance().getReference("Server").child("paid"); // Get data server from database

        Query query = reference.orderByChild("country");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    serverList.clear();
                    for (DataSnapshot dss: snapshot.getChildren()){
                        final Server model = dss.getValue(Server.class);
                        serverList.add(model);
                    }
                    serverAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
