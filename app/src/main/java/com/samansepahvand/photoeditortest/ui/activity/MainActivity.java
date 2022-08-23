package com.samansepahvand.photoeditortest.ui.activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.USE_FULL_SCREEN_INTENT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.samansepahvand.photoeditortest.R;
import com.samansepahvand.photoeditortest.utility.FileUtility;
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import iamutkarshtiwari.github.io.ananas.editimage.EditImageActivity;
import iamutkarshtiwari.github.io.ananas.editimage.ImageEditorIntentBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String mCurrentPhotpPath;

    public static final int PERMISSION_REQUEST_CODE=5010;

    private ImageView imgGallery, imgCamera;
    //request code from gallery
    public static final int REQUEST_CODE_PICKER = 100;
    public static final int REQUEST_PICKER_LIBRARY = 500;


    public static final int REQUEST_PHOTO_EDIT = 231;
    public static final int CAMERA_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();


    }


    private void initView() {

        imgCamera = findViewById(R.id.img_camera);
        imgGallery = findViewById(R.id.img_gallery);


        //onClick Handle

        imgGallery.setOnClickListener(this);
        imgCamera.setOnClickListener(this);


        imgCamera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editImage(getOriginalImagePath());
                return false;

            }
        });

        if (!checkPermission()) {
            requestPermission();
        }



    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {

                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if  (shouldShowRequestPermissionRationale(CAMERA)


                            ) {
                                showMessageOKCancel("دسترسی تایید نشد! شما اجازه استفاده از سایر قسمت های سیستم را نداری  ",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/iran_sans.ttf");
        TextView content = new TextView(this);
        content.setText("برای استفاده از سایر امکانات  برنامه به دسترسی ها نیاز داریم!");
        content.setTypeface(face);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.alert));
        alertDialogBuilder.setTitle("درخواست دسترسی");
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder.setView(content);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                requestPermission();
            }
        });
        alertDialogBuilder.setNeutralButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_camera:

                openCamera();
                break;
            case R.id.img_gallery:
                openGallery();
                break;

        }
    }

    private void openCamera() {

        try {

            String fileName="photo";
            File storageDirectory=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                mCurrentPhotpPath=imageFile.getAbsolutePath();
             Uri imgUri=   FileProvider.getUriForFile(MainActivity.this,"com.samansepahvand.photoeditortest.fileprovider",imageFile);
             Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
             startActivityForResult(intent,CAMERA_REQUEST);
            }catch (Exception e){
                e.printStackTrace();
            }


        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Failed to load camera", Toast.LENGTH_SHORT).show();
        }
    }



    private void openGallery() {

        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, REQUEST_CODE_PICKER);
    }



    public String getOriginalImagePath() {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int column_index_data = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        return cursor.getString(column_index_data);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == -1 && requestCode == CAMERA_REQUEST) {

            Bitmap bitmap= BitmapFactory.decodeFile(mCurrentPhotpPath);
            imgCamera.setImageBitmap(bitmap);
           editImage(mCurrentPhotpPath);



        }
        //this block of code camera
        if (resultCode == RESULT_OK && requestCode == LongImageCameraActivity.LONG_IMAGE_RESULT_CODE && data != null) {
            String imgPath = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
            editImage(imgPath);
        }


        //this block of code gallery
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICKER) {
            Uri imgUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imgUri, filePathColumn, null, null, null);
            cursor.moveToNext();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(columnIndex);

            editImage(imgPath);

        }

        //this block of code editor
        if (requestCode == REQUEST_PHOTO_EDIT) {
            String newFilePath = data.getStringExtra(ImageEditorIntentBuilder.OUTPUT_PATH);
            boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IS_IMAGE_EDITED, false);

            if (isImageEdit) {

            } else {
                newFilePath = data.getStringExtra(ImageEditorIntentBuilder.SOURCE_PATH);
            }
            startActivity(new Intent(getApplicationContext(), OutputImageActivity.class)
                    .putExtra("imagePath", newFilePath));

        }


    }

    private void editImage(String imgPath) {

        try {

            File outputFile = FileUtility.genEditFile();

            Intent intent = new ImageEditorIntentBuilder(MainActivity.this, imgPath, outputFile.getAbsolutePath())
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

            EditImageActivity.start(MainActivity.this, intent, REQUEST_PHOTO_EDIT);


        } catch (Exception e) {
            Log.e("TAG", "editImage: "+e.getMessage() );
        }
    }




}

