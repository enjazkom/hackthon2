package com.example.ahmad.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

/**
 * Created by Ahmad on 23 ديس، 14 م.
 */
public class FileList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public FileList(Activity context,
                    String[] web, Integer[] imageId) {
        super(context, R.layout.single_list_view_cell, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.single_list_view_cell, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.fileNameTxt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.fileTypeIcon);
        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
