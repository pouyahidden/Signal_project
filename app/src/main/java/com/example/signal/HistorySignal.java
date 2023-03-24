package com.example.signal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signal.Adapter.InnerAdapter;
import com.example.signal.Adapter.OuterAdapter;
import com.example.signal.Fragment.BottomSheetDialog;
import com.example.signal.Fragment.Menu;
import com.example.signal.Fragment.Notification;
import com.example.signal.Model.Inner;
import com.example.signal.Model.Outer;
import com.example.signal.UI.MainActivity;
import com.example.signal.UI.Splash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistorySignal extends AppCompatActivity {
    public String message = "", token;
    ImageView menu, more, profile;
    @BindView(R.id.parentrecycler)
    RecyclerView rvMain;
    protected  int page =1, limit = 300;
    List<Inner> inners = new ArrayList<>();
    Inner single = new Inner("name", "value");
    List<Outer> outers = new ArrayList<>();
    List<String> values = new ArrayList<String>();
    List<String> ids = new ArrayList<String>();
    String url1 = "https://vprofit.info/api/signal/all?filter=";
    String url3 = "&per_page=10&page=1";
    String url4 = "&per_page=10&page=";
    public static String url = "https://vprofit.info/api/signal/all?filter=&page=";
    String  urlsignal="";
    String  urlfilter="";
    boolean doubleBackToExitPressedOnce = false;
    private ProgressBar loadingPB;
    private OuterAdapter outerAdapter;
    int shart=0;
    private NestedScrollView nestedSV;
    Outer dm = new Outer("trade_type", "currency_mark", "human_date", "title", "description", "mark", "profit", inners);

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            message = intent.getStringExtra("message");
            if (message.equals("")){
                shart=0;
                setupRv();
                MakeVolleyConnection(page, limit);
            }else {
                shart=1;
                setupRv();
                MakeVolleyConnectionfilter(page, limit);
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_signal);

        menu = findViewById(R.id.menu);
        more = findViewById(R.id.more);
        profile = findViewById(R.id.profile);
        loadingPB = findViewById(R.id.idLoadingPB);
        nestedSV = findViewById(R.id.idNestedSV);

        ButterKnife.bind(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("custom-action-local-broadcast"));
        SharedPreferences sp = getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
        token = sp.getString("token", "");
        setupRv();
        if (shart==1){

            MakeVolleyConnectionfilter(page, limit);
        }else {

            MakeVolleyConnection(page, limit);
        }

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                    if (shart==1){

                        MakeVolleyConnectionfilter(page, limit);
                    }else {
                        MakeVolleyConnection(page, limit);
                    }
                }
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragMgnr = getSupportFragmentManager();
                FragmentTransaction fragtrans;
                fragtrans = fragMgnr.beginTransaction();
                fragtrans.setCustomAnimations(R.animator.enter_from_left
                        , R.animator.enter_from_right);
                fragtrans.replace(R.id.nav_host_fragment_activity_main23, new Menu());
                fragtrans.addToBackStack(SyncStateContract.Constants.class.getName());
                fragtrans.commit();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
                shart=0;
                page=1;
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragMgnr = getSupportFragmentManager();
                FragmentTransaction fragtrans;
                fragtrans = fragMgnr.beginTransaction();
                fragtrans.setCustomAnimations(R.animator.enter_from_right1,
                        R.animator.enter_from_left1);
                fragtrans.replace(R.id.nav_host_fragment_activity_main24, new Notification());
                fragtrans.addToBackStack(SyncStateContract.Constants.class.getName());
                fragtrans.commit();
            }
        });

    }

    private void setupRv() {
        rvMain.setHasFixedSize(true);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        outerAdapter = new OuterAdapter();
        rvMain.setAdapter(outerAdapter);

    }

    private void MakeVolleyConnection(int page, int limit) {
        if (page > limit) {

            Toast.makeText(this, "That's all the signals..", Toast.LENGTH_SHORT).show();
            loadingPB.setVisibility(View.GONE);
            return;
        }
        urlsignal = url + page + "&status=0";
        RequestQueue queue = Volley.newRequestQueue(HistorySignal.this);
        StringRequest request = new StringRequest(Request.Method.GET, urlsignal, new Response.Listener<String>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);

                more.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray arafilter = jsonObject.getJSONArray("array_filters");
                    JSONObject data = jsonObject.getJSONObject("signals");
                    JSONArray region = data.getJSONArray("data");

                    ids.clear();
                    values.clear();
                    for (int i = 0; i < arafilter.length(); i++) {
                        JSONObject obj = arafilter.getJSONObject(i);
                        String id = obj.getString("id");
                        String name = obj.getString("title");

                        ids.add(id);
                        values.add(name);

                        Intent intent = getIntent();
                        intent.putStringArrayListExtra("filterlist", new ArrayList<>(values));
                        intent.putStringArrayListExtra("filterid", new ArrayList<>(ids));

                    }

                    outers.clear();

                    for (int i = 0; i < region.length(); i++) {
                        JSONObject parent = region.getJSONObject(i);

                        String trade_type = dm.setTrade_type(parent.getString("trade_type"));
                        String currency_mark = dm.setCurrency_mark(parent.getString("currency_mark"));
                        String human_date = dm.setHuman_date(parent.getString("human_date"));
                        String mark = dm.setMark(parent.getString("status_text"));
                        String des = dm.setDescription(parent.getString("description"));
                        String profit = dm.setProfit(parent.getString("profit"));
                        String title = dm.setTitle(parent.getString("title"));
                        inners.clear();
                        JSONArray item = parent.getJSONArray("array_details");

                        List<Inner> innersitems = new ArrayList<>();
                        for (int j = 0; j < item.length(); j++) {
                            JSONObject child = item.getJSONObject(j);
                            String name = single.setName(child.getString("name"));
                            String vall = single.setVall(child.getString("value"));
                            innersitems.add(new Inner(name, vall));

                        }
                        outers.clear();
                        outerAdapter.addOuter(new Outer(trade_type, currency_mark, human_date, title, mark, des, profit, innersitems));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    String message = data.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

        queue.add(request);
    }


    private void MakeVolleyConnectionfilter(int page, int limit) {
        if (page > limit) {

            Toast.makeText(this, "That's all the signals..", Toast.LENGTH_SHORT).show();
            loadingPB.setVisibility(View.GONE);
            return;
        }

        urlfilter = url1 + message + url4+page+"&status=0" ;

        RequestQueue queue = Volley.newRequestQueue(HistorySignal.this);
        StringRequest request = new StringRequest(Request.Method.GET, urlfilter, new Response.Listener<String>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);

                more.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray arafilter = jsonObject.getJSONArray("array_filters");
                    JSONObject data = jsonObject.getJSONObject("signals");
                    JSONArray region = data.getJSONArray("data");

                    ids.clear();
                    values.clear();
                    for (int i = 0; i < arafilter.length(); i++) {
                        JSONObject obj = arafilter.getJSONObject(i);
                        String id = obj.getString("id");
                        String name = obj.getString("title");

                        ids.add(id);
                        values.add(name);

                        Intent intent = getIntent();
                        intent.putStringArrayListExtra("filterlist", new ArrayList<>(values));
                        intent.putStringArrayListExtra("filterid", new ArrayList<>(ids));

                    }

                    outers.clear();

                    for (int i = 0; i < region.length(); i++) {
                        JSONObject parent = region.getJSONObject(i);

                        String trade_type = dm.setTrade_type(parent.getString("trade_type"));
                        String currency_mark = dm.setCurrency_mark(parent.getString("currency_mark"));
                        String human_date = dm.setHuman_date(parent.getString("human_date"));
                        String mark = dm.setMark(parent.getString("status_text"));
                        String des = dm.setDescription(parent.getString("description"));
                        String profit = dm.setProfit(parent.getString("profit"));
                        String title = dm.setTitle(parent.getString("title"));
                        inners.clear();
                        JSONArray item = parent.getJSONArray("array_details");

                        List<Inner> innersitems = new ArrayList<>();
                        for (int j = 0; j < item.length(); j++) {
                            JSONObject child = item.getJSONObject(j);
                            String name = single.setName(child.getString("name"));
                            String vall = single.setVall(child.getString("value"));
                            innersitems.add(new Inner(name, vall));

                        }
                        outers.clear();
                        outerAdapter.addOuter(new Outer(trade_type, currency_mark, human_date, title, mark, des, profit, innersitems));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if(error != null){
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String message = data.getString("message");
                        if (message.equals("Unauthenticated.")){
                            SharedPreferences settings = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
                            settings.edit().remove("token").commit();
                            Intent i = new Intent(getApplicationContext(), Splash.class);
                            startActivity(i);

                        }else{
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

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
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + token);

                return headerMap;
            }
        };

        queue.add(request);
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}