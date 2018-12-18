package com.alixlp.ship.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alixlp.ship.R;
import com.alixlp.ship.bean.Goods;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

    private Context context;
    private List<Goods> mDatas;

    public GoodsAdapter(Context context, List<Goods> data) {
        this.context = context;
        this.mDatas = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(mDatas.get(position).getTitle());
        holder.num.setText(mDatas.get(position).getNum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("这里是点击每一行item的响应事件", "" + position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView num;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.id_tv_goods_name);
            num = itemView.findViewById(R.id.id_tv_goods_num);

        }
    }
}

