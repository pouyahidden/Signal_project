package info.vprofit.app.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import info.vprofit.app.Adapter.BottomSheetRecycler;
import info.vprofit.app.ItemClickListener;
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
        ArrayList<String> votedByList = getActivity().getIntent().getStringArrayListExtra("filterlist");

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

                dismiss();
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BottomSheetRecycler(votedByList, itemClickListener);
        recyclerView.setAdapter(adapter);
        return v;
    }


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