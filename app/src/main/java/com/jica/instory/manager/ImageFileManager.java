package com.jica.instory.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
//not use yet
public class ImageFileManager {
    FileOutputStream outputStream;
    FileInputStream inputStream;
    String filename;
    Context context;

    public ImageFileManager(Context context) {
        this.context = context;
    }

    Bitmap readImage(String filename) {
        Bitmap bitmap = null;
        try {
            inputStream = context.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    void saveImage(Bitmap bitmap,String filename){
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
