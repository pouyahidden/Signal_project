package info.vprofit.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import info.vprofit.app.Adapter.TicketAdapter;
import info.vprofit.app.Helper.MyApplication;
import info.vprofit.app.Model.TicketModel;
import com.example.signal.R;
import info.vprofit.app.UI.Splash;

import com.example.signal.databinding.UnreadBinding;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Unread extends Fragment {
     String url = "";
    private  RecyclerView recyclerView,recyclerViewsearch;
    private List<TicketModel> ticketModels;
    String token;
    EditText search;
    MaterialCardView newmessage;
    private ProgressBar loadingPB;
    RequestQueue requestQueue;
    private TicketAdapter ticketAdapter;
    String reload;
    private UnreadBinding binding;
    private NestedScrollView nestedSV;
    private int page = 1;
    String searchs="";
    private int safahat=300;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            reload = intent.getStringExtra("message");


            if (reload.equals("1")){
                MakeVolleyConnection(page,safahat);
            }

        }
    };



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = UnreadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter("custom-action-local-broadcast"));
        SharedPreferences sp =getActivity().getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
        token  = sp.getString("token","");

        newmessage = root.findViewById(R.id.newmessage);
        recyclerView=root.findViewById(R.id.recyclerview);
        recyclerViewsearch=root.findViewById(R.id.recyclerviewsearch);
        search = root.findViewById(R.id.search);
        loadingPB =root.findViewById(R.id.idLoadingPB);
        requestQueue = Volley.newRequestQueue(getActivity());
        ticketModels = new ArrayList<>();
        nestedSV = root.findViewById(R.id.idNestedSV);

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


//searchs = "pouya1";
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                     searchs = search.getText().toString();

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

            MakeVolleyConnection(page, safahat);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void MakeVolleyConnection(int page, int safahat) {
        if (page > safahat) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            Toast.makeText(getContext(), "That's all the data..", Toast.LENGTH_SHORT).show();

            // hiding our progress bar.
            loadingPB.setVisibility(View.GONE);
            return;
        }

        url= "https://vprofit.info/api/support/all?filter=unread&q="+searchs+"&per_page=10&page="+page;

        StringRequest jsonObjectRequest = new StringRequest(Request.
                Method.GET, url,  new Response.Listener<String>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray dataArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject userData = dataArray.getJSONObject(i);

                        JSONObject lastest= dataArray.getJSONObject(0);
                        JSONObject lastmessage= lastest.getJSONObject("latest");
                        TicketModel recyclerViewData = new TicketModel("","","","","","","");
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
                } catch (JSONException e) {

                } catch (UnsupportedEncodingException errorr) {
                }



            }
        }){
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
    public void refresh() {

        Log.i("Refresh", "YES");
    }
}

