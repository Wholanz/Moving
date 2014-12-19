package com.example.Moving;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by tiny on 12/19/14.
 */
public class ChooseStage extends Activity implements View.OnClickListener{
    private final static String LOG_TAG="Choose Stage";
    private LevelDatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private int onClick;
    private SoundPool soundPool;
    private int difficulty = 0 ;
    private Cursor cursor ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_stage);

        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        onClick=soundPool.load(this,R.raw.normalclick,1);

        databaseHelper = new LevelDatabaseHelper(this,"LevelData",1);
        db = databaseHelper.getReadableDatabase();
        Intent intent = getIntent();
        difficulty = intent.getIntExtra("difficulty", 0);
        Button[] levelButton = new Button[6];

        levelButton[0] = (Button)findViewById(R.id.button1);
        levelButton[1] = (Button)findViewById(R.id.button2);
        levelButton[2] = (Button)findViewById(R.id.button3);
        levelButton[3] = (Button)findViewById(R.id.button4);
        levelButton[4] = (Button)findViewById(R.id.button5);
        levelButton[5] = (Button)findViewById(R.id.button6);

        int[] btnPicutre=new int[6];
        btnPicutre[0]=R.drawable.btn1;
        btnPicutre[1]=R.drawable.btn2;
        btnPicutre[2]=R.drawable.btn2;  //TODO:change the button picture to 3
        btnPicutre[3]=R.drawable.btn4;
        btnPicutre[4]=R.drawable.btn5;
        btnPicutre[5]=R.drawable.btn6;

        int[] level=new int[6];
        level[0]=R.id.button1;
        level[1]=R.id.button2;
        level[2]=R.id.button3;
        level[3]=R.id.button4;
        level[4]=R.id.button5;
        level[5]=R.id.button6;

        for(int i=0; i<6; i++){
            if(!isLock(levelButton[i])){
                Log.d(LOG_TAG, "levelButton" + (i + 1) + " can start!");
                findViewById(level[i]).setBackgroundResource(btnPicutre[i]);
            }
            else{
                findViewById(level[i]).setBackgroundResource(R.drawable.locked);
            }

            levelButton[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(!isLock(v)){
            Log.v(LOG_TAG,"Start!");
            switch (v.getId()){
                case R.id.button1:
                    Intent intent=new Intent(ChooseStage.this,Stage1.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.roomin,R.anim.roomout);
                    break;
                case R.id.button2:break;
                case R.id.button3:break;
                case R.id.button4:break;
                case R.id.button5:break;
                case R.id.button6:break;
                default:break;
            }
        }else {
            Toast.makeText(this.getApplicationContext(), "You have not unlock the stage...", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isLock(View v){
        final String LockOrNot = "select lock from levellock where "
                + "level = ? and "
                + "difficulty = ?";
        switch(v.getId()){
            case R.id.button1:
                cursor = db.rawQuery(LockOrNot, new String[]{"0",Integer.toString(difficulty)});
                break;
            case R.id.button2:
                cursor = db.rawQuery(LockOrNot, new String[]{"1",Integer.toString(difficulty)});
                break;
            case R.id.button3:
                cursor = db.rawQuery(LockOrNot, new String[]{"2",Integer.toString(difficulty)});
                break;
            case R.id.button4:
                cursor = db.rawQuery(LockOrNot, new String[]{"3",Integer.toString(difficulty)});
                break;
            case R.id.button5:
                cursor = db.rawQuery(LockOrNot, new String[]{"4",Integer.toString(difficulty)});
                break;
            case R.id.button6:
                cursor = db.rawQuery(LockOrNot, new String[]{"5",Integer.toString(difficulty)});
                break;
            default:
                break;
        }
        if(cursor.moveToNext()){
            if(cursor.getInt(0) == 0){
                cursor.close();
                return false;}
        }

        cursor.close();
        return true;
    }

    @Override
    public void onBackPressed(){
        Log.d(LOG_TAG, "onBackPressed");
        soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
        Intent intent=new Intent(ChooseStage.this,MainList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        finish();
    }

}