package com.example.ahmad.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.View;


public class MainActivity4 extends ActionBarActivity {
    EditText getSomsing;
    View myExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity4);
        getSomsing = (EditText) findViewById(R.id.justTxt);
        myExp = findViewById(R.id.ViewTst);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity4, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void programWay(View view) {
        try {
            int pixels = Integer.parseInt(getSomsing.getText() + "");
            myExp.setMinimumHeight(pixels);
            myExp.setMinimumWidth(pixels);
        } catch (NumberFormatException e) {

        }
    }

    public void xmlWay(View view) {
        try {
            int pixels = Integer.parseInt(getSomsing.getText() + "");
            myExp.getLayoutParams().height = pixels;
            myExp.getLayoutParams().width = pixels;
            myExp.requestLayout();
        } catch (NumberFormatException e) {

        }
    }
}
