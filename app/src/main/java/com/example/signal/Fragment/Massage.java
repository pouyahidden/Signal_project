package com.example.signal.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.signal.Adapter.TicketAdapter;
import com.example.signal.Helper.MyApplication;
import com.example.signal.Model.TicketModel;
import com.example.signal.R;
import com.example.signal.databinding.MassageBinding;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Massage extends Fragment {

    private static String url = "https://vprofit.info/api/support/all?filter=&q&per_page=10";
    MaterialCardView newmessage;
    String token;
    EditText search;
    private MassageBinding binding;
    private ProgressBar loadingPB;
    private RecyclerView recyclerView;
    private List<TicketModel> ticketModels;
    private TicketAdapter ticketAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = MassageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sp = getActivity().getSharedPreferences("Prefs", Activity.MODE_PRIVATE);

        token = sp.getString("token", "");
        newmessage = root.findViewById(R.id.newmessage);
        recyclerView = root.findViewById(R.id.recyclerview);
        search = root.findViewById(R.id.search);
        ticketModels = new ArrayList<>();
        loadingPB = root.findViewById(R.id.idLoadingPB);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String url1 = search.getText().toString();

                    url = "https://vprofit.info/api/support/all?filter=&q=" + url1 + "&per_page=10";
                    MakeVolleyConnection();
                    return true;
                }
                return false;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        MakeVolleyConnection();
        newmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMessageSheet bottomSheet = new NewMessageSheet();
                bottomSheet.show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
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
                        TicketModel recyclerViewData = new TicketModel("id", "", "", "", "", "", "");
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
                    e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject data = new JSONObject(responseBody);
                    String message = data.getString("message");
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Accept", "application/json");
                headerMap.put("Authorization", "Bearer " + token);
                return headerMap;
            }};
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}