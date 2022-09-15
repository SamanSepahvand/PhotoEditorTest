package com.samansepahvand.photoeditortest.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.samansepahvand.photoeditortest.R;

import java.util.ArrayList;
import java.util.List;

public class ImageLoaderAdapter extends RecyclerView.Adapter<ImageLoaderAdapter.ViewHolder> {

    private List<Bitmap> datas = new ArrayList<>();
    private Context mContext;

    public ImageLoaderAdapter(List<Bitmap> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_image_loader, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (datas != null)
            holder.imgView.setImageBitmap(datas.get(position));
        else
            holder.imgView.setImageResource(R.drawable.no_image);
    }

    @Override
    public int getItemCount() {

        if (datas != null)
            return datas.size();
        else return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.img_view);
        }
    }
}
