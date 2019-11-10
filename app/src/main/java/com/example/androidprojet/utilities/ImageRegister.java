package com.example.androidprojet.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageRegister {

    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
       // root = getFilesDir().toString();

        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Shutta_" + timeStamp + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
           // personne.setPath(fname);
           // Sauvegarde();
            if (file.exists()) {
                getBitmap(fname);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBitmap(String fname) {
        String root = Environment.getExternalStorageDirectory().toString();
        //root = getFilesDir().toString();

        File myDir = new File(root + "/saved_images");
        File file = new File(myDir, fname);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
               // photo.setImageBitmap(bitmap);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
