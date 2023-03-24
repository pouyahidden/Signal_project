package com.example.signal.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signal.ItemClickListener;
import com.example.signal.R;

import java.util.ArrayList;

public class BottomSheetRecycler extends RecyclerView.Adapter<BottomSheetRecycler.ViewHolder> {

    // Initialize variable
    ArrayList<String> arrayList;
    ItemClickListener itemClickListener;
    int selectedPosition = -1;
    int shomare= 0;
    private Context context;;

    // Create constructor
    public BottomSheetRecycler(ArrayList<String> arrayList,
                       ItemClickListener itemClickListener)
    {
        this.arrayList = arrayList;
        this.itemClickListener = itemClickListener;

       //

    }

    @NonNull
    @Override
    public ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        // Initialize view
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottomsheet_recycler_items, parent,
                        false);
        // Pass holder view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder,
                                 int position)
    {


        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
        int text = sharedPreferences.getInt("KEY", -1);
       // Toast.makeText(context, text+"", Toast.LENGTH_SHORT).show();
        if (text==-1){
         holder.radioButton.setChecked(position==0);
        }else{
            holder.radioButton.setChecked(position==text);
            String s = Integer.toString(text);


         //   Intent("message", s);
        }





      //  Toast.makeText(context, ""+text, Toast.LENGTH_SHORT).show();
        holder.radioButton.setText(arrayList.get(position));



        holder.radioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton compoundButton, boolean b)
                    {

                        if (b) {

                            selectedPosition
                                    = holder.getAdapterPosition();

                            SharedPreferences sharedPreferences = context.getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("KEY", selectedPosition);
                            editor.apply();


                            itemClickListener.onClick(
                                    holder.radioButton.getText()
                                            .toString(),holder.getLayoutPosition());
                        }
                    }
                });
    }

    @Override public long getItemId(int position)
    {
        // pass position
        return position;
    }
    @Override public int getItemViewType(int position)
    {
        // pass position
        return position;
    }

    @Override public int getItemCount()
    {

        return arrayList == null ? 0 : arrayList.size();

    }

    public class ViewHolder
            extends RecyclerView.ViewHolder {
        // Initialize variable
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Assign variable
            radioButton
                    = itemView.findViewById(R.id.radiobtn);
        }
    }

//    public void Intent(String message ,String value ) {
//
//
//        Intent intent = new Intent("custom-action-local-broadcast");
//        intent.putExtra(message,value);
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
////
////        SharedPreferences sp = context.getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
////        SharedPreferences.Editor editor = sp.edit();
////        editor.putString(message, value);
////        editor.commit();
//
//    }
}

class GFG {
    public static void main(String[] args)
    {
        System.out.println("GFG!");
    }

}