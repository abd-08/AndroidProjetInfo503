package com.example.androidprojet.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidprojet.R;
import com.example.androidprojet.models.Personne;
import com.example.androidprojet.utilities.MyJSONFile;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfilActivity extends AppCompatActivity {
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;

    Activity activity = this;
    boolean mode_modification = false;
    Personne personne;
    ImageView photo;
    ImageButton retour;
    ImageButton edit;
    ImageButton upload;
    ImageButton capture;
    Button valider;
    TextView mode;
    TextView nom;
    TextView prenom;
    TextView groupe;
    EditText edit_nom;
    EditText edit_prenom;
    EditText edit_groupe;
    LinearLayout visuel;
    LinearLayout modifier;
    MyJSONFile myJSONFile;
    private static final int PICK_IMAGE = 1;
    int parentPosition;
    int position;
    private String pictureFilePath = null;
    Bitmap bitmap;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        mode_modification = false;
        this.initilisation_Composant();

        myJSONFile = MyJSONFile.get(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        parentPosition = extras.getInt("parent_position");
        position = extras.getInt("position");
        personne = myJSONFile.getPersonne(parentPosition, position);
        initialisation_Valeur();

        //prendre une photo
        this.capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an implicit intent, for image capture.
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Start camera and wait for the results.
                startActivityForResult(camera, REQUEST_ID_IMAGE_CAPTURE);

            }
        });


        //uploader une photo de profil
        this.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });


        //button edit profil
        this.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activation_modification();
            }
        });

        //button valider modification
        this.valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valider_modification();
                Sauvegarde();
            }
        });

        //button retour
        this.retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //--Prendre une photo
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
                photo.setImageBitmap(bitmap);
                saveImage(bitmap);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }

        //--selctionne photo dans la gallerie
        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                photo.setImageBitmap(bitmap);
                saveImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
        }
    }



    //function annexe

    private void valider_modification() {
        this.mode_modification = false;
        //modification

        //pour le nom
        String champs = this.edit_nom.getText().toString();
        if (champs != personne.getNom() && champs != "") personne.setNom(champs);
        //pour le prenom
        champs = this.edit_prenom.getText().toString();
        if (champs != personne.getPrenom() && champs != "") personne.setPrenom(champs);
        //pour le grooupe
        champs = this.edit_groupe.getText().toString();
        if (champs != personne.getGroupe() && champs != "") personne.setGroupe(champs);

        this.mode.setText("Visualisation Profil");

        //on cache les champs de modification
        this.modifier.setVisibility(View.INVISIBLE);
        this.valider.setVisibility(View.INVISIBLE);

        //on affiche les champs pour la visualisation
        this.nom.setVisibility(View.VISIBLE);
        this.prenom.setVisibility(View.VISIBLE);
        this.groupe.setVisibility(View.VISIBLE);
        initialisation_Valeur();
        Sauvegarde();

    }

    private void activation_modification() {
        if (this.mode_modification == false) {
            this.mode_modification = true;
            this.mode.setText("Modification Profil");
            //on cache les champs  de visualisation
            this.nom.setVisibility(View.INVISIBLE);
            this.prenom.setVisibility(View.INVISIBLE);
            this.groupe.setVisibility(View.INVISIBLE);

            //zon affiche les edit text pour la modification
            this.modifier.setVisibility(View.VISIBLE);
            this.valider.setVisibility(View.VISIBLE);
        }
    }


    public void initilisation_Composant() {
        this.photo = findViewById(R.id.profil_image);
        this.retour = findViewById(R.id.retour_profil);
        this.edit = findViewById(R.id.edit_profil);
        this.upload = findViewById(R.id.profil_upload);
        this.capture = findViewById(R.id.profil_capture);
        this.valider = findViewById(R.id.profil_valider);
        this.mode = findViewById(R.id.mode_text);
        this.nom = findViewById(R.id.profil_nom);
        this.prenom = findViewById(R.id.profil_prenom);
        this.groupe = findViewById(R.id.profil_groupe);
        this.edit_nom = findViewById(R.id.profil_edit_nom);
        this.edit_prenom = findViewById(R.id.profil_edit_prenom);
        this.edit_groupe = findViewById(R.id.profil_edit_groupe);
        this.modifier = findViewById(R.id.profil_modifier);
        this.visuel = findViewById(R.id.profile_visualiser);

    }

    public void initialisation_Valeur() {
        this.nom.setText(personne.getNom());
        this.prenom.setText(personne.getPrenom());
        this.groupe.setText(String.valueOf(personne.getGroupe()));
        this.edit_nom.setText(personne.getNom());
        this.edit_prenom.setText(personne.getPrenom());
        this.edit_groupe.setText(String.valueOf(personne.getGroupe()));
        if (personne.path != null) {
            bitmap=getBitmap(personne.getPath());
            photo.setImageBitmap(bitmap);
        }
    }


    public void Sauvegarde() {
        try {
            myJSONFile.setMyPersonnedatas(parentPosition, position, personne);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            myJSONFile.saveData(myJSONFile.getMydatas());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        root = getFilesDir().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = personne.getNom() + timeStamp + ".jpg";

        //on supprime l ancien photo de la personne
        if (personne.getPath().length()>3){
            File file = new File(myDir, personne.getPath());
            if (file.exists()) {
                file.delete();
            }
        }

        File file = new File(myDir, fname);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            personne.setPath(fname);
            Sauvegarde();
        } catch (Exception e) {
            e.printStackTrace();
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

  public boolean imageExist(String name){
      String root = getFilesDir().toString();

      File myDir = new File(root + "/saved_images");
      File file = new File(myDir, name);
      if (file.exists()) {
          return true;
      }
      else return false;

  }


}
