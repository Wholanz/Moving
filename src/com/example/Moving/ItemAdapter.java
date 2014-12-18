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
        View view;
        ViewHolder viewHolder;
        if(convertView==null) {
            view= LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder=new ViewHolder();
            viewHolder.itemImage=(ImageView)view.findViewById(R.id.item_image);
            viewHolder.itemTo=(ImageView)view.findViewById(R.id.item_to);
            viewHolder.itemName=(TextView)view.findViewById(R.id.item_name);
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.itemImage.setImageResource(listItem.getItemImageId());
        viewHolder.itemTo.setImageResource(listItem.getItemToId());
        viewHolder.itemName.setText(listItem.getName());
        return view;
    }
    class ViewHolder{
        ImageView itemImage=null;
        ImageView itemTo=null;
        TextView itemName=null;
    }
}
