package info.vprofit.app.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import info.vprofit.app.Adapter.ChatAdapter;
import info.vprofit.app.Fragment.Menu;
import info.vprofit.app.Fragment.Notification;
import info.vprofit.app.Helper.MyApplication;
import info.vprofit.app.Model.ChatItemModel;
import com.example.signal.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketView extends AppCompatActivity {

    public static String urlget = "https://vprofit.info/api/support/";
    public static String urlpost = "https://vprofit.info/api/support/";
    String url1get = "";
    String url1post = "";
    TextView subject, ticket_id;
    TextInputEditText message;
    String token, s_message;
    MaterialCardView sent;
    String s_ticket_id;
    RequestQueue requestQueue;
    ImageView menu, profile;
    ConstraintLayout back;

    List<ChatItemModel> itemClasses = new ArrayList<>();
    RecyclerView recyclerView;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_view);


        SharedPreferences sp = getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
        token = sp.getString("token", "");

        String s_id = getIntent().getExtras().getString("id");
        s_ticket_id = getIntent().getExtras().getString("ticket_id");
        String s_subject = getIntent().getExtras().getString("subject");

        url1get = urlget + s_id;
        url1post = urlpost + s_id + "/answer";

        subject = findViewById(R.id.subject);
        ticket_id = findViewById(R.id.ticketid);
        message = findViewById(R.id.message);
        sent = findViewById(R.id.sentmaterial);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        subject.setText(s_subject);
        ticket_id.setText("ticket id: " + s_ticket_id);
        recyclerView = findViewById(R.id.recyclerview);
        menu = findViewById(R.id.menu);
        profile = findViewById(R.id.profile);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s_message = message.getText().toString();

                if (s_message.equals("")) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_zarb, findViewById(R.id.toast_layout_root));


                    Toast toast = new Toast(getApplicationContext());
                    TextView text = layout.findViewById(R.id.text);
                    text.setText("fill all items!!!");
                    toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();


                } else {
                    sent();

                }


                //   Toast.makeText(TicketView.this, "ok", Toast.LENGTH_SHORT).show();
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        recyclerView.setLayoutManager(linearLayoutManager);
        MakeVolleyConnection();

    }

    private void MakeVolleyConnection() {
        itemClasses = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1get, null, new Response.Listener<JSONObject>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONArray dataArray = response.getJSONArray("messages");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject userData = dataArray.getJSONObject(i);
                        ChatItemModel recyclerViewData = new ChatItemModel(ChatItemModel.User_chat, R.mipmap.logo, "", "", "", "");
                        ChatItemModel recyclerViewData1 = new ChatItemModel(ChatItemModel.System_chat, R.mipmap.logo, "", "", "");

                        String support = userData.getString("is_support");

                        if (support.equals("0")) {
                            recyclerViewData.setDescription1(userData.getString("message"));
                           recyclerViewData.setTime1(userData.getString("humanDate"));
                            itemClasses.add(recyclerViewData);
                        } else {
                            recyclerViewData1.setDescription(userData.getString("message"));
                            recyclerViewData1.setTime(userData.getString("humanDate"));
                            itemClasses.add(recyclerViewData1);
                        }

                    }


                    ChatAdapter adapterClass = new ChatAdapter(itemClasses);
//                    rvAdapter = new DetailsSelectAdapter(detailsSelectItems, getActivity());
                    recyclerView.setAdapter(adapterClass);
                    adapterClass.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
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


        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void sent() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1post,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            MakeVolleyConnection();
                            message.setText("");

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("message").equals("The message has been successfully sent to the user panel")) {


                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast_tick,
                                        findViewById(R.id.toast_layout_root));

                                Toast toast = new Toast(getApplicationContext());
                                TextView text = layout.findViewById(R.id.text);
                                text.setText("The message has been successfully sent to the user panel");
                                toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();

                            } else {

                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast_tick,
                                        findViewById(R.id.toast_layout_root));

                                Toast toast = new Toast(getApplicationContext());
                                TextView text = layout.findViewById(R.id.text);
                                text.setText("Your request has been not sent successfully.");
                                toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();


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

                        }else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {

                } catch (UnsupportedEncodingException errorr) {
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("message", s_message);
                params.put("ticket_id", s_ticket_id);
                params.put("file", "");
                params.put("rel", "UserLicense");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                // headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + token);

                return headerMap;
            }
        };

        requestQueue.add(stringRequest);
    }
//Todo

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce = false;
//            }
//        }, 2000);
//    }
}