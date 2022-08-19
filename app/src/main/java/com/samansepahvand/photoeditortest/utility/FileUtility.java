package com.samansepahvand.photoeditortest.utility;

import android.os.Build;
import android.os.Environment;

import java.io.File;

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
}
