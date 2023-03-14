package com.example.signal.Adapter;

import static com.example.signal.Model.ChatItemModel.System_chat;
import static com.example.signal.Model.ChatItemModel.User_chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signal.Model.ChatItemModel;
import com.example.signal.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    private final List<ChatItemModel> itemClassList;

    // public constructor for this class
    public ChatAdapter(List<ChatItemModel> itemClassList) {
        this.itemClassList = itemClassList;
    }

    // Override the getItemViewType method.
    // This method uses a switch statement
    // to assign the layout to each item
    // depending on the viewType passed

    @Override
    public int getItemViewType(int position) {
        switch (itemClassList.get(position).getViewType()) {
            case 0:
                return User_chat;
            case 1:
                return System_chat;
            default:
                return -1;
        }
    }

    // Create classes for each layout ViewHolder.

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case User_chat:
                View layoutOne = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat, parent, false);
                return new LayoutOneViewHolder(layoutOne);
            case System_chat:
                View layoutTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.system_chat, parent, false);
                return new LayoutTwoViewHolder(layoutTwo);
            default:
                return null;
        }
    }

    // similarly a class for the second layout is also
    // created.

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (itemClassList.get(position).getViewType()) {
            case User_chat:
                int avatar = itemClassList.get(position).getIcon1();
                String ttl = itemClassList.get(position).getTitle1();
                String dds = itemClassList.get(position).getDescription1();
                String tim = itemClassList.get(position).getTime1();

                ((LayoutOneViewHolder) holder).setView(avatar ,ttl,dds,tim);

//                ((LayoutOneViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Toast.makeText(view.getContext(), "Hello from Layout One!", Toast.LENGTH_SHORT).show();
//                    }
//                });

                break;

            case System_chat:
                int image = itemClassList.get(position).geticon();
                String text_one = itemClassList.get(position).getTitle();
                String text_two = itemClassList.get(position).getDescription();
                String time = itemClassList.get(position).getTime();

                ((LayoutTwoViewHolder) holder).setViews(image, text_one, text_two,time);
                //((LayoutTwoViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
               //     @Override
                //    public void onClick(View view) {

                //        Toast.makeText(view.getContext(), "Hello from Layout Two!", Toast.LENGTH_SHORT).show();
              //      }
            //    });
                break;
            default:
                return;
        }
    }

    // In the onCreateViewHolder, inflate the
    // xml layout as per the viewType.
    // This method returns either of the
    // ViewHolder classes defined above,
    // depending upon the layout passed as a parameter.

    @Override
    public int getItemCount() {
        return itemClassList.size();
    }

    // In onBindViewHolder, set the Views for each element
    // of the layout using the methods defined in the
    // respective ViewHolder classes.

    class LayoutOneViewHolder extends RecyclerView.ViewHolder {
        private final ImageView icon;
   //     private final TextView titles;
        private final TextView descriptions;
        private final TextView times;
        private final ConstraintLayout card;
       // private final TextView times;
        //private final TextView textview;
      //  private final LinearLayout linearLayout;

        public LayoutOneViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
           // titles = itemView.findViewById(R.id.title);
            descriptions = itemView.findViewById(R.id.description);
            times = itemView.findViewById(R.id.time);
            card= itemView.findViewById(R.id.card);

            // Find the Views
          //  textview = itemView.findViewById(R.id.text);
         //   linearLayout = itemView.findViewById(R.id.linearlayout);
        }

        // method to set the views that will
        // be used further in onBindViewHolder method.
        private void setView(int image1, String title1, String description1,String time1) {

            icon.setImageResource(image1);
          //  titles.setText(title1);
            descriptions.setText(description1);
            times.setText(time1);
        }
    }

    // This method returns the count of items present in the
    // RecyclerView at any given time.

    class LayoutTwoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView icon;
       // private final TextView titles;
        private final TextView descriptions;
        private final TextView times;
        //private final LinearLayout linearLayout;

        public LayoutTwoViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
          //  titles = itemView.findViewById(R.id.title);
            descriptions = itemView.findViewById(R.id.description);
            times = itemView.findViewById(R.id.time);
         //   linearLayout = itemView.findViewById(R.id.linearlayout);
        }

        private void setViews(int image, String title, String description,String time) {
            icon.setImageResource(image);
         //   titles.setText(title);
            descriptions.setText(description);
            times.setText(time);
        }
    }
}