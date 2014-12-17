package com.example.Moving;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

/**
 * Created by tiny on 12/16/14.
 */
public class Main extends Activity {
    private SoundPool soundPool;
    private int onClick;
    private int onAlert;
    private final static String LOG_TAG="Main";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        onClick=soundPool.load(this,R.raw.normalclick,1);
        onAlert=soundPool.load(this,R.raw.alert,1);
    }
    public void onBackPressed(){
        soundPool.play(onAlert, 1.0F, 1.0F, 0, 0, 1.0F);
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
        soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        soundPool.play(onClick,1.0F, 1.0F, 0, 0, 1.0F);
        switch(item.getItemId()) {
            case R.id.mi1:
                // do sth
                break;
            case R.id.mi2:
                // do sth
                break;
            case R.id.mi3:
                // do sth
                break;
            case R.id.mi4:
                // do sth
                break;
        }
        return true;
    }
}