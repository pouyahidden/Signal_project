package com.example.signal.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.signal.Model.Outer;
import com.example.signal.R;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OuterAdapter extends RecyclerView.Adapter<OuterAdapter.ViewHolder> {

    private final RecyclerView.RecycledViewPool recycledViewPool;
    private Context context;

    private List<Outer> outerList;

    public OuterAdapter() {
        outerList = new ArrayList<>();
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    public void addOuter(Outer outer) {
        outerList.add(outer);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.signal_recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.rvOuter.setRecycledViewPool(recycledViewPool);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return outerList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view)
        ConstraintLayout view2;
        @BindView(R.id.pipcon)
        ConstraintLayout pipcon;
        @BindView(R.id.less)
        ConstraintLayout less;
        @BindView(R.id.info)
        ConstraintLayout info;
        @BindView(R.id.show)
        ConstraintLayout show;
        @BindView(R.id.type)
        TextView trade_type;
        @BindView(R.id.jens)
        TextView currency_mark;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.pipprice)
        TextView pipprice;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.status_text)
        TextView mark;
        @BindView(R.id.description)
        HtmlTextView description;
        @BindView(R.id.child_recycler)
        RecyclerView rvOuter;
        private InnerAdapter innerAdapter;
        private int shortAnimationDuration= 500;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setupRv();
        }

        private void setupRv() {
            rvOuter.setHasFixedSize(true);

            rvOuter.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            innerAdapter = new InnerAdapter();
            rvOuter.setAdapter(innerAdapter);

        }


        public void bind(int position) {
            Outer outer = outerList.get(position);

            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    info.setVisibility(View.VISIBLE);
                    show.setVisibility(View.GONE);

                    info.setAlpha(0f);
                    info.animate()
                            .alpha(1f)
                            .setDuration(shortAnimationDuration)
                            .setListener(null);

                }
            });

            less.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    info.setVisibility(View.GONE);
                    show.setVisibility(View.VISIBLE);
                }
            });

            if (outer != null) {


                time.setText(outer.getHuman_date());
                currency_mark.setText(outer.getCurrency_mark());

                String s_des = outer.getMark();

                if (s_des.equals("null")){
                    description.setVisibility(View.GONE);
                }else {
                    description.setHtml(s_des);
                }

              //  String text = TextUtils.htmlEncode();
                //    description.setText(text);
                mark.setText(outer.getDescription());

                String color = outer.getTrade_type();

                String titles= outer.getTitle();
                if (titles.equals("null")){
                    title.setVisibility(View.GONE);

                }else {
                    title.setText(outer.getTitle());
                }

                String pipi = outer.getProfit();
                if (pipi.equals("null")) {
                    pipcon.setVisibility(View.GONE);
                }else {
                    pipprice.setText(outer.getProfit());
                }


                    if (color.equals("Sell")) {
                        trade_type.setTextColor(Color.parseColor("#FF5656"));
                        trade_type.setText(color);

                    } else {
                        trade_type.setTextColor(Color.parseColor("#5FC5A2"));
                        trade_type.setText(color);
                    }
                }

            assert outer != null;
            innerAdapter.addInner(outer.getSignalss());
            }
        }

    }

