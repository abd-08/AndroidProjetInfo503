package com.example.androidprojet.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidprojet.R;
import com.example.androidprojet.models.Categorie;
import com.example.androidprojet.models.Personne;
import com.example.androidprojet.utilities.MyJSONFile;
import com.example.androidprojet.utilities.PopupLancementRapide;
import com.example.androidprojet.utilities.RandomChoice;

import org.json.JSONException;

import java.io.IOException;

public class LanceurRapide extends Activity {
    Activity activity=this;
    TextView affichereste;
    TextView nom;
    Button fermer;
    Button relancer;
    Button continuer;
    Button rajouter ;
    Switch aleatoire;
    boolean mode_aleatoire;

    Categorie categorie;
    Personne personne;
    int nombre;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lanceurrapide);

        //on configure les element du layout
        config();


        Bundle extras = getIntent().getExtras();
        nombre=extras.getInt("nombre_candidat");

        categorie=new Categorie("rapide");
        categorie.initCategorieRapide(nombre);
        nom.setText("0");
        affichereste.setText( categorie.getListe().size() +"");


        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        relancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relancer();
            }
        });

        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               lancer();
            }
        });

        rajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] n = new int[1];
                final PopupLancementRapide customPopup= new PopupLancementRapide(LanceurRapide.this);
                customPopup.getValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        n[0] = customPopup.getNombreCandidat();
                        customPopup.dismiss();
                    }
                });

                customPopup.getAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customPopup.dismiss();
                    }
                });
                customPopup.build();

                rajouter(6);
            }
        });

        aleatoire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mode_aleatoire= true;
                    Toast.makeText(getApplicationContext(),"Mode aleatoire activé"+categorie.getListe_restant().size(),Toast.LENGTH_SHORT).show();
                }
                else {
                    mode_aleatoire =false;
                    Toast.makeText(getApplicationContext(),"Mode aleatoire désactivé",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void config(){
        fermer=findViewById(R.id.fermer_rapide);
        affichereste=findViewById(R.id.reste_rapide);
        nom=findViewById(R.id.nom_rapide);
        relancer=findViewById(R.id.relancer_rapide);
        continuer=findViewById(R.id.continuer_rapide);
        aleatoire=findViewById(R.id.switch1);
        rajouter=findViewById(R.id.rajouter);
    }


    public void select_rapide() {
        this.nom.setText(personne.getNom());
        affichereste.setText( categorie.getListe().size() +"");
    }

    public static int rand(int min, int max){
        return (int ) (min + Math.random()*(max-min));
    }

    public void lancer()  {
        int aleat;
        if (mode_aleatoire ){
            aleat= rand(0,categorie.getListe().size());

        }
        else{
            aleat = 0;
        }
        personne= categorie.getPersonne(aleat);
        categorie.Remove(aleat);
        select_rapide();

        if (categorie.getListe().size()==1){
            Toast.makeText(this,"Il reste qu'une seule personne à passer ", Toast.LENGTH_SHORT).show();
        }
        else if (categorie.getListe().size()==0){
            Toast.makeText(this," SESSION TERMINEE ", Toast.LENGTH_LONG).show();
            this.finish();
        }

    }


    public void relancer()  {
        if (mode_aleatoire) categorie.getListe().add(0,personne);
        else categorie.Ajouter(personne);
        lancer();
        select_rapide();
    }


    public void rajouter(int n){
        int nombre =0;
        int compare;

       /* for(int i=0;i<categorie.getListe().size();i++){
            compare= Integer.getInteger(categorie.getPersonne(i).getNom());
            if (nombre <= compare){
                nombre = compare;
            }
        }*/

        for (int j=0;j<n;j++){
            categorie.Ajouter(new Personne(String.valueOf(nombre+j)));
        }

    }



}
