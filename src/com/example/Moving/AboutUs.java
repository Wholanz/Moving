package com.example.Moving;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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

    private static int choosed=1;

    private int onClick;
    private SoundPool soundPool;

    private List<MainListItem>list=new ArrayList<MainListItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_us);

        initList();
        final ItemAdapter adapter=new ItemAdapter(AboutUs.this,R.layout.main_list_item, list);
        final ListView listView=(ListView)findViewById(R.id.main_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(onClick,1.0F, 1.0F, 0, 0, 1.0F);
                MainListItem item=list.get(position);
                if(item.getName()==TINY){
                    Log.d(LOG_TAG, "Choose Tiny");
                    choosed=1;
                    list.get(position).changeItemTo(R.drawable.tick);
                    list.get(++position).changeItemTo(0);
                    list.get(++position).changeItemTo(0);
                    findViewById(R.id.imageView).setBackgroundResource(R.drawable.info1);
                }
                if(item.getName()==HLZ){
                    Log.d(LOG_TAG, "Choose Hlz");
                    choosed=2;
                    list.get(position).changeItemTo(R.drawable.tick);
                    list.get(position-1).changeItemTo(0);
                    list.get(position+1).changeItemTo(0);
                    findViewById(R.id.imageView).setBackgroundResource(R.drawable.info2);
                }
                if(item.getName()==ZJY){
                    Log.d(LOG_TAG, "Choose Zjy");
                    choosed=3;
                    list.get(position).changeItemTo(R.drawable.tick);
                    list.get(--position).changeItemTo(0);
                    list.get(--position).changeItemTo(0);
                    findViewById(R.id.imageView).setBackgroundResource(R.drawable.info3);
                }
                ItemAdapter tmp=new ItemAdapter(AboutUs.this,R.layout.main_list_item, list);
                listView.setAdapter(tmp);
            }
        });

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
        MainListItem tiny=new MainListItem(TINY,R.drawable.p2,getTick(TINY));
        MainListItem hlz=new MainListItem(HLZ,R.drawable.p1,getTick(HLZ));
        MainListItem zjy=new MainListItem(ZJY,R.drawable.p3,getTick(ZJY));
        list.add(tiny);
        list.add(hlz);
        list.add(zjy);
    }

    private int getTick(String name){
        if(name==TINY&&choosed==1)
            return R.drawable.tick;
        if(name==HLZ&&choosed==2)
            return R.drawable.tick;
        if(name==ZJY&&choosed==3)
            return R.drawable.tick;
        return 0;
    }
}