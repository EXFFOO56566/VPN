package com.nairaland.snakevpn.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;
import com.ramadhanhadiatma.admob_advanced_native_recyvlerview.AdmobNativeAdAdapter;
import com.nairaland.snakevpn.R;
import com.nairaland.snakevpn.adapter.AdapterAPI;
import com.nairaland.snakevpn.adapter.ServerAdapter;
import com.nairaland.snakevpn.interfaces.ServerAPI;
import com.nairaland.snakevpn.model.Api;
import com.nairaland.snakevpn.model.Server;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ServerFreeFragment extends Fragment {

    public static ServerFreeFragment getInstance(){
        ServerFreeFragment serverFreeFragment = new ServerFreeFragment();
        return serverFreeFragment;
    }

    RecyclerView recyclerView, recyclerViewapi;

    ServerAdapter serverAdapter;
    AdapterAPI adapterAPI;
    List<Server> serverList;
    List<Api> apiList;

    SharedPreferences pref, prefs;

    TextView nodata1, nodata2;

    boolean prem;

    public static final String PREF_FILE= "MyPref";
    public static final String SUBSCRIBE_KEY= "subscribe";

    public String nativeid, place;

    DatabaseReference reference;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_server_free, container, false);

        recyclerView = root.findViewById(R.id.recycler);
        recyclerViewapi = root.findViewById(R.id.recyclerapi);
        nodata1 = root.findViewById(R.id.nodata1);
        nodata2 = root.findViewById(R.id.nodata2);

        nodata1.setVisibility(View.VISIBLE);
        nodata2.setVisibility(View.VISIBLE);

        prefs = getActivity().getSharedPreferences(PREF_FILE, 0);
        prem = prefs.getBoolean(SUBSCRIBE_KEY, false);

        pref = getActivity().getSharedPreferences("key", MODE_PRIVATE);
        nativeid = pref.getString("nativeid", "");
        place = pref.getString("commercial", "");

        recyclerView.setHasFixedSize(true); // Set Recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        serverList = new ArrayList<>();
        serverAdapter = new ServerAdapter(getContext(), serverList);

        apiList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                //Add your api url
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerAPI serverAPI = retrofit.create(ServerAPI.class);

        Call<List<Api>> call = serverAPI.getApis();

        if (place.equals("yes") && !prem){
            AdmobNativeAdAdapter admobNativeAdAdapter= AdmobNativeAdAdapter.Builder
                    .with(
                            nativeid,//Create a native ad id from admob console
                            serverAdapter,//The adapter you would normally set to your recyClerView
                            "small"//Set it with "small","medium" or "custom"
                    )
                    .adItemIterval(10)//native ad repeating interval in the recyclerview
                    .build();
            recyclerView.setAdapter(admobNativeAdAdapter);//set your RecyclerView adapter with the admobNativeAdAdapter

            call.enqueue(new Callback<List<Api>>() {
                @Override
                public void onResponse(Call<List<Api>> call, Response<List<Api>> response) {

                    if (response.code() != 200){
                        return;
                    }

                    List<Api> apis = response.body();

                    for (Api api : apis){

                        nodata2.setVisibility(View.GONE);
                        apiList.add(api);

                    }

                    PutDataRecycler(apiList);

                    AdmobNativeAdAdapter admobNativeAdAdapter1= AdmobNativeAdAdapter.Builder
                            .with(
                                    nativeid,//Create a native ad id from admob console
                                    adapterAPI,//The adapter you would normally set to your recyClerView
                                    "small"//Set it with "small","medium" or "custom"
                            )
                            .adItemIterval(12)//native ad repeating interval in the recyclerview
                            .build();
                    recyclerViewapi.setAdapter(admobNativeAdAdapter1);//set your RecyclerView adapter with the admobNativeAdAdapter

                }

                @Override
                public void onFailure(Call<List<Api>> call, Throwable t) {

                }
            });
        }else {
            recyclerView.setAdapter(serverAdapter);

            call.enqueue(new Callback<List<Api>>() {
                @Override
                public void onResponse(Call<List<Api>> call, Response<List<Api>> response) {

                    if (response.code() != 200){
                        return;
                    }

                    List<Api> apis = response.body();

                    for (Api api : apis){

                        nodata2.setVisibility(View.GONE);
                        apiList.add(api);

                    }

                    PutDataRecycler(apiList);

                    recyclerViewapi.setAdapter(adapterAPI);
                }

                @Override
                public void onFailure(Call<List<Api>> call, Throwable t) {

                }
            });
        }

        readData();

        return root;
    }

    private void PutDataRecycler(List<Api> apiList) {

        recyclerViewapi.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        recyclerViewapi.setLayoutManager(linearLayoutManager1);
        adapterAPI = new AdapterAPI(getContext(), apiList);
    }

    private void readData(){

        reference = FirebaseDatabase.getInstance().getReference("Server").child("free"); // Get data server from database

        Query query = reference.orderByChild("country");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    nodata1.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
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
