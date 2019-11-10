package com.example.androidprojet.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidprojet.R;
import com.example.androidprojet.activities.ChoiceActivity;
import com.example.androidprojet.helpers.ViewHolderCategory;
import com.example.androidprojet.models.Categorie;
import com.example.androidprojet.utilities.MyJSONFile;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class MainAdapter extends ArrayAdapter<Categorie> {

    private ArrayList<Categorie> _myList;
    private MyJSONFile _myJsonFile;
    private ListView _listView;

    /**
     * Constructor
     *
     * @param context     The current context.
     * @param objects     The objects to represent in the ListView.
     * @param _myJSONFile
     */
    public MainAdapter(Context context, ArrayList<Categorie> objects, MyJSONFile _myJSONFile, ListView listView) {
        super(context, 0);
        _myList = objects;
        _myJsonFile = _myJSONFile;
        _listView = listView;
    }

    public int getCount() {
        return _myList.size();
    }


    public Categorie getItemC(int position) {
        return _myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderCategory holder = new ViewHolderCategory();
        final Categorie categoryName = getItemC(position);
        // Bind item with item view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_main, parent, false);
            holder.editText = convertView.findViewById(R.id._main_item_textview);
            holder.deleteButton = convertView.findViewById(R.id._main_item_button);
            holder.editButton = convertView.findViewById(R.id._main_item_edit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderCategory) convertView.getTag();
        }

        holder.editText.setText(categoryName.getNom());
        final String finalCategoryName = categoryName.getNom();
        final Context fContext = this.getContext();
        final MainAdapter fmainAdapter = this;

        // Si le boutton supprimer a été cliqué
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    _myJsonFile.remove(position);
                    _myList.remove(position);
                    _myJsonFile.saveData();
                    _listView.setAdapter(fmainAdapter);
                    Toast.makeText(fContext, finalCategoryName + " bien été supprimé avec succès", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Si le boutton ouvrir a été cliqué
        holder.editText.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChoiceActivity.class);
                intent.putExtra("parent_position", position);
                getContext().startActivity(intent);

            }
        });

        // Si le boutton update cliqué
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(fContext);
                editText.setText(finalCategoryName);
                new AlertDialog.Builder(fContext)
                        .setTitle("Modifier")
                        .setMessage("Entrez la nouvelle valeur")
                        .setView(editText)
                        .setPositiveButton("modifier", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String newName = editText.getText().toString();
                                if (newName.isEmpty()) {
                                    dialog.dismiss();
                                    Toast.makeText(fContext, "La valeur ne peut pas être vide!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    newName = editText.getText().toString();
                                    Categorie categorie =_myList.get(position);
                                    categorie.setNom(newName);
                                    _myJsonFile.setMyCategoriedatas(position,categorie);
                                    try {
                                        _myJsonFile.saveData();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                    Toast.makeText(fContext, "Votre champs a bien été mise à jour!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        })
                        .setNegativeButton("fermer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        return convertView;
    }
}
