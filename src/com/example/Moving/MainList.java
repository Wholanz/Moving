package com.example.Moving;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 12/18/14.
 */
public class MainList extends Activity {
    private final static String LOG_TAG="MainList";

    private final static String NEW_GAME="New Game";
    private final static String CHOOSE_STAGE="Choose Stage";
    private final static String DIFFICULTY="Difficulty";
    private final static String GAME_TYPE="Game Type";
    private final static String SETTINGS="Settings";
    private final static String ABOUT_US="About us";

    private int onClick;
    private int onAlert;
    private Button voiceButton;
    private MediaPlayer mp;
    private SoundPool soundPool;
    private List<MainListItem>mainList=new ArrayList<MainListItem>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_list);
        initList();
        ItemAdapter adapter=new ItemAdapter(MainList.this,R.layout.main_list_item,mainList);
        final ListView listView=(ListView)findViewById(R.id.main_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
                MainListItem item = mainList.get(position);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainListItem item=mainList.get(position);
                if(item.getName()==NEW_GAME) {
                    Log.d(LOG_TAG, "GameList:Direct to the new game");
                    Intent intent = new Intent(MainList.this, GameActivity.class);
                    startActivity(intent);
                }
            }
        });

        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        onClick=soundPool.load(this,R.raw.normalclick,1);
        onAlert=soundPool.load(this,R.raw.alert,1);
        mp=MediaPlayer.create(this, R.raw.main_music);
        mp.setLooping(true);
        mp.start();

        voiceButton=(Button)findViewById(R.id.voice_btn);
        voiceButton.setOnClickListener(new View.OnClickListener() {
            boolean isVoiceOn=false;
            @Override
            public void onClick(View v) {
                isVoiceOn=!isVoiceOn;
                if(isVoiceOn){
                    v.setBackgroundResource(R.drawable.voice_off);
                    mp.pause();
                }else {
                    v.setBackgroundResource(R.drawable.voice_on);
                    mp.start();
                }
            }
        });
    }
    private void initList(){
        MainListItem newGame=new MainListItem(NEW_GAME,R.drawable.newgame,R.drawable.arrow);
        mainList.add(newGame);
        MainListItem chooseStage=new MainListItem(CHOOSE_STAGE,R.drawable.stage,R.drawable.arrow);
        mainList.add(chooseStage);
        MainListItem difficulty=new MainListItem(DIFFICULTY,R.drawable.difficulty,R.drawable.arrow);
        mainList.add(difficulty);
        MainListItem gameType=new MainListItem(GAME_TYPE,R.drawable.gametype,R.drawable.arrow);
        mainList.add(gameType);
        MainListItem settings=new MainListItem(SETTINGS,R.drawable.settings,R.drawable.arrow);
        mainList.add(settings);
        MainListItem aboutUs=new MainListItem(ABOUT_US,R.drawable.about,R.drawable.arrow);
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

    @Override
    public void onResume(){
        super.onResume();
        mp.start();
        Log.d(LOG_TAG,"Game:Resume");
    }

    @Override
    public void onPause(){
        super.onPause();
        mp.pause();
        Log.d(LOG_TAG,"Game:Pause");
    }

}