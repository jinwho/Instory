package com.jica.instory.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//not use yet
public class MyFileManager {

    private static MyFileManager INSTANCE = null;

    private MyFileManager() {
    }

    public static MyFileManager getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new MyFileManager();
        }
        return INSTANCE;
    }

    public boolean saveImage(Bitmap bitmap, Context context, String filename) {
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Bitmap loadImage(Context context, String filename) {
        Bitmap bitmap = null;
        try {
            FileInputStream inputStream = context.openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}