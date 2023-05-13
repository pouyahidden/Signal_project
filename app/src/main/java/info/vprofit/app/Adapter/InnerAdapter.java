package info.vprofit.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import info.vprofit.app.Model.Inner;
import com.example.signal.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;




public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.ViewHolder> {

    private Context context;
    private List<Inner> innerList;

    public InnerAdapter() {
        innerList = new ArrayList<>();
    }

    public void addInner(List<Inner> inners) {
        innerList.clear();
        innerList.addAll(inners);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.signal_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return innerList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.vall)
        TextView vall;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            Inner inner = innerList.get(position);
            if (inner != null) {
            //    imgContent.setImageDrawable(ContextCompat.getDrawable(context, inner.getDrawableId()));
                name.setText(inner.getName());
                vall.setText(inner.getVall());
            }
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context, vall.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
