package com.example.signal.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signal.Adapter.BottomSheetRecycler;
import com.example.signal.ItemClickListener;
import com.example.signal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    //public int i = 6;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3, rb4, rb5, rb6;
    RecyclerView recyclerView;
    ItemClickListener itemClickListener;
    BottomSheetRecycler adapter;

    @Override
    public int getTheme() {
        return R.style.NoBackgroundDialogTheme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable

            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        v.setBackgroundResource(R.drawable.sheet_shape);
        recyclerView = v.findViewById(R.id.bottomrecycler);

        ArrayList<String> ids = getActivity().getIntent().getStringArrayListExtra("filterid");


      //  Toast.makeText(getActivity(), ""+s, Toast.LENGTH_SHORT).show();


        //Toast.makeText(getActivity(), ss, Toast.LENGTH_SHORT).show();
        ArrayList<String> votedByList = getActivity().getIntent().getStringArrayListExtra("filterlist");
//        for (int i = 0; i < 20; i++) {
//            // add values in array list
//            votedByList.add("RB " + i);

        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(String s,int i) {
                // Notify adapter
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                String ss =(String.valueOf(ids.get(i)));
                Intent("message",ss);
               // Toast.makeText(getActivity(), "Selected : " + ss, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BottomSheetRecycler(votedByList, itemClickListener);
        recyclerView.setAdapter(adapter);
        //}

        //Toast.makeText(getContext(), votedByList + "ine", Toast.LENGTH_SHORT).show();

//
//        rb1 = v.findViewById(R.id.rb1);
//        rb2 = v.findViewById(R.id.rb2);
//        rb3 = v.findViewById(R.id.rb3);
//        rb4 = v.findViewById(R.id.rb4);
//        rb5 = v.findViewById(R.id.rb5);
//        rb6 = v.findViewById(R.id.rb6);
//        rg = v.findViewById(R.id.rg);
//
//        SharedPreferences sp = getContext().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
//        int value = sp.getInt("i", 0);
//        if (value == 1) {
//            rb1.setChecked(true);
//        } else if (value == 2) {
//            rb2.setChecked(true);
//        } else if (value == 3) {
//            rb3.setChecked(true);
//        } else if (value == 4) {
//            rb4.setChecked(true);
//        } else if (value == 5) {
//            rb5.setChecked(true);
//        } else if (value == 6) {
//            rb6.setChecked(true);
//        }
//        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                switch (checkedId) {
//                    case R.id.rb1:
//                        i = 1;
//                        sharedprefs();
//                        Intent("message", "Cryptocurrency");
//                        dismiss();
//                        break;
//                    case R.id.rb2:
//                        i = 2;
//                        sharedprefs();
//                        Intent("message", "Futures");
//                        dismiss();
//                        break;
//                    case R.id.rb3:
//                        i = 3;
//                        sharedprefs();
//                        Intent("message", "Stocks");
//                        dismiss();
//                        break;
//                    case R.id.rb4:
//                        i = 4;
//                        sharedprefs();
//                        Intent("message", "Buy");
//                        dismiss();
//                        break;
//                    case R.id.rb5:
//                        i = 5;
//                        sharedprefs();
//                        Intent("message", "Sell");
//                        dismiss();
//                        break;
//                    case R.id.rb6:
//                        i = 6;
//                        sharedprefs();
//                        Intent("message", "all");
//                        dismiss();
//                        break;
//                }
//            }
//        });

        return v;
    }

//    public void sharedprefs() {
//        SharedPreferences sp = getContext().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt("i", i);
//        editor.commit();
//    }

    public void Intent(String message, String value) {


        Intent intent = new Intent("custom-action-local-broadcast");
        intent.putExtra(message, value);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        SharedPreferences sp = getContext().getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(message, value);
        editor.commit();

    }
}