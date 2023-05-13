package info.vprofit.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.content.CursorLoader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signal.R;
import info.vprofit.app.UI.Splash;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewMessageSheet extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {
    static final int PICK_IMAGE_REQUEST = 1;
    String url = "https://vprofit.info/api/support/create";
    TextInputEditText titlet, descriptiont;
    Spinner spinner1, spinner;
    MaterialCardView sentmessage;
    TextView image_select,sendtext;
    String token;
    ImageView sendicon;
    String s_title, s_description, s_radio, s_department, s_periority, s_file = "slm.png";
    List<String> department = new ArrayList<String>();
    List<String> priority = new ArrayList<String>();
    RequestQueue requestQueue;
    ConstraintLayout uploadfile;
    private ProgressBar loadingPB;
    String filePath="";

    @Override
    public int getTheme() {
        return R.style.NoBackgroundDialogTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable

            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_message_bottom_sheet, container, false);
        v.setBackgroundResource(R.drawable.sheet_new_message);

        SharedPreferences sp = getActivity().getSharedPreferences("Prefs", MODE_PRIVATE);
        token = sp.getString("token", "");

        titlet = v.findViewById(R.id.titlet);
        descriptiont = v.findViewById(R.id.descriptiont);

        spinner1 = v.findViewById(R.id.spinner1);
        spinner = v.findViewById(R.id.spinner);
        sentmessage = v.findViewById(R.id.sentmessage);
        uploadfile = v.findViewById(R.id.upload);
        image_select= v.findViewById(R.id.image_select);
        requestQueue = Volley.newRequestQueue(getActivity());
        loadingPB = v.findViewById(R.id.idLoadingPB);
        sendtext = v.findViewById(R.id.sendtext);
        sendicon = v.findViewById(R.id.sendicon);
        spinner.setOnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(this);
        department = new ArrayList<String>();
        department.add("management");
        department.add("sale");
        department.add("accounting");



        priority = new ArrayList<String>();
        priority.add("low");
        priority.add("medium");
        priority.add("much");
        //spinner.setPrompt("Department");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, department);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, priority);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner1.setAdapter(dataAdapter1);


        uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrowse();
            }
        });

        sentmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                s_title = titlet.getText().toString();
                s_description = descriptiont.getText().toString();
                if (s_title.equals("") && s_description.equals("")) {


                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_zarb,
                            getActivity().findViewById(R.id.toast_layout_root));

//                ImageView image = (ImageView) layout.findViewById(R.id.image);
//                image.setImageResource(R.drawable.android);
//                TextView text = (TextView) layout.findViewById(R.id.text);
//                text.setText("Hello! This is a custom toast!");

                    Toast toast = new Toast(getActivity());
                    TextView text = layout.findViewById(R.id.text);
                    text.setText("fill all items!!!");
                    toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();


                } else {
                    sentmessage.setEnabled(false);
                    loadingPB.setVisibility(View.VISIBLE);
                    sendtext.setVisibility(View.GONE);
                    sendicon.setVisibility(View.GONE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    sentmessage.setEnabled(true);
                                    loadingPB.setVisibility(View.GONE);
                                    sendtext.setVisibility(View.VISIBLE);
                                    sendicon.setVisibility(View.VISIBLE);
                                    Intent("message", "1");
                                 //   Intent("title",s_title);

                                    dismiss();
                                    try {

                                        JSONObject jsonObject = new JSONObject(response);


                                        if (jsonObject.optString("message").equals("Your request has been sent successfully, our experts will announce your answer in the same ticket soon.")) {


                                            LayoutInflater inflater = getLayoutInflater();
                                            View layout = inflater.inflate(R.layout.toast_tick,
                                                    getActivity().findViewById(R.id.toast_layout_root));

                                            Toast toast = new Toast(getActivity());
                                            TextView text = layout.findViewById(R.id.text);
                                            text.setText("Your request has been sent successfully, our experts will announce your answer in the same ticket soon.");
                                            toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                                            toast.setDuration(Toast.LENGTH_SHORT);
                                            toast.setView(layout);
                                            toast.show();

                                        } else {

                                            LayoutInflater inflater = getLayoutInflater();
                                            View layout = inflater.inflate(R.layout.toast_zarb,
                                                    getActivity().findViewById(R.id.toast_layout_root));

                                            Toast toast = new Toast(getActivity());
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
                            sentmessage.setEnabled(true);
                            loadingPB.setVisibility(View.GONE);
                            sendtext.setVisibility(View.VISIBLE);
                            sendicon.setVisibility(View.VISIBLE);

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
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("subject", s_title);
                            params.put("department", s_department);
                            params.put("message", s_description);
                            params.put("priority", s_periority);
                            params.put("file", filePath);
                            params.put("rel", "User");
                            return params;
                        }

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
        });
        return v;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        switch (adapterView.getId()) {
            case R.id.spinner1:

                s_periority = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                adapterView.getSelectedItem().toString();
                break;
            case R.id.spinner:

                s_department = adapterView.getItemAtPosition(i).toString();

                // Showing selected spinner item
                //Toast.makeText(adapterView.getContext(), "Selected: " + item1, Toast.LENGTH_LONG).show();
                break;
            default:

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void Intent(String message, String value) {


        Intent intent = new Intent("custom-action-local-broadcast");
        intent.putExtra(message, value);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

    }
    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST) {
                Uri picUri = data.getData();

                filePath = getPath(picUri);

                Log.d("picUri", picUri.toString());
                Log.d("filePath", filePath);
                image_select.setText("Image Selected");


            }
            }

        }

    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}