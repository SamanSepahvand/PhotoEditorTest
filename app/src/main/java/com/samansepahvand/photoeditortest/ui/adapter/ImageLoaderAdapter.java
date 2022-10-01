package com.samansepahvand.photoeditortest.ui.adapter;

import android.app.Activity;
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
import com.samansepahvand.photoeditortest.metamodel.PhotoViewMetaModel;
import com.samansepahvand.photoeditortest.ui.dialog.ShowImageDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ImageLoaderAdapter extends RecyclerView.Adapter<ImageLoaderAdapter.ViewHolder> {

    private List<Bitmap> datas = new ArrayList<>();
    private  List<String> arrayListPaths;

    private Context mContext;

    public ImageLoaderAdapter(List<Bitmap> datas, List<String> arrayListPath, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
        this.arrayListPaths=arrayListPath;

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

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoViewMetaModel model =new PhotoViewMetaModel();

                model.setTitle(arrayListPaths.get(getAdapterPosition()).toLowerCase(Locale.ROOT).toString());
                model.setDescription(arrayListPaths.get(getAdapterPosition()).toLowerCase(Locale.ROOT).toString()+"ffdsfsdf");
                model.setDate(Calendar.getInstance().getTime().toString());

                model.setImageUrl(arrayListPaths.get(getAdapterPosition()));
                ShowImageDialog showImageDialog=    new ShowImageDialog((Activity) mContext,model );
                showImageDialog.show();
            }
        });
        }
    }
}
