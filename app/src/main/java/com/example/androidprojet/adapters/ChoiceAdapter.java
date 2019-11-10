package com.example.androidprojet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.androidprojet.R;
import com.example.androidprojet.helpers.ViewHolderCategory;
import com.example.androidprojet.utilities.MyJSONFile;

import java.util.ArrayList;
import java.util.List;

public class ChoiceAdapter extends ArrayAdapter<String> {
    private MyJSONFile _myJSONFile;
    private ListView _listView;
    private ArrayList<String> _myList;
    private int _parentPosition;

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public ChoiceAdapter(Context context, List<String> objects, ListView listView, MyJSONFile myJSONFile, int parentPosition) {
        super(context, 0, objects);
        _listView = listView;
        _myJSONFile = myJSONFile;
        _myList = (ArrayList<String>) objects;
        _parentPosition = parentPosition;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderCategory holder = new ViewHolderCategory();
        final String choiceName = getItem(position);
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

        holder.editText.setText(choiceName);

        final ChoiceAdapter fchoiceAdapter = this;
        final Context fContext = this.getContext();
        final String fchoiceName = choiceName;


        return convertView;
    }

}
