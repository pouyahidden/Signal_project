package com.example.signal.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.signal.Model.Inner;
import com.example.signal.Model.TicketModel;
import com.example.signal.R;
import com.example.signal.UI.TicketView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.RVHOLDER> {


    List<TicketModel> recyclerViewDataList;
    Context mContext;

    // Constructor with List and Context which we'll pass from RecyclerView Activity after a connection to Volley. And application context for the listener that we'll implement this later.
        public TicketAdapter(List<TicketModel> recyclerViewDataList, Context mContext) {
        this.recyclerViewDataList = recyclerViewDataList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public RVHOLDER onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.message_rec_item, viewGroup, false);
        RVHOLDER rvholder = new RVHOLDER(itemView);
        return rvholder;
    }
    @SuppressLint("RecyclerView")    @Override
    public void onBindViewHolder(@NonNull RVHOLDER holder, int i) {



        holder.title.setText(recyclerViewDataList.get(i).getSubject());
        holder.time.setText(recyclerViewDataList.get(i).getHumanDate());
       // holder.periority.setText(recyclerViewDataList.get(i).getPriority());
        holder.ticketid.setText("ticket id : "+recyclerViewDataList.get(i).getTicket_id());
        holder.department.setText(recyclerViewDataList.get(i).getDepartment());
        holder.message.setText(recyclerViewDataList.get(i).getMessage());


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), TicketView.class);
                intent.putExtra("id", recyclerViewDataList.get(i).getId());
                intent.putExtra("ticket_id",recyclerViewDataList.get(i).getTicket_id());
                intent.putExtra("subject",recyclerViewDataList.get(i).getSubject());
                v.getContext().startActivity(intent);



            }
        });
    }



    @Override
    public int getItemCount() {
        return recyclerViewDataList.size();
    }

    public class RVHOLDER extends RecyclerView.ViewHolder {

        MaterialCardView card;
        private final TextView title,time,ticketid,department,message;



        public RVHOLDER(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
          //  periority = itemView.findViewById(R.id.periority);
            ticketid = itemView.findViewById(R.id.ticketid);
            department = itemView.findViewById(R.id.department);
            message = itemView.findViewById(R.id.description);


        }
    }
}

