package com.example.signal.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signal.Adapter.TicketAdapter;
import com.example.signal.Helper.MyApplication;
import com.example.signal.Model.TicketModel;
import com.example.signal.R;
import com.example.signal.databinding.UnreadBinding;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Unread extends Fragment {
    private static String url = "https://vprofit.info/api/support/all?filter=unread&q&per_page=10";
    private  RecyclerView recyclerView;
    private List<TicketModel> ticketModels;
    String token;
    EditText search;
    MaterialCardView newmessage;
    private ProgressBar loadingPB;
    RequestQueue requestQueue;
    private TicketAdapter ticketAdapter;
    String reload;
    private UnreadBinding binding;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            reload = intent.getStringExtra("message");


            if (reload.equals("1")){
                MakeVolleyConnection();
            }

            //setupRv();



        }
    };



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = UnreadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("custom-action-local-broadcast"));


        SharedPreferences sp =getActivity().getSharedPreferences("Prefs", Activity.MODE_PRIVATE);

        token  = sp.getString("token","");

        newmessage = root.findViewById(R.id.newmessage);
        recyclerView=root.findViewById(R.id.recyclerview);
        search = root.findViewById(R.id.search);
        loadingPB =root.findViewById(R.id.idLoadingPB);
        requestQueue = Volley.newRequestQueue(getActivity());
        ticketModels = new ArrayList<>();



        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String url1 = search.getText().toString();

                    url= "https://vprofit.info/api/support/all?filter=unread&q="+url1+"&per_page=10";
                    MakeVolleyConnection();
                    return true;
                }
                return false;
            }
        });


        newmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMessageSheet bottomSheet = new NewMessageSheet();
                bottomSheet.show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        MakeVolleyConnection();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void MakeVolleyConnection() {
        ticketModels = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.
                Method.GET, url, null, new Response.Listener<JSONObject>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {

                loadingPB.setVisibility(View.GONE);
                try {

                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject userData = dataArray.getJSONObject(i);

                        JSONObject lastest= dataArray.getJSONObject(0);
                        JSONObject lastmessage= lastest.getJSONObject("latest");
                        TicketModel recyclerViewData = new TicketModel("","","","","","","");
                        recyclerViewData.setId(userData.getString("id"));
                        recyclerViewData.setTicket_id(userData.getString("ticket_id"));
                        recyclerViewData.setSubject(userData.getString("subject"));
                        recyclerViewData.setHumanDate(userData.getString("humanDate"));
                        recyclerViewData.setDepartment(userData.getString("department"));
                        recyclerViewData.setPriority(userData.getString("priority"));
                        recyclerViewData.setMessage(lastmessage.getString("message"));

                        ticketModels.add(recyclerViewData);

                    }


                    ticketAdapter = new TicketAdapter(ticketModels, getActivity());
                    recyclerView.setAdapter(ticketAdapter);
                    ticketAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Accept", "application/json");
                headerMap.put("Authorization", "Bearer " + token);

                return headerMap;
            }
        };


        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }


}

