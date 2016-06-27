package com.boredream.interiodesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boredream.interiodesign.R;
import com.boredream.interiodesign.entity.Community;

import java.util.ArrayList;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Community> datas;

    public CommunityAdapter(Context context, ArrayList<Community> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_address;
        public TextView tv_public_offset;
        public TextView tv_types;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_public_offset = (TextView) itemView.findViewById(R.id.tv_public_offset);
            tv_types = (TextView) itemView.findViewById(R.id.tv_types);
        }
    }

    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_community, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Community data = datas.get(position);
        holder.tv_name.setText(data.getName());
        holder.tv_address.setText("[" + data.getArea() + "]" + data.getAddress());
        holder.tv_public_offset.setText("公摊面积：" + data.getPublic_offset());
        holder.tv_types.setText(data.getCommunityTypeStr());
    }


}
