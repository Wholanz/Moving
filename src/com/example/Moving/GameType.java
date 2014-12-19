package com.example.Moving;

import android.app.Activity;
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
public class GameType extends Activity {
    private final static String LOG_TAG="Game Type";

    private final static String FINGER="Finger";
    private final static String GRAVITY="Gravity";

    private static boolean isGravity=true;

    private int onClick;
    private SoundPool soundPool;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_type);

        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        onClick=soundPool.load(this,R.raw.normalclick,1);

        initList();
        final ItemAdapter adapter=new ItemAdapter(GameType.this,R.layout.main_list_item, list);
        final ListView listView=(ListView)findViewById(R.id.main_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(onClick,1.0F, 1.0F, 0, 0, 1.0F);
                MainListItem item=list.get(position);
                if(item.getName()==FINGER){
                    Log.d(LOG_TAG, "Finger mod");
                    isGravity=false;
                    list.get(position).changeItemTo(R.drawable.tick);
                    list.get(--position).changeItemTo(0);
                }
                if(item.getName()==GRAVITY){
                    Log.d(LOG_TAG, "Gravity mod");
                    isGravity=true;
                    list.get(position).changeItemTo(R.drawable.tick);
                    list.get(++position).changeItemTo(0);
                }

                ItemAdapter tmp=new ItemAdapter(GameType.this,R.layout.main_list_item, list);
                listView.setAdapter(tmp);
            }
        });
    }

    private List<MainListItem> list=new ArrayList<MainListItem>();

    @Override
    public void onBackPressed(){
        Log.d(LOG_TAG, "onBackPressed");
        soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
        Intent intent=new Intent(GameType.this,MainList.class);
        startActivity(intent);

        final ItemAdapter adapter=new ItemAdapter(GameType.this,R.layout.main_list_item, list);
        final ListView listView=(ListView)findViewById(R.id.main_list);
        listView.setAdapter(adapter);

        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        finish();
    }

    private void initList(){
        MainListItem finger=new MainListItem(FINGER,R.drawable.finger,getTick(FINGER));
        MainListItem gravity=new MainListItem(GRAVITY,R.drawable.gravity,getTick(GRAVITY));
        list.add(gravity);
        list.add(finger);
    }

    public int getTick(String name){
        if(name==FINGER && isGravity==false)
            return R.drawable.tick;
        if(name==GRAVITY && isGravity==true)
            return R.drawable.tick;
        return 0;
    }
}