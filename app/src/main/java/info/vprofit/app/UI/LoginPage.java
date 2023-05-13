package info.vprofit.app.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
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
    String MT="";
    RequestQueue requestQueue;
    TextView logtext;
    String mDeviceName;
    private ProgressBar loadingPB;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        license = findViewById(R.id.license_edit_text);
        login = findViewById(R.id.login);
        requestQueue = Volley.newRequestQueue(getApplication());
        loadingPB = findViewById(R.id.idLoadingPB);
        logtext = findViewById(R.id.logtext);

        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //String mDeviceName = DeviceName.getDeviceName();
         mDeviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;

     //   Toast.makeText(getApplication(), ""+getIMEIDeviceId(getApplication()), Toast.LENGTH_SHORT).show();


//        SharedPreferences shared = getApplication().getSharedPreferences("prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = shared.edit();
//        String fcm_token = shared.getString("fcm_token", "").trim();


   //     Toast.makeText(this, fcm_token, Toast.LENGTH_SHORT).show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //     String fcm_token= String.valueOf(FirebaseMessaging.getInstance().getToken());
                s_license = license.getText().toString();

            // String   MT= String.valueOf(FirebaseMessaging.getInstance().getToken());
              //
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    System.out.println("Failed to get token");
                                    return;
                                }
                               MT = task.getResult();



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

                    login.setEnabled(false);
                    loadingPB.setVisibility(View.VISIBLE);
                    logtext.setVisibility(View.GONE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, license_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        login.setEnabled(true);
                                        loadingPB.setVisibility(View.GONE);
                                        logtext.setVisibility(View.VISIBLE);
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
                            login.setEnabled(true);
                            loadingPB.setVisibility(View.GONE);
                            logtext.setVisibility(View.VISIBLE);
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


                            JSONArray jsonArray=new JSONArray();
                            JSONObject jsonObj=new JSONObject();
                            try {
                                jsonObj.put("device_name",mDeviceName);
                                jsonObj.put("fcm_token",MT);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObj);
                            params.put("license_code", s_license);
                            params.put("app_code",android_id);
                            params.put("app_info",jsonArray.toString());
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
                        });
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
    }}

