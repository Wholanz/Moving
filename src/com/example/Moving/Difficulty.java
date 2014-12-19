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
public class Difficulty extends Activity {
    private final static String LOG_TAG="Difficulty";

    private final static String EASY="Easy";
    private final static String Normal="Normal";
    private final static String HARD="Hard";

    private  int level=1;
    private int onClick;
    private SoundPool soundPool;

    private List<MainListItem>list=new ArrayList<MainListItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.difficulty);

        initList();

        final ItemAdapter adapter=new ItemAdapter(Difficulty.this,R.layout.main_list_item, list);
        final ListView listView=(ListView)findViewById(R.id.main_list);
        listView.setAdapter(adapter);

        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        onClick=soundPool.load(this,R.raw.normalclick,1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(onClick,1.0F, 1.0F, 0, 0, 1.0F);
                MainListItem item=list.get(position);
                if(item.getName()==EASY){
                    Log.d(LOG_TAG, "Easy mod");
                    level=1;
                    list.get(position).changeItemTo(R.drawable.tick);
                    list.get(++position).changeItemTo(0);
                    list.get(++position).changeItemTo(0);
                }
                if(item.getName()==Normal){
                    Log.d(LOG_TAG, "Normal mod");
                    level=2;
                    list.get(position).changeItemTo(R.drawable.tick);
                    list.get(position-1).changeItemTo(0);
                    list.get(position+1).changeItemTo(0);
                }
                if(item.getName()==HARD){
                    Log.d(LOG_TAG, "Hard mod");
                    level=3;
                    list.get(position).changeItemTo(R.drawable.tick);
                    list.get(--position).changeItemTo(0);
                    list.get(--position).changeItemTo(0);
                }
                ItemAdapter tmp=new ItemAdapter(Difficulty.this,R.layout.main_list_item, list);
                listView.setAdapter(tmp);
            }
        });
    }

    private void initList(){
        MainListItem hard=new MainListItem(HARD,R.drawable.hard,0);
        MainListItem normal=new MainListItem(Normal,R.drawable.normal,0);
        MainListItem easy=new MainListItem(EASY,R.drawable.easy,R.drawable.tick);
        list.add(easy);
        list.add(normal);
        list.add(hard);
    }

    @Override
    public void onBackPressed(){
        Log.d(LOG_TAG, "onBackPressed");
        soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
        MainList.difficulty=level;
        Intent intent=new Intent(Difficulty.this,MainList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        finish();
    }
}