package com.example.Moving;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
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
public class Settings extends Activity {
    private final static String LOG_TAG="Settings";

    private final static String CONTACT_US="Contact Us";
    private final static String VOLUME="Volume";
    private final static String CHANGE_MUSIC="Change Music";
    private final static String CLEAR_HISTORY="Clear History";
    private static int choosed=0;

    private int onClick;
    private SoundPool soundPool;

    private List<MainListItem> list=new ArrayList<MainListItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);

        soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC,5);
        onClick=soundPool.load(this,R.raw.normalclick,1);

        initList();
        final ItemAdapter adapter=new ItemAdapter(Settings.this,R.layout.main_list_item, list);
        final ListView listView=(ListView)findViewById(R.id.main_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundPool.play(onClick,1.0F, 1.0F, 0, 0, 1.0F);
                MainListItem item=list.get(position);
                if(item.getName()==CONTACT_US){
                    Log.d(LOG_TAG, "Contact Us");
                    choosed=1;
                    list.get(position).changeItemTo(R.drawable.dot);
                    list.get(++position).changeItemTo(0);
                    list.get(++position).changeItemTo(0);
                    list.get(++position).changeItemTo(0);

                    Intent data=new Intent(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:zjutiny@gmail.com"));
                    data.putExtra(Intent.EXTRA_SUBJECT, "GameFeedBack");
                    data.putExtra(Intent.EXTRA_TEXT, "Feedback:\t\n");
                    startActivity(data);
                }
                if(item.getName()==VOLUME){
                    Log.d(LOG_TAG, "Volume");
                    choosed=2;
                    list.get(position).changeItemTo(R.drawable.dot);
                    list.get(position-1).changeItemTo(0);
                    list.get(++position).changeItemTo(0);
                    list.get(++position).changeItemTo(0);
                }
                if(item.getName()==CHANGE_MUSIC){
                    Log.d(LOG_TAG, "Change Music");
                    choosed=3;
                    list.get(position).changeItemTo(R.drawable.dot);
                    list.get(position+1).changeItemTo(0);
                    list.get(--position).changeItemTo(0);
                    list.get(--position).changeItemTo(0);
                }
                if(item.getName()==CLEAR_HISTORY){
                    Log.d(LOG_TAG, "Clear History");
                    choosed=4;
                    list.get(position).changeItemTo(R.drawable.dot);
                    list.get(--position).changeItemTo(0);
                    list.get(--position).changeItemTo(0);
                    list.get(--position).changeItemTo(0);
                }
                ItemAdapter tmp=new ItemAdapter(Settings.this,R.layout.main_list_item, list);
                listView.setAdapter(tmp);
            }
        });
    }

    private void initList(){
        MainListItem email=new MainListItem(CONTACT_US,R.drawable.email,getDot(CONTACT_US));
        MainListItem volume=new MainListItem(VOLUME,R.drawable.volume,getDot(VOLUME));
        MainListItem changeMusic=new MainListItem(CHANGE_MUSIC,R.drawable.change_music,getDot(CHANGE_MUSIC));
        MainListItem clearHistory=new MainListItem(CLEAR_HISTORY,R.drawable.clear,getDot(CLEAR_HISTORY));
        list.add(email);
        list.add(volume);
        list.add(changeMusic);
        list.add(clearHistory);
    }

    private int getDot(String name){
        if(name==CONTACT_US&&choosed==1)
            return R.drawable.dot;
        if(name==VOLUME&&choosed==2)
            return R.drawable.dot;
        if(name==CHANGE_MUSIC&&choosed==3)
            return R.drawable.dot;
        if(name==CLEAR_HISTORY&&choosed==4)
            return R.drawable.dot;
        return 0;
    }

    @Override
    public void onBackPressed(){
        Log.d(LOG_TAG, "onBackPressed");
        soundPool.play(onClick, 1.0F, 1.0F, 0, 0, 1.0F);
        Intent intent=new Intent(Settings.this,MainList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        finish();

    }
}