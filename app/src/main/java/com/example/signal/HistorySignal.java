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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    TextView text1;
    @BindView(R.id.parentrecycler)
    RecyclerView rvMain;
    List<Inner> inners = new ArrayList<>();
    Inner single = new Inner("name", "value");
    List<Outer> outers = new ArrayList<>();
    Outer dm = new Outer("trade_type", "currency_mark", "human_date", "title", "description", "mark", "profit", inners);
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            message = intent.getStringExtra("message");

            url = url1 + message + url3;

            setupRv();
            MakeVolleyConnection();


        }
    };
    List<String> values = new ArrayList<String>();
    List<String> ids = new ArrayList<String>();
    TextView txt1;
    String url1 = "https://vprofit.info/api/signal/all?filter=";
    String url2 = "";
    String url3 = "&page=1&per_page=10&status=0";
    String url = "https://vprofit.info/api/signal/all?page=1&per_page=10&status=0";
    boolean doubleBackToExitPressedOnce = false;
    private ProgressBar loadingPB;
    private OuterAdapter outerAdapter;
    private InnerAdapter innerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_signal);

        menu = findViewById(R.id.menu);
        more = findViewById(R.id.more);
        profile = findViewById(R.id.profile);
        loadingPB = findViewById(R.id.idLoadingPB);
        text1 = findViewById(R.id.txt2);


        ButterKnife.bind(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("custom-action-local-broadcast"));

//
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        SharedPreferences sp = getSharedPreferences("Prefs", Activity.MODE_PRIVATE);

        token = sp.getString("token", "");
        //  Toast.makeText(getApplicationContext(), value1, Toast.LENGTH_SHORT).show();


        setupRv();
        MakeVolleyConnection();

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
        innerAdapter = new InnerAdapter();
        innerAdapter.notifyDataSetChanged();
        rvMain.setAdapter(outerAdapter);
    }

    private void MakeVolleyConnection() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);

                more.setVisibility(View.VISIBLE);
                try {


                    JSONObject jsonObject = new JSONObject(response);
//todo filter1
                   // JSONArray arafilter = jsonObject.getJSONArray("array_filters");

 //////////////////////////////////////////////////////////////////////////////////////////////
                    JSONObject data = jsonObject.getJSONObject("signals");
                    JSONArray region = data.getJSONArray("data");


//                    for (int L = 0; L <region.length(); L++) {
//                        JSONObject pool = region.getJSONObject(L);
//                        JSONArray item = pool.getJSONArray("array_details");
//               //         Toast.makeText(getApplicationContext(), ""+L, Toast.LENGTH_SHORT).show();
//
//                       if (L==0){
//                            inners.clear();
//                                   for (int j = 0; j < item.length(); j++) {
//
//                                   JSONObject ar = item.getJSONObject(j);
//
//
//                                 Toast.makeText(getApplicationContext(), ""+ar, Toast.LENGTH_SHORT).show();
//
//                                    String name = single.setName(ar.getString("name"));
//                                   String vall = single.setVall(ar.getString("value"));
//
//                                   inners.add(new Inner(name, vall));
//
//
//                                 }
//
//
//
//                        }
//
//                    }
                 //   JSONArray array = parent.getJSONArray("array_details");
                    //    JSONObject itemdetails = pool.getJSONObject("currency");
                    //  JSONObject filter = pool.getJSONObject("array_filters");


   //todo filter2

//                    ids.clear();
//                    values.clear();
//                    for (int i = 0; i < arafilter.length(); i++) {
//                        JSONObject obj = arafilter.getJSONObject(i);
//                        String id = obj.getString("id");
//                        String name = obj.getString("title");
//
//
//                        ids.add(id);
//                        values.add(name);
//
//                        Intent intent = getIntent();
//                        intent.putStringArrayListExtra("filterlist", new ArrayList<>(values));
//                        intent.putStringArrayListExtra("filterid", new ArrayList<>(ids));
//
//                    }

////////////////////////////////////////////////////////////////////////////////////////////////////
                    //String filter_array = arafilter.getString("title");
                    //  String filter_array=   filterobj.getString("");
                    outers.clear();
                    inners.clear();
                    for (int i = 0; i < region.length(); i++) {
                        JSONObject parent = region.getJSONObject(i);
                        JSONObject details= parent.getJSONObject("details");


                        Iterator<String> keys= details.keys();
                        while (keys.hasNext())
                        {
                            String keyValue = (String)keys.next();
                            String valueString = details.getString(keyValue);
                            Toast.makeText(HistorySignal.this, ""+keyValue, Toast.LENGTH_SHORT).show();

                            text1.append(valueString);

                        }

                        String s=    details.getString("entry_price");



                        String trade_type = dm.setTrade_type(parent.getString("trade_type"));
                        String currency_mark = dm.setCurrency_mark(parent.getString("currency_mark"));
                        String human_date = dm.setHuman_date(parent.getString("human_date"));
                        String mark = dm.setMark(parent.getString("status_text"));
                        String des = dm.setDescription(parent.getString("description"));
                        String profit = dm.setProfit(parent.getString("profit"));
                        String title = dm.setTitle(parent.getString("title"));

                        outers.clear();










                     //   outerAdapter.addOuter(new Outer(trade_type, currency_mark, human_date, title, mark, des, profit, inners));

                    }










//                    for (int p = 0; p < pool.length();p++) {
//                        pool=pool.getJSONObject(String.valueOf(p));
//
//
//
//
//
//
//
//                        // Toast.makeText(getApplicationContext(), ""+name+"   "+vall, Toast.LENGTH_SHORT).show();
//
//
//
//                    }
                    //Currency itemsdetail = new Currency("","");
                    // for (int k = 0; k<itemdetails.length();k++){
                    //      JSONObject detailobject = itemdetails.getJSONObject(k);
                    //  String description = dm.set(detailobject.getString("status_text"));

//                        String title = detailobject.getString("title");
//                        String mark =  detailobject.getString("mark");

                    //      Toast.makeText(MainActivity.this, mark, Toast.LENGTH_SHORT).show();
//                        String title =   single.setName(child.getString("name"));
//                        String vall =   single.setVall(child.getString("value"));

                    //     }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_LONG).show();
                Log.i(String.valueOf(error), "");
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