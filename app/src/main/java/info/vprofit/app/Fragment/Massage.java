package info.vprofit.app.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import info.vprofit.app.Adapter.TicketAdapter;
import info.vprofit.app.Helper.MyApplication;
import info.vprofit.app.Model.Inner;
import info.vprofit.app.Model.Outer;
import info.vprofit.app.Model.TicketModel;
import com.example.signal.R;
import info.vprofit.app.UI.Splash;

import com.example.signal.databinding.MassageBinding;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Massage extends Fragment {

    String url = "";
    MaterialCardView newmessage;
    String token;
    EditText search;
    private MassageBinding binding;
    private ProgressBar loadingPB;
    private RecyclerView recyclerView;
    private List<TicketModel> ticketModels;
    private TicketAdapter ticketAdapter;
    private NestedScrollView nestedSV;
    private int page = 1;
    private final int safahat = 200;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = MassageBinding.inflate(inflater);
        View root = binding.getRoot();

        newmessage = root.findViewById(R.id.newmessage);
        recyclerView = root.findViewById(R.id.recyclerview);
        search = root.findViewById(R.id.search);
        ticketModels = new ArrayList<>();
        loadingPB = root.findViewById(R.id.idLoadingPB);
        nestedSV = root.findViewById(R.id.idNestedSV);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("custom-action-local-broadcast"));
        SharedPreferences sp = getActivity().getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
        token = sp.getString("token", "");

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    MakeVolleyConnection(page, safahat);

                }
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searched = search.getText().toString();
                    url = "https://vprofit.info/api/support/all?filter=&q=" + searched + "&per_page=15&page=1";
                    MakeVolleyConnection(page, safahat);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        MakeVolleyConnection(page, safahat);
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String reload = intent.getStringExtra("message");
           // String title = intent.getStringExtra("title");

            if (reload.equals("1")) {
                reload .equals("");
                refresh(1);
            }
        }
    };
    public void refresh(int milliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                MakeVolleyConnection2(page, safahat);
                ticketAdapter = new TicketAdapter(ticketModels, getActivity());
                TicketModel recyclerViewData = new TicketModel("", "", "", "", "", "", "");
                ticketModels.add(recyclerViewData);
                recyclerView.setAdapter(ticketAdapter);
                ticketAdapter.notifyDataSetChanged();
                recyclerView.setHasFixedSize(true);
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }

    private void MakeVolleyConnection(int page, int safahat) {

        if (page > safahat) {
            Toast.makeText(getContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
            loadingPB.setVisibility(View.GONE);
            return;
        }

        url = "https://vprofit.info/api/support/all?filter=&q&per_page=15&page=" + page;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.
                Method.GET, url, null, new Response.Listener<JSONObject>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                //ticketModels.clear();
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject userData = dataArray.getJSONObject(i);
                        JSONObject lastest = dataArray.getJSONObject(0);
                        JSONObject lastmessage = lastest.getJSONObject("latest");
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
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error != null) {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String message = data.getString("message");
                        if (message.equals("Unauthenticated.")) {
                            SharedPreferences settings = getActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE);
                            settings.edit().remove("token").commit();
                            Intent i = new Intent(getContext(), Splash.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                } catch (UnsupportedEncodingException errorr) {
                }
            }
        }) {
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

    private void MakeVolleyConnection2(int page, int safahat) {

        if (page > safahat) {
            Toast.makeText(getContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
            loadingPB.setVisibility(View.GONE);
            return;
        }

        url = "https://vprofit.info/api/support/all?filter=&q&per_page=15&page=" + page;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.
                Method.GET, url, null, new Response.Listener<JSONObject>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                ticketModels.clear();
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject userData = dataArray.getJSONObject(i);
                        JSONObject lastest = dataArray.getJSONObject(0);
                        JSONObject lastmessage = lastest.getJSONObject("latest");
                        TicketModel recyclerViewData = new TicketModel("", "", "", "", "", "", "");
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
                try {
                    if (error != null) {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String message = data.getString("message");
                        if (message.equals("Unauthenticated.")) {
                            SharedPreferences settings = getActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE);
                            settings.edit().remove("token").commit();
                            Intent i = new Intent(getContext(), Splash.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                } catch (UnsupportedEncodingException errorr) {
                }
            }
        }) {
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