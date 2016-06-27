package com.boredream.interiodesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.interiodesign.R;
import com.boredream.interiodesign.entity.HouseType;
import com.boredream.interiodesign.net.GlideHelper;

import java.util.List;

public class HouseTypeAdapter extends RecyclerView.Adapter<HouseTypeAdapter.ViewHolder> {

    private Context context;
    private List<HouseType> datas;

    public HouseTypeAdapter(Context context, List<HouseType> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_name;
        public TextView tv_size;

        public ViewHolder(final View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_size = (TextView) itemView.findViewById(R.id.tv_size);
        }
    }

    @Override
    public HouseTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_house_type, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HouseType data = datas.get(position);
        GlideHelper.showImage(context, data.getPhotoPath(), holder.iv_image);
        holder.tv_title.setText(data.getTitle());
        holder.tv_name.setText(data.getTypeName());
        holder.tv_size.setText(data.getTypeInfo());
    }


}
