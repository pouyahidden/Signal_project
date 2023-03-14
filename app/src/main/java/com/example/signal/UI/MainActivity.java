package com.example.signal.UI;

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
import com.example.signal.Adapter.OuterAdapter;
import com.example.signal.Fragment.BottomSheetDialog;
import com.example.signal.Fragment.Menu;
import com.example.signal.Fragment.Notification;
import com.example.signal.Model.Inner;
import com.example.signal.Model.Outer;
import com.example.signal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public String message = "", token;
    // private static String url = "";
    ImageView menu, more, profile;
    @BindView(R.id.parentrecycler)
    RecyclerView rvMain;
    List<Outer> outers = new ArrayList<>();
    Inner single = new Inner("name", "value");
    List<String> values = new ArrayList<String>();
    List<String> ids = new ArrayList<String>();
    TextView txt1;
    String url1 = "https://vprofit.info/api/signal/all?filter=";
    String url2 = "";
    String url3 = "&page=1&per_page=10&status=1";
    String url = "https://vprofit.info/api/signal/all?page=1&status=1";
    boolean doubleBackToExitPressedOnce = false;
     protected  int page =1, limit = 5;
    private List<Inner> inners = new ArrayList<>();
    Outer dm = new Outer("trade_type"
            , "currency_mark",
            "human_date",
            "title",
            "description",
            "mark",
            "profit", inners);
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            message = intent.getStringExtra("message");

            url = url1 + message + url3;

            setupRv();
            MakeVolleyConnection(page, limit);


        }
    };
    private NestedScrollView nestedSV;
    private ProgressBar loadingPB;
    private OuterAdapter outerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = findViewById(R.id.menu);
        more = findViewById(R.id.more);
        profile = findViewById(R.id.profile);
        nestedSV = findViewById(R.id.idNestedSV);
        loadingPB = findViewById(R.id.idLoadingPB);
        txt1 = findViewById(R.id.txt2);

        ///////////////////////////////////////////////////////////

        ButterKnife.bind(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("custom-action-local-broadcast"));

//
        // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        SharedPreferences sp = getSharedPreferences("Prefs", Activity.MODE_PRIVATE);

        token = sp.getString("token", "");



        setupRv();
        MakeVolleyConnection(page, limit);

        // url = JSON_URL + "?category_id=" + value1;


        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // in this method we are incrementing page number,
                    // making progress bar visible and calling get data method.
                    page++;
                    loadingPB.setVisibility(View.VISIBLE);
                   // Toast.makeText(MainActivity.this, ""+page, Toast.LENGTH_SHORT).show();
                    setupRv();
                    MakeVolleyConnection(page, limit);
                }
            }
        });


//        setupRv();
//        MakeVolleyConnection();


        ////////////////////////////////////////////////////////
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
        rvMain.setAdapter(outerAdapter);

    }

    private void MakeVolleyConnection(int page, int limit) {

        if (page > limit) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();

            // hiding our progress bar.
            loadingPB.setVisibility(View.GONE);
            return;
        }
        String url = "https://vprofit.info/api/signal/all?page="+page+"&status=1";

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);

                more.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray arafilter = jsonObject.getJSONArray("array_filters");
                    JSONObject data = jsonObject.getJSONObject("signals");

                    int total = data.getInt("total");
                    total = total / 10;

                    final  int safahat = (int) Math.ceil(total);

                    Toast.makeText(MainActivity.this, limit + "", Toast.LENGTH_SHORT).show();

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


//                        for (int j = 0; j < item.length(); j++) {
//                            JSONObject child = item.getJSONObject(j);
//
//
////                        String name = child.getString("name");
////                        String vall = child.getString("value");
//                            String name = single.setName(child.getString("name"));
//                            String vall = single.setVall(child.getString("value"));
//                            inners.add(new Inner(name, vall));
//
//
//                        }


                        outers.clear();
                        // inners.clear();


                        outerAdapter.addOuter(new Outer(trade_type, currency_mark, human_date, title, mark, des, profit, inners));

                    }

//                    for (int j = 0; j < item.length(); j++) {
//
//                        JSONObject child = item.getJSONObject(j);
//                        Toast.makeText(MainActivity.this, ""+item.length(), Toast.LENGTH_SHORT).show();
//
//
//
//
//                        txt1.setText(vall);
//
//
//                        //   Toast.makeText(getApplicationContext(), "   "+vall, Toast.LENGTH_SHORT).show();
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