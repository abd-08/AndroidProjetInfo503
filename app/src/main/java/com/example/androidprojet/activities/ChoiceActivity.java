package com.example.androidprojet.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.androidprojet.R;
import com.example.androidprojet.adapters.AdapterChoix;
import com.example.androidprojet.models.Categorie;
import com.example.androidprojet.models.Personne;
import com.example.androidprojet.utilities.MyJSONFile;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ChoiceActivity extends Activity {

    Activity activity =this;
    private GridView gridView;
    private Categorie categorie;
    private MyJSONFile myJSONFile;
    private Button lancer;
    private EditText _editText;
    private int parentPosition;
    LinearLayout linearlayout;
    AdapterChoix adapterChoix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        gridView = findViewById(R.id._choice_gridview);
        registerForContextMenu(this.gridView);
        _editText = findViewById(R.id._choice_editText);
        lancer= findViewById(R.id._choice_btn_run);
        linearlayout = findViewById(R.id.choice_groupe_edit);
        myJSONFile = MyJSONFile.get(getApplicationContext());



        Bundle extras = getIntent().getExtras();
        String mode = extras.getString("mode");
        Log.i("test mode", mode);
        parentPosition  = extras.getInt("parent_position");
        this._editText.setText(mode);




        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("parent_position", parentPosition);
                startActivity(intent);

            }
        });

        this.lancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,LanceurActivity.class);
                intent.putExtra("parent_position", parentPosition);
                startActivity(intent);
            }
        });


        mode(mode);
    }


        public void btn_choice_add_clicked(View view) {
        String name = _editText.getText().toString();
        Personne personne = new Personne(name);
        categorie.Ajouter(personne);
        categorie.AjouterRestant(personne);
        Sauvegarde();
        adapterChoix.setPersonneItemList(categorie.getListe());
        gridView.setAdapter(adapterChoix);
    }


    public void mode(String string){
        if (string == " reste"){
            this.lancer.setVisibility(View.INVISIBLE);
            this.linearlayout.setVisibility(View.INVISIBLE);
            this._editText.setVisibility(View.INVISIBLE);
        }
    }

    public void Sauvegarde(){
        myJSONFile.setMyCategoriedatas( parentPosition,categorie);
        try {
            myJSONFile.saveData(myJSONFile.getMydatas());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            categorie = myJSONFile.getCategorie(parentPosition);
            adapterChoix = new AdapterChoix(this,categorie.getListe());
            gridView.setAdapter(adapterChoix);
            chargeImageAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void chargeImageAdapter(){
         ArrayList<Bitmap> imagePersonne = new ArrayList<Bitmap>() ;
         for (int i=0 ; i<categorie.getListe().size();i++){
             if (categorie.getPersonne(i).getPath().length()>3){
                 imagePersonne.add(getBitmap(categorie.getPersonne(i).getPath()));
             }
             else imagePersonne.add(null);
         }

         adapterChoix.setImagePersonne(imagePersonne);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.personne_menu,menu);
    }




    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){

            case R.id.supprimer_personne:
                categorie.Remove(info.position);
                adapterChoix.setPersonneItemList(categorie.getListe());
                gridView.setAdapter(adapterChoix);
                Sauvegarde();
                return true;
            case R.id.mode_personne:
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }


    public Bitmap getBitmap(String fname) {
        Bitmap bmp=null;
        String root = Environment.getExternalStorageDirectory().toString();
        root = getFilesDir().toString();

        File myDir = new File(root + "/saved_images");
        File file = new File(myDir, fname);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                bmp = BitmapFactory.decodeStream(fis);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bmp;
    }
}
