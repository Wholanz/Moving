package com.example.Moving;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tiny on 12/18/14.
 */
public class ItemAdapter extends ArrayAdapter<MainListItem>{
    private int resourceId;

    public ItemAdapter(Context context, int textViewResourceId,List<MainListItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        MainListItem listItem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView itemImage=(ImageView)view.findViewById(R.id.item_image);
        ImageView itemTo=(ImageView)view.findViewById(R.id.item_to);
        TextView itemName=(TextView)view.findViewById(R.id.item_name);
        itemImage.setImageResource(listItem.getItemImageId());
        itemTo.setImageResource(listItem.getItemToId());
        itemName.setText(listItem.getName());
        return view;
    }
}
