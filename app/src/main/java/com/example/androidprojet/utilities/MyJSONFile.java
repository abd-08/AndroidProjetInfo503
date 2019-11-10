package com.example.androidprojet.utilities;

import android.content.Context;
import android.util.Log;

import com.example.androidprojet.R;
import com.example.androidprojet.models.Categorie;
import com.example.androidprojet.models.Personne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyJSONFile {
    public static final String DB_NAME = "dbjson.json";
    private static MyJSONFile FILE = null;
    public static MyJSONFile get(Context context) {
        if (FILE == null) {
            FILE = new MyJSONFile(context);
        }
        return FILE;
    }

    private Context _context;
    private JSONArray _mydatas;

    public MyJSONFile(Context context) {
        this._context = context;
        this.openData();
    }

    /**
     * Ouvre le fichier JSON
     */
    public JSONArray openData() {
        String bddJSon = null;
        try { //Si le fichier bddjson existe on l'ouvre
            FileInputStream fileInputStream = _context.openFileInput(DB_NAME);
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            bddJSon = new String(buffer, "UTF-8");
            _mydatas = new JSONArray(bddJSon);
            return new JSONArray(bddJSon);
        } catch (FileNotFoundException e) { //Sinon on l'a crée
            //e.printStackTrace();
            Log.e("MAIN " + DB_NAME, "n'éxiste pas ou " + e.getMessage());
            InputStream inputStream = _context.getResources().openRawResource(R.raw.dbjson);
            int size = 0;
            try {
                size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                bddJSon = new String(buffer, "UTF-8");
                _mydatas = new JSONArray(bddJSon);
                return new JSONArray(bddJSon);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveData(JSONArray datas) throws IOException {
        Log.i("saveJSON", datas.toString());
        FileOutputStream fileOut = null;
        fileOut = _context.openFileOutput(DB_NAME, MODE_PRIVATE);
        OutputStreamWriter os = new OutputStreamWriter(fileOut);
        BufferedWriter writer = new BufferedWriter(os);
        writer.write(datas.toString());
        writer.flush();
        writer.close();
    }

    public void saveData() throws IOException {
        saveData(_mydatas);
    }



    /**
     * Remplace un objet dans le objet jsonArray
     *
     * @param jsonArray Le JSONArray qu'on souhaite modifier
     * @param position  La position de l'élément qu'on souhaite remplacer
     * @param object    Le nouveau objet
     * @throws JSONException
     */
    public void update(JSONArray jsonArray, int position, JSONObject object) throws JSONException {
        jsonArray.put(position, object);
    }

    /**
     * Supprime une catégorie
     *
     * @param position La position de la catégorie
     * @throws JSONException
     */
    public void remove(int position) throws JSONException {
        _mydatas = remove(_mydatas, position);
    }

    /**
     * Supprime un choix
     *
     * @param parentPosition La position de sa catégorie
     * @param position       Le position du choix
     */
    public void remove(int parentPosition, int position) throws JSONException {
        _mydatas.getJSONObject(parentPosition).put("data", remove(_mydatas.getJSONObject(parentPosition).getJSONArray("liste"), position));
    }

    /**
     * Supprime un objet dans le tableau objet donné en paramètre
     *
     * @param jsonArray Le tableau objet qu'on souhaite modifier
     * @param position  La position de l'élément a effacé
     * @return JSONArray
     * @throws JSONException
     */
    public JSONArray remove(JSONArray jsonArray, int position) throws JSONException {
        JSONArray jsonArrayResult = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (position != i) {
                JSONObject jo = jsonArray.getJSONObject(i);
                jsonArrayResult.put(jo);
            }
        }
        return jsonArrayResult;
    }

    public JSONArray getMydatas() {
        return _mydatas;
    }

    public void setMydatas(JSONArray _mydatas) {
        this._mydatas = _mydatas;
    }


    public void setMyCategoriedatas( int position, Categorie categorie) {
        JSONObject jsonObject = transforme_categorie(categorie);
        try {
            this._mydatas.put(position,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setMyPersonnedatas( int parent_position, int position, Personne personne) throws JSONException{
        JSONObject jsonObject = transforme_personne(personne);
        Categorie categorie = getCategorie(parent_position);
        categorie.getListe().set(position,personne);
        this._mydatas.put(parent_position,transforme_categorie(categorie));

    }


    //---pour une personne
    public JSONObject transforme_personne(Personne p)  {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("nom", p.getNom());
            jsonObject.put("prenom", p.getPrenom());
            jsonObject.put("groupe", p.getGroupe());

            //jsonObject.put("etat", p.getEtat().toString());
            jsonObject.put("photo", p.getPath());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    //focntion qui transforme un json objet en personne
    public Personne getPersonne(JSONObject jsonObject)  {
        Personne personne = null;
        try {
            personne = new Personne(jsonObject.getString("nom"));
            personne.setPrenom(jsonObject.getString("prenom"));
            personne.setGroupe(jsonObject.getString("groupe"));
            personne.setPath( (jsonObject.getString("photo")) );

            // personne.setEtat(Etat.getEtat(jsonObject.getString("etat")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return personne;
    }

    public Personne getPersonne(int parent,int position){
        Categorie categorie = null;
        try {
            categorie = getCategorie(_mydatas.getJSONObject(parent));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Personne personne = categorie.getPersonne(position);
        return personne;
    }



    //pour une catégorie
    public JSONObject transforme_categorie(Categorie categorie) {
        JSONObject jsonObject = new JSONObject();
        JSONArray Liste = new JSONArray();

       //transformation de la iste d eleve en JSON array
        for(int i=0;i<categorie.getListe().size();i++){
            JSONObject jo = transforme_personne(categorie.getPersonne(i));
            Liste.put(jo);
        }

        //finalisation de l objet categorie
        try {
            jsonObject.put("nom",categorie.getNom());
            jsonObject.put("liste",Liste);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    //convertir un objet en categorie
    public Categorie getCategorie(JSONObject jsonObject) throws JSONException {
        Categorie  categorie = new Categorie(jsonObject.getString("nom"));
        JSONArray    jsonArray = jsonObject.getJSONArray("liste");
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jo=jsonArray.getJSONObject(i);
            categorie.Ajouter(getPersonne(jo));
        }
        return categorie;
    }



    //recuperer la catégorie a la position i
    public Categorie getCategorie(int position) throws JSONException {
        JSONObject jo = null;
        jo = _mydatas.getJSONObject(position);
        return getCategorie(jo);
    }


    //pour avoir toute la liste de categorie
    public ArrayList<Categorie> getItems(JSONArray jsonArray) throws JSONException {
        ArrayList<Categorie> myResultList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentJsonObject = jsonArray.getJSONObject(i);
            myResultList.add(getCategorie(currentJsonObject));
        }
        return myResultList;
    }

    public ArrayList<Categorie> getCategories() throws JSONException {
        return getItems(_mydatas);
    }

    public JSONArray getListeCategorie(ArrayList<Categorie> list) throws JSONException{
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for(int i = 0;i<list.size();i++){
            jsonObject=transforme_categorie(list.get(i));
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }



}
