package com.example.Moving;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.io.IOException;

public class OnGame extends Activity {
    /**
     * Called when the activity is first created.
     */
    final static String LOG_TAG="StartApp";
    MediaPlayer mp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.main, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);

        //Play the BGM when first activity starts
        mp=MediaPlayer.create(this,R.raw.moving_bgm);
        mp.start();

        //Edit the animation when the game starts
        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation arg0) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redirectTo();
                        mp.stop();
                    }
                });
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}
        });
    }
    private void redirectTo(){
        Log.d(LOG_TAG,"Game:Direct to the next activity");
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Log.d(LOG_TAG,"Game:onBackPressed");
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
