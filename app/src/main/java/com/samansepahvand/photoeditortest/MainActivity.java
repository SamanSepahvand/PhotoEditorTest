package com.samansepahvand.photoeditortest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.samansepahvand.photoeditortest.utility.FileUtility;
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;

import java.io.File;

import iamutkarshtiwari.github.io.ananas.editimage.EditImageActivity;
import iamutkarshtiwari.github.io.ananas.editimage.ImageEditorIntentBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    private ImageView imgGallery,imgCamera;
//request code from gallery
public static final int REQUEST_CODE_PICKER=100;

public static final int REQUEST_PHOTO_EDIT=231;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         initView();


    }


    private void initView(){

        imgCamera=findViewById(R.id.img_camera);
        imgGallery=findViewById(R.id.img_gallery);



        //onClick Handle

        imgGallery.setOnClickListener(this);
        imgCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.img_camera:

                openCamera();
                break;
            case R.id.img_gallery:
                openGallery();
                break;

        }
    }

    private void openCamera(){

        LongImageCameraActivity.launch(MainActivity.this);
    }
    private void openGallery(){

        Intent intentGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery,REQUEST_CODE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //this block of code camera

        if (resultCode==RESULT_OK && requestCode==LongImageCameraActivity.LONG_IMAGE_RESULT_CODE && data!=null){
            String imgPath=data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
            editImage(imgPath);
        }


        //this block of code gallery
        if (resultCode==RESULT_OK && requestCode==REQUEST_CODE_PICKER ){
            Uri imgUri=data.getData();
            String[] filePathColumn={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(imgUri,filePathColumn,null,null,null);
            cursor.moveToNext();
            int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
            String imgPath=cursor.getString(columnIndex);

            editImage(imgPath);
        }

        //this block of code editor

        if(requestCode==REQUEST_PHOTO_EDIT){

            String newFilePath=data.getStringExtra(ImageEditorIntentBuilder.OUTPUT_PATH);
            boolean isImageEdit=data.getBooleanExtra(EditImageActivity.IS_IMAGE_EDITED,false);

            if (isImageEdit){


            }else{
                newFilePath=data.getStringExtra(ImageEditorIntentBuilder.SOURCE_PATH);
            }

            startActivity(new Intent(getApplicationContext(),OutputImageActivity.class)
                          .putExtra("imagePath",newFilePath));

        }



    }

    private void editImage(String imgPath) {

        try{

            File outputFile= FileUtility.genEditFile();

            Intent intent=new ImageEditorIntentBuilder(MainActivity.this,imgPath,outputFile.getAbsolutePath())
                    .withAddText()
                    .withPaintFeature()
                    .withFilterFeature()
                    .withRotateFeature()
                    .withCropFeature()
                    .withBrightnessFeature()
                    .withSaturationFeature()
                    .withBeautyFeature()
                    .withStickerFeature()
                    .forcePortrait(true)
                    .setSupportActionBarVisibility(false)
                    .build();

            EditImageActivity.start(MainActivity.this,intent,REQUEST_PHOTO_EDIT);

        }catch (Exception e){

        }
    }
}

