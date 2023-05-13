package info.vprofit.app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import info.vprofit.app.Model.NotifItem;
import com.example.signal.R;

import java.util.List;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.RVHOLDER>  {

    List<NotifItem> doreItemList;
    Context mContext;

    // Constructor with List and Context which we'll pass from RecyclerView Activity after a connection to Volley. And application context for the listener that we'll implement this later.
    public NotifAdapter(List<NotifItem> doreItems, Context mContext) {
        this.doreItemList = doreItems;
        this.mContext = mContext;
    }

    // Override the method onCreateViewHolder, which will call the custom view holder that needs to be initialized. We specify the layout that each item to be used, so we can achieve this using Layout Inflator to inflate the layout and passing the output to constructor of custom ViewHolder.
    @NonNull
    @Override
    public NotifAdapter.RVHOLDER onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.notifitems, viewGroup, false);
        NotifAdapter.RVHOLDER rvholder = new NotifAdapter.RVHOLDER(itemView);
        return rvholder;
    }


    //onBindViewHolder is for specifying the each item of RecyclerView. This is very similar to getView() method on ListView. In our example, this is where you need to set the user's id, name and image.
    @Override
    public void onBindViewHolder(@NonNull NotifAdapter.RVHOLDER rvholder, @SuppressLint("RecyclerView") int i) {


        rvholder.title.setText(doreItemList.get(i).getTitle());
        rvholder.des.setText(doreItemList.get(i).getDescription());
        rvholder.time.setText(doreItemList.get(i).getCreat());





        rvholder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(v.getContext(), DasteBandi.class);
//                intent.putExtra("id",doreItemList.get(i).getId());
//                intent.putExtra("title",doreItemList.get(i).getTitle());
//                v.getContext().startActivity(intent);


            }
        });
    }

    //We need to return the size for RecyclerView as how long a RecyclerView is, Our data is in list so passing data.size() will return the number as long as we have.
    @Override
    public int getItemCount() {
        return doreItemList.size();
    }

    //This is CustomView holder that we had discuss it earlier above and inflated it in onCreateView() method. This constructor links with the xml to set the data, which we set into onBindViewHolder().
    class RVHOLDER extends RecyclerView.ViewHolder {

        ConstraintLayout card;
        public ImageView axthub;
        private final TextView title;
        private final TextView des;
        private final TextView time;


        public RVHOLDER(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            title = itemView.findViewById(R.id.title1);
            des = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.time);

        }
    }


}
