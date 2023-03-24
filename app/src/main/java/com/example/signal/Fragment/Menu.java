package com.example.signal.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signal.HistorySignal;
import com.example.signal.R;
import com.example.signal.UI.AboutUs;
import com.example.signal.UI.LoginPage;
import com.example.signal.UI.MainActivity;
import com.example.signal.UI.Profile;
import com.example.signal.UI.Splash;
import com.example.signal.UI.Subscribtion;
import com.example.signal.UI.Support;
import com.example.signal.databinding.FragmentMenuBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Menu extends Fragment {

    ConstraintLayout lightdark, user, sub, about, light, support,chart,log_out,historysignal;
    FrameLayout exit;
    String license_url = "https://vprofit.info/api/user/logout";

    RequestQueue requestQueue;
    String token;
    private FragmentMenuBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMenuBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        exit = root.findViewById(R.id.exit);
        lightdark = root.findViewById(R.id.lightdark);
        user = root.findViewById(R.id.user);
        sub = root.findViewById(R.id.sub);
        about = root.findViewById(R.id.about);
        light = root.findViewById(R.id.light);
        support = root.findViewById(R.id.support);
        chart = root.findViewById(R.id.chart);
        historysignal = root.findViewById(R.id.history);
        log_out = root.findViewById(R.id.logout);
        requestQueue = Volley.newRequestQueue(getContext());
        SharedPreferences sp = getActivity().getSharedPreferences("Prefs", MODE_PRIVATE);
        token = sp.getString("token", "");

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                light.setVisibility(View.VISIBLE);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                lightdark.setVisibility(View.VISIBLE);
                break;
        }


        log_out.setOnClickListener(new
                                           View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {



                                                   Sent();


                                               }
                                           });



        historysignal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), HistorySignal.class);
                getFragmentManager().beginTransaction().remove(Menu.this).commit();
                startActivity(i);
                getActivity().finish();
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MainActivity.class);
                getFragmentManager().beginTransaction().remove(Menu.this).commit();
                startActivity(i);
                getActivity().finish();
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Support.class);
                getFragmentManager().beginTransaction().remove(Menu.this).commit();
                startActivity(i);
                getActivity().finish();
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Profile.class);
                getFragmentManager().beginTransaction().remove(Menu.this).commit();
                startActivity(i);
                getActivity().finish();
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Subscribtion.class);
                getFragmentManager().beginTransaction().remove(Menu.this).commit();
                startActivity(i);
                getActivity().finish();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AboutUs.class);
                getFragmentManager().beginTransaction().remove(Menu.this).commit();
                startActivity(i);
                getActivity().finish();
            }
        });
        lightdark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //restart App
                //Called intent with the restartApp method to be active.
                lightdark.setVisibility(View.GONE);
                light.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(Menu.this).commit();
                restartApp();

            }
        });
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                //restart App
                //Called intent with the restartApp method to be active.
                light.setVisibility(View.GONE);
                lightdark.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().remove(Menu.this).commit();
                restartApp();


            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().beginTransaction().remove(Menu.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void restartApp() {
        //Go to the same class as Intent

        //With setFlags, the application is prevented from being a black screen.
        Intent i = new Intent(getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
    private void Sent() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, license_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        SharedPreferences settings = getActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE);
                        settings.edit().remove("token").commit();
                        Intent i = new Intent(getContext(), Splash.class);
                        startActivity(i);
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
                            SharedPreferences settings = getActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE);
                            settings.edit().remove("token").commit();
                            Intent i = new Intent(getContext(), Splash.class);
                            startActivity(i);

                        }else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (JSONException e) {

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

        requestQueue.add(stringRequest);




    }
}