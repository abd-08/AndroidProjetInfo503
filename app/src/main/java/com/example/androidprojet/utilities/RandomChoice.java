package com.example.androidprojet.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.androidprojet.models.Personne;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RandomChoice {

    public static Personne run(ArrayList<Personne> list){
        int aleatoire = rand(0, list.size());
        Personne personne = list.get(aleatoire);
        list.remove(aleatoire);
        return personne ;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static int rand(int min, int max){
        return (int ) (min + Math.random()*(max-min));
    }
}


