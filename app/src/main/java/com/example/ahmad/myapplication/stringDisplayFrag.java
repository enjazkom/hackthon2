package com.example.ahmad.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ahmad on 28 ديس، 14 م.
 */
public class stringDisplayFrag extends Fragment {
    String stringToDisplay;

    public void setStringToDisplay(String stringToDisplay) {
        this.stringToDisplay = stringToDisplay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vv = inflater.inflate(R.layout.fragment_main_activity3, container, false);
        TextView txtx = (TextView) vv.findViewById(R.id.section_label);
        txtx.setText(stringToDisplay);
        return vv;
    }
}
