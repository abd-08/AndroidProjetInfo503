package com.example.androidprojet.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidprojet.R;
import com.example.androidprojet.models.Categorie;
import com.example.androidprojet.models.Personne;
import com.example.androidprojet.utilities.MyJSONFile;
import com.example.androidprojet.utilities.RandomChoice;

import org.json.JSONException;

import java.io.IOException;

public class LanceurActivity extends Activity {
    Activity activity=this;
    TextView affichereste;
    ImageView profil;
    TextView nom;
    TextView prenom;
    TextView groupe;
    Button fermer;
    Button relancer;
    Button continuer;
    LinearLayout layout;
    LinearLayout layout_reste;

    int parent_position;
    int position;
    Categorie categorie;
    Personne personne;
    MyJSONFile myJSONFile;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanceur);


        //on configure les element du layout
        fermer = findViewById(R.id.fermer_lanceur);
        affichereste = findViewById(R.id.reste_lanceur);
        profil = findViewById(R.id.image_lanceur);
        nom = findViewById(R.id.nom_lanceur);
        prenom = findViewById(R.id.prenom_lanceur);
        groupe = findViewById(R.id.groupe_lanceur);
        relancer = findViewById(R.id.relancer_lanceur);
        continuer = findViewById(R.id.continuer_lanceur);
        layout = findViewById(R.id.layout_lanceur);
        layout_reste = findViewById(R.id.layout_lanceur);



        myJSONFile = MyJSONFile.get(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        parent_position=extras.getInt("parent_position");
        position=extras.getInt("position");
        try {
            categorie = myJSONFile.getCategorie(parent_position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        categorie.setListe_restant(categorie.getListe());
        Sauvegarde();
        personne = categorie.getPersonne(1);
        select_Personne(personne);

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
                personne= RandomChoice.run(categorie.getListe_restant());
                select_Personne(personne);
            }
        });


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("parent_position", parent_position);
                startActivity(intent);
            }
        });


        affichereste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChoiceActivity.class);
                intent.putExtra("mode", "reste");
                intent.putExtra("parent_position", parent_position);
                startActivity(intent);
            }
        });



    }

    public void config(){
        this.fermer.findViewById(R.id.fermer_lanceur);
        this.affichereste.findViewById(R.id.reste_lanceur);
        this.profil.findViewById(R.id.image_lanceur);
        this.nom.findViewById(R.id.nom_lanceur);
        this.prenom.findViewById(R.id.prenom_lanceur);
        this.groupe.findViewById(R.id.groupe_lanceur);
        this.relancer.findViewById(R.id.relancer_lanceur);
        this.continuer.findViewById(R.id.continuer_lanceur);
    }

    public void select_Personne(Personne personne){

        if (personne!=null) {
            //this.profil.setImageURI(personne.getImageUri());
            this.nom.setText(personne.getNom());
           // this.prenom.setText(personne.getPrenom());
            this.groupe.setText(String.valueOf(personne.getGroupe()));
        }
    }



    public void relancer() {

        categorie.AjouterRestant(personne);
        personne = RandomChoice.run(categorie.getListe_restant());
        select_Personne(personne);
        layout.setBackgroundColor(Color.rgb(RandomChoice.rand(0, 255), RandomChoice.rand(0, 255), RandomChoice.rand(0, 255)));
    }


    public void Sauvegarde(){
        myJSONFile.setMyCategoriedatas( parent_position,categorie);
        try {
            myJSONFile.saveData(myJSONFile.getMydatas());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
