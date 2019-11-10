package com.example.androidprojet.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidprojet.R;
import com.example.androidprojet.activities.ProfilActivity;
import com.example.androidprojet.models.Personne;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class AdapterChoix extends BaseAdapter {



    //attribut
    private Context context;
    private  List<Personne> personneItemList;
    private List<Bitmap> imagePersonne;
    private LayoutInflater inflater;

    public AdapterChoix(Context context, List<Personne> matriceItemList) {
        this.context = context;
        this.personneItemList = matriceItemList;
        this.inflater = LayoutInflater.from(context);
    }

    public void setPersonneItemList(List<Personne> personneItemList) {
        this.personneItemList = personneItemList;
    }

    @Override
    public int getCount() {
        if (this.personneItemList==null) return 0;
        else return this.personneItemList.size();
    }

    public void setImagePersonne(List<Bitmap> imagePersonne) {
        this.imagePersonne = imagePersonne;
    }

    @Override
    public Personne getItem(int position) {
        return personneItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.personne, null);

        //recupération des information des item
        Personne currentItem = getItem(position);

        String itemName = currentItem.getNom();

        if ( itemName.length()>9){
            itemName = (String) itemName.subSequence(0,7);
            itemName = itemName + "..";
        }

        TextView itemNameView = convertView.findViewById(R.id.nom_profile);
        ImageView imageView = convertView.findViewById(R.id.personne_image);
        itemNameView.setText(itemName);
        if (imagePersonne.get(position)!=null) {
            imageView.setImageBitmap(imagePersonne.get(position));
        }

        return convertView;
    }


}


