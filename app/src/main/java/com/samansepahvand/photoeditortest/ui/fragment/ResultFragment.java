package com.samansepahvand.photoeditortest.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.samansepahvand.photoeditortest.R;
import com.samansepahvand.photoeditortest.ui.activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment implements View.OnClickListener, MainActivity.OnAboutDataReceivedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";





    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ImageView imgOutputImage;

    private ImageView btnSave;
    private      String imgPath ;


    MainActivity mActivity=new MainActivity();


    public ResultFragment() {
        // Required empty public constructor
    }




    public static ResultFragment newInstance( String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mActivity = (MainActivity) getActivity();
        mActivity.setAboutDataListener(this);

    }

    private void initView(View view){



   //    ChangeStatusBar();


        imgOutputImage=view.findViewById(R.id.output_image);

        btnSave=view.findViewById(R.id.btn_save);

        imgOutputImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser);
          //  ChangeStatusBar();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_result, container, false);

         initView(view);
        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.output_image:

                break;

            case R.id.btn_save:

                String dirPath=
                        Environment.getExternalStorageDirectory().getAbsolutePath()
                                +"/"+getString(R.string.app_name)+"/";

                String fileName=imgPath.substring(imgPath.lastIndexOf('/')+1);

                File dir=new File(dirPath);

                imgOutputImage.buildDrawingCache();
                Bitmap bitmap=imgOutputImage.getDrawingCache();
                saveImage(bitmap,dir,fileName);

                break;

        }
    }


    private String saveImage(Bitmap image, File storageDir,String imageFileName){

        String saveImagePath=null;

        boolean isSuccess=true;

        if (!storageDir.exists()){
            isSuccess=storageDir.mkdirs();

        }
        if (isSuccess){
            File imageFile=new File(storageDir,imageFileName);
            saveImagePath=imageFile.getAbsolutePath();
            try{

                OutputStream fOut=new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100,fOut);
                fOut.close();
            }catch (Exception e){
                e.printStackTrace();
                Log.e("TAG", "saveImage: " );
            }
            galleryAddPic(saveImagePath);

        }
        Toast.makeText(getActivity(), "Image Save", Toast.LENGTH_SHORT).show();
        return saveImagePath;
    }

    private void galleryAddPic(String saveImagePath) {

        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file=new File(saveImagePath);
        Uri contentUri=Uri.fromFile(file);

        mediaScanIntent.setData(contentUri);

        getActivity().sendBroadcast(mediaScanIntent);

    }

    @Override
    public void onDataReceived(String photoUrl) {

        imgPath = photoUrl;
        imgOutputImage.setImageURI(Uri.parse(imgPath));

    }




}