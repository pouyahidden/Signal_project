package info.vprofit.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import info.vprofit.app.Adapter.NotifAdapter;
import info.vprofit.app.Helper.MyApplication;
import info.vprofit.app.Model.NotifItem;
import com.example.signal.R;
import com.example.signal.databinding.FragmentNotificationBinding;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification extends Fragment {

    private FragmentNotificationBinding binding;
    TextView txt2;
    FrameLayout exit;
    String token;

    private static final String JSON_URL = "https://vprofit.info/api/signal/notifications?limit=10/";
    private List<NotifItem> doreItemList;
    private RecyclerView notifrecycler;
    private NotifAdapter aaAdapter;
    private ProgressBar loadingPB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txt2 = root.findViewById(R.id.txt2);
        exit = root.findViewById(R.id.exit);
        notifrecycler = root.findViewById(R.id.noifrecycler);
        SharedPreferences sp = getActivity().getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
        token = sp.getString("token", "");
        doreItemList = new ArrayList<>();
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        notifrecycler.setLayoutManager(gridLayoutManager);
        loadingPB = root.findViewById(R.id.idLoadingPB);

        PusherOptions options = new PusherOptions();
        options.setCluster("eu");
        Pusher pusher = new Pusher("67b19b83a1d178a467ef",options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {

            }

            @Override
            public void onError(String message, String code, Exception e) {

            }
        }, ConnectionState.ALL);
     //   Channel channel = (Channel) pusher.subscribe("my-channel");




        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction().remove(Notification.this).commit();
            }
        });
        MakeVolleyConnection();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void MakeVolleyConnection() {
        doreItemList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONArray response) {
                loadingPB.setVisibility(View.GONE);
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject userData = response.getJSONObject(i);
                        NotifItem recyclerViewData = new NotifItem();

                        recyclerViewData.setId(userData.getString("id"));
                        recyclerViewData.setTitle(userData.getString("title"));
                       recyclerViewData.setDescription(userData.getString("description"));
                        recyclerViewData.setCreat(userData.getString("human_date"));

                        doreItemList.add(recyclerViewData);}


                    aaAdapter = new NotifAdapter(doreItemList,getActivity());
                    notifrecycler.setAdapter(aaAdapter);
                    aaAdapter.notifyDataSetChanged();


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

                    loadingPB.setVisibility(View.GONE);
                    txt2.setVisibility(View.VISIBLE);
                    txt2.setText(message);

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
        }};
        MyApplication.getInstance().addToRequestQueue(jsonArrayRequest);
    }

}