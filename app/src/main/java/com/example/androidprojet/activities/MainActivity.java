package com.example.androidprojet.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidprojet.R;
import com.example.androidprojet.adapters.MainAdapter;
import com.example.androidprojet.models.Categorie;
import com.example.androidprojet.utilities.MyJSONFile;
import com.example.androidprojet.utilities.PopupLancementRapide;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private EditText _editText;
    private ListView _listView;
     private Button _ajouter;
     private Button _lanceur_rapide;
    public static MyJSONFile myJSONFile;
    private ArrayList<Categorie> _listCategory;
    private Context _context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _editText = findViewById(R.id.mainEditText);
        _listView = findViewById(R.id.mainListView);
        _ajouter = findViewById(R.id.id_main_btn_ajouter);
        _lanceur_rapide = findViewById(R.id.lanceur_rapide);
        _context = this;

        myJSONFile = MyJSONFile.get(getApplicationContext());
        try {
            _listCategory = myJSONFile.getCategories();
            _listView.setAdapter(new MainAdapter(this, _listCategory, myJSONFile, _listView));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*
        // Help
        TextView author = new EditText(this);
        author.setText("Créer par MOURCHIDI Abdallah 2018");
        author.setTextSize(12f);
        author.setEnabled(false);
        new AlertDialog.Builder(this)
                .setTitle("Comment ouvrir une catégorie?")
                .setMessage("Cliquez 2 fois de suite sur une catégorie pour l'ouvrir")
                .setView(author)
                .setPositiveButton("Merci MOURCHIDI Abdallah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(_context, "Pas de quoi!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();


*/
        this._ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_main_add_clicked();
            }
        });

        this._lanceur_rapide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanceur_rapide();
            }
        });

    }

    public void btn_main_add_clicked(){
        String name = String.valueOf(_editText.getText());
        Categorie categorie = new Categorie(name);
        if (this._listCategory==null)this._listCategory=new ArrayList<Categorie>();
        this._listCategory.add(categorie);
        Sauvegarde();
        _listView.setAdapter(new MainAdapter(this, this._listCategory, myJSONFile, _listView));

    }


    public void lanceur_rapide(){

        //popup customiser en alert pour lancement rapide
        final PopupLancementRapide customPopup= new PopupLancementRapide(MainActivity.this);

        customPopup.getValider().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int nombre = customPopup.getNombreCandidat();
                Intent intent = new Intent(getApplicationContext(), LanceurRapide.class);
                intent.putExtra("nombre_candidat", nombre);
                getApplicationContext().startActivity(intent);
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





    }

    public void Sauvegarde()   {
        //sauvegarde des donnée
        try {
            myJSONFile.setMydatas(myJSONFile.getListeCategorie(_listCategory));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            myJSONFile.saveData(myJSONFile.getMydatas());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
