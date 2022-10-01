package com.samansepahvand.photoeditortest.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.samansepahvand.photoeditortest.R;
import com.samansepahvand.photoeditortest.metamodel.PhotoViewMetaModel;

import java.util.ArrayList;
import java.util.List;

public class ShowImageDialog extends Dialog  {

    private static final String TAG = "SearchViewDialog";
    private Activity mContext;

    private ConstraintLayout root;

    TextView txtDate, txtTitle, txtFullDetails;
    ImageView  imgBack;
    ConstraintLayout rootDialog, rootToolbar;
    NestedScrollView nestedscrollview;
    PhotoView photoView;
    int count = 2;



    public ShowImageDialog(@NonNull Activity context,PhotoViewMetaModel photoViewMetaModel) {
        super(context);
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_show_image_chat);
        this.getWindow().getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        initView();
        initDataDialog(photoViewMetaModel);


    }

    private void initView() {



        txtDate = findViewById(R.id.txt_date);
        txtFullDetails = findViewById(R.id.txt_full_details);
        txtTitle = findViewById(R.id.txt_title);
        // imgMain=dialog.findViewById(R.id.main_image);
        imgBack = findViewById(R.id.img_back);
        photoView = findViewById(R.id.main_image);
        rootDialog = findViewById(R.id.root);
        rootToolbar = findViewById(R.id.constraintLayout);
        nestedscrollview = findViewById(R.id.nestedscrollview);

        imgBack.setOnClickListener(view -> {
            dismiss();
        });

        photoView.setOnClickListener(view -> {
            if (count % 2 == 0) {
                rootToolbar.setVisibility(View.GONE);
                txtFullDetails.setVisibility(View.GONE);
                nestedscrollview.setVisibility(View.GONE);
            } else {
                rootToolbar.setVisibility(View.VISIBLE);
                txtFullDetails.setVisibility(View.VISIBLE);
                nestedscrollview.setVisibility(View.VISIBLE);
            }
            count++;
        });

    }

    private void initDataDialog(PhotoViewMetaModel photoViewMetaModel) {
        txtTitle.setText(GetTitlePic(photoViewMetaModel.getTitle()));
        String str = ShowDisc(photoViewMetaModel);
        Spanned strHtml = Html.fromHtml(str);
        txtFullDetails.setText(strHtml);
        txtDate.setText(photoViewMetaModel.getDate());
        Glide.with(mContext)
                .load(photoViewMetaModel.getImageUrl())
                .error(R.drawable.no_image_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);
    }


    private String GetTitlePic(String FullPath){
       return   FullPath.split("/")[FullPath.split("/").length-1];


    }
    private String  ShowDisc(PhotoViewMetaModel photoViewMetaModel) {

            String data = "<b>Details</b> " +
                    "<br/>" +
                    "<br/>" +
                    "<br/>" +
                    "Name: " +
                    "<br/>" +
                    " " +GetTitlePic(photoViewMetaModel.getTitle())+
                    "<br/>" +
                    "Path:" +
                    "<br/>" +
                    " " +photoViewMetaModel.getDescription()+
                    "<br/>" +
                    "Date : " +
                    "<br/>" +
                    " " +photoViewMetaModel.getDate();

        return data;


    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public  static Dialog Dismiss(){
        return Dismiss();
    }

}
