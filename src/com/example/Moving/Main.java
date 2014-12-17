package com.example.Moving;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by tiny on 12/16/14.
 */
public class Main extends Activity {
    private final static String LOG_TAG="Main";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
    }
    public void onBackPressed(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(Main.this);
        dialog.setTitle("Warning");
        dialog.setMessage("Are you sure to leave the game?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();

        Log.d(LOG_TAG, "Game:onBackPressed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        return true;
    }
}