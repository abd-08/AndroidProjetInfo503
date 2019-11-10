package com.example.androidprojet.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidprojet.R;

public class PopupLancementRapide extends Dialog {

    private Button valider;
    private Button annuler;
    private EditText nombre_candidat;
    private TextView titre_popup;
    private TextView text_nombre_candidat;


    public PopupLancementRapide(Activity activity ){
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.popupajoutrapide);
        this.nombre_candidat=findViewById(R.id.popup_nombre);
        this.valider=findViewById(R.id.popup_btn_valider);
        this.annuler=findViewById(R.id.popup_btn_annuler);
    }

    public Button getValider(){
        return valider;
    }

    public Button getAnnuler(){
        return annuler;
    }


    public int getNombreCandidat() {
        return Integer.parseInt(  nombre_candidat.getText().toString()  );
    }

    public void mode (String s){
        if (s == "rajout"){
            titre_popup.setVisibility(View.INVISIBLE);
            text_nombre_candidat.setText("Nombre de personne à rajouter");
        }

    }

    public void build(){
        show();
    }
}


