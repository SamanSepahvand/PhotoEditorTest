package com.samansepahvand.photoeditortest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.location.LocationRequestCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class OutputImageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgOutputImage;

    private Button btnSave;
private      String imgPath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_image);

        initView();

    }


    public void initView(){

        imgOutputImage=findViewById(R.id.output_image);

        btnSave=findViewById(R.id.btn_save);




        Bundle bundle=getIntent().getExtras();
         imgPath=bundle.getString("imagePath");

        imgOutputImage.setImageURI(Uri.parse(imgPath));

        imgOutputImage.setOnClickListener(this);
        btnSave.setOnClickListener(this);
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
            }
            galleryAddPic(saveImagePath);
            Toast.makeText(OutputImageActivity.this, "Image Save", Toast.LENGTH_SHORT).show();
        }
        return saveImagePath;
    }

    private void galleryAddPic(String saveImagePath) {

        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file=new File(saveImagePath);
        Uri contentUri=Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

    }
}