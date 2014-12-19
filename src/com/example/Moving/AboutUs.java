package com.example.Moving;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 12/19/14.
 */
public class AboutUs extends Activity {
    private final static String LOG_TAG="About us";

    private final static String TINY="Tiny";
    private final static String HLZ="HLZ";
    private final static String ZJY="ZJY";

    private int onClick;
    private SoundPool soundPool;

    private List<ProgrammerItem>programmerList=new ArrayList<ProgrammerItem>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_us);

        initList();

        ProgrammersAdapter adapter=new ProgrammersAdapter(AboutUs.this,R.layout.about_us_item, programmerList);
        final ListView listView=(ListView)findViewById(R.id.main_list);
        listView.setAdapter(adapter);

        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        onClick=soundPool.load(this,R.raw.normalclick,1);
    }

    @Override
    public void onBackPressed(){
        Log.d(LOG_TAG, "onBackPressed");
        soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
        Intent intent=new Intent(AboutUs.this,MainList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        finish();
    }

    private void initList(){
        ProgrammerItem tiny=new ProgrammerItem(TINY,R.drawable.p2);
        ProgrammerItem hlz=new ProgrammerItem(HLZ,R.drawable.p1);
        ProgrammerItem zjy=new ProgrammerItem(ZJY,R.drawable.p3);
        programmerList.add(tiny);
        programmerList.add(hlz);
        programmerList.add(zjy);
    }
}