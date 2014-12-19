package com.example.Moving;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * Created by tiny on 12/19/14.
 */
public class GameType extends Activity {
    private final static String LOG_TAG="Game Type";
    private int onClick;
    private SoundPool soundPool;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_type);

        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        onClick=soundPool.load(this,R.raw.normalclick,1);

    }

    @Override
    public void onBackPressed(){
        Log.d(LOG_TAG, "onBackPressed");
        soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
        Intent intent=new Intent(GameType.this,MainList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        finish();
    }
}