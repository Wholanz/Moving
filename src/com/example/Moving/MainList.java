package com.example.Moving;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 12/18/14.
 */
public class MainList extends Activity {
    private final static String LOG_TAG="MainList";
    private int onClick;
    private int onAlert;
    private SoundPool soundPool;
    private List<MainListItem>mainList=new ArrayList<MainListItem>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_list);
        initList();
        ItemAdapter adapter=new ItemAdapter(MainList.this,R.layout.main_list_item,mainList);
        ListView listView=(ListView)findViewById(R.id.main_list);
        listView.setAdapter(adapter);
        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        onClick=soundPool.load(this,R.raw.normalclick,1);
        onAlert=soundPool.load(this,R.raw.alert,1);
    }
    private void initList(){
        MainListItem newGame=new MainListItem("New Game",R.drawable.newgame,R.drawable.arrow);
        mainList.add(newGame);
        MainListItem chooseStage=new MainListItem("Choose Stage",R.drawable.stage,R.drawable.arrow);
        mainList.add(chooseStage);
        MainListItem difficulty=new MainListItem("Difficulty",R.drawable.difficulty,R.drawable.arrow);
        mainList.add(difficulty);
        MainListItem gameType=new MainListItem("Game Type",R.drawable.gametype,R.drawable.arrow);
        mainList.add(gameType);
        MainListItem settings=new MainListItem("Settings",R.drawable.settings,R.drawable.arrow);
        mainList.add(settings);
        MainListItem aboutUs=new MainListItem("About Us",R.drawable.about,R.drawable.arrow);
        mainList.add(aboutUs);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mainmenu,menu);
        return true;
    }
    public void onBackPressed(){
        Log.d(LOG_TAG, "Game:onBackPressed");
        soundPool.play(onAlert, 1.0F, 1.0F, 0, 0, 1.0F);
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainList.this);
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
    }

}