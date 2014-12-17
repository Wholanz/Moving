package com.example.Moving;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class Opening extends Activity {
    /**
     * Called when the activity is first created.
     */
    //private ImageView imageView;
    private SoundPool soundPool;
    private int clickStart;
    private final static String LOG_TAG="StartApp";
    MediaPlayer mp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.opening, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
        //Play the BGM when first activity starts

        //imageView=(ImageView)findViewById(R.id.imageView);
        //imageView.setImageResource(R.drawable.maki);

        mp=MediaPlayer.create(this,R.raw.moving_bgm);
        mp.start();

        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        clickStart=soundPool.load(this,R.raw.click,1);

        //Edit the animation when the game starts
        final AlphaAnimation pictureIn = new AlphaAnimation(0,1.0f);
        final AlphaAnimation pictureOut = new AlphaAnimation(1.0f,0);
        pictureIn.setDuration(2000);
        pictureOut.setDuration(2000);
        view.startAnimation(pictureIn);

        pictureIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        soundPool.play(clickStart, 1.0F, 1.0F, 0, 0, 1.0F);
                        view.startAnimation(pictureOut);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });

        pictureOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                mp.stop();
                //redirectTo();
                Log.d(LOG_TAG,"Game:Direct to the next activity");
                Intent intent = new Intent(Opening.this, Main.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
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
