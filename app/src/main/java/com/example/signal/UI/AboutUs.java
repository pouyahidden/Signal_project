package com.example.signal.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.signal.Fragment.Menu;
import com.example.signal.Fragment.Notification;
import com.example.signal.R;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class AboutUs extends AppCompatActivity {

    ImageView menu, profile;
    MaterialCardView call;
    ConstraintLayout back;
    JustifyTextView text4;
    TextView text1;
    String url = "https://vprofit.info/api/tools/about";
    RequestQueue queue;
    boolean doubleBackToExitPressedOnce = false;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        menu = findViewById(R.id.menu);
        back = findViewById(R.id.back);
        text4 = findViewById(R.id.text4);
        text1 = findViewById(R.id.text1);
        profile = findViewById(R.id.profile);
        loadingPB = findViewById(R.id.idLoadingPB);
        call = findViewById(R.id.call);
        queue = Volley.newRequestQueue(getApplicationContext());
        ////////////////////////////////////////////////////////

        getmethod();

        call.setOnClickListener(view -> {
            String phone = "+989333277628";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void getmethod() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                loadingPB.setVisibility(View.GONE);

                try {
                    // now we get our response from API in json object format.
                    // in below line we are extracting a string with its key
                    // value from our json object.
                    // similarly we are extracting all the strings from our json object.
                    String value = response.getString("value");
                    String key = response.getString("key");


                    // after extracting all the data we are
                    // setting that data to all our views.

                    text1.setText(key);
                    text4.setText(value);

                } catch (JSONException e) {
                    // if we do not extract data from json object properly.
                    // below line of code is use to handle json exception
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // this is the error listener method which
            // we will call if we get any error from API.
            @Override
            public void onErrorResponse(VolleyError error) {
                // below line is use to display a toast message along with our error.
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
                }
                catch (JSONException e) {

                } catch (UnsupportedEncodingException errorr) {

                }            }
        });
        // at last we are adding our json
        // object request to our request
        // queue to fetch all the json data.
        queue.add(jsonObjectRequest);
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
