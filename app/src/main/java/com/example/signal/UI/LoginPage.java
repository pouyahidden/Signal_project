package com.example.signal.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signal.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    TextInputEditText license;
    MaterialCardView login;
    String license_url = "https://vprofit.info/api/auth/active-app";
    String s_license;
    RequestQueue requestQueue;
    String mDeviceName;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        license = findViewById(R.id.license_edit_text);
        login = findViewById(R.id.login);
        requestQueue = Volley.newRequestQueue(getApplication());

        //String mDeviceName = DeviceName.getDeviceName();
         mDeviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        //Toast.makeText(this, mDeviceName, Toast.LENGTH_SHORT).show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_license = license.getText().toString();

                if (s_license.equals("")) {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_zarb,
                            findViewById(R.id.toast_layout_root));


                    Toast toast = new Toast(getApplicationContext());
                    TextView text = layout.findViewById(R.id.text);
                    text.setText("Hello! This is a custom toast44!");
                    toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();


                } else {
                    Sent();
                }
            }


        });


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

    private void Sent() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, license_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                                JSONObject jsonObject1 = new JSONObject(response);
                                String token = jsonObject1.optString("access_token");

                                SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putString("token", token);
                                editor.apply();


                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);




                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                int statusCode = error.networkResponse.statusCode;
//                if (statusCode == 422) {
//                    String status = error.getMessage();
//                    Toast.makeText(LoginPage.this, status, Toast.LENGTH_SHORT).show();
//                    //Toast.makeText(LoginPage.this, "The selected license code is invalid.", Toast.LENGTH_SHORT).show();
//                }else if (statusCode==403){
//                    Toast.makeText(LoginPage.this, "this license is incorrect.", Toast.LENGTH_SHORT).show();
//                }


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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("license_code", s_license);
                params.put("app_info",mDeviceName);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                 headerMap.put("Accept", "application/json");
               // headerMap.put("Authorization", "Bearer " + token);

                return headerMap;
            }

        };

        requestQueue.add(stringRequest);




    }

}