package com.samansepahvand.photoeditortest.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileUtility {


    public static final String FOLDER_NAME="photoEditor";

    public static File createFolder(){

        File baseDir;
        if (Build.VERSION.SDK_INT<8){
            baseDir= Environment.getExternalStorageDirectory();

        }else{
            baseDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        }
        if (baseDir==null){
            return Environment.getExternalStorageDirectory();
        }
        File aviaryFolder=new File(baseDir,FOLDER_NAME);
        if (aviaryFolder.exists()){
            return aviaryFolder;
        }
        if (aviaryFolder.isFile()){
            aviaryFolder.delete();
        }
        if (aviaryFolder.mkdirs()){
            return aviaryFolder;
        }
        return Environment.getExternalStorageDirectory();

    }

    public static File genEditFile(){
        return FileUtility.getEmptyFile("1"+System.currentTimeMillis()+".jpg");

    }

    private static File getEmptyFile(String name) {

        File folder=FileUtility.createFolder();
        if (folder!=null){
            if (folder.exists()){
                File file=new File(folder,name);
                return file;
            }
        }
        return null;
    }


    private String saveImage(Bitmap image, File storageDir, String imageFileName, Context context){

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
            galleryAddPic(saveImagePath,context);
          //  Toast.makeText(OutputImageActivity.this, "Image Save", Toast.LENGTH_SHORT).show();
        }
        return saveImagePath;
    }

    private void galleryAddPic(String saveImagePath, Context context) {

        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file=new File(saveImagePath);

        Uri contentUri=Uri.fromFile(file);

        mediaScanIntent.setData(contentUri);

        context.sendBroadcast(mediaScanIntent);

    }


}
