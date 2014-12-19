package com.example.Moving;

/**
 * Created by tiny on 12/19/14.
 */

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
public class ProgrammersAdapter extends ArrayAdapter<ProgrammerItem> {
    private int resourceId;

    public ProgrammersAdapter(Context context, int textViewResourceId, List<ProgrammerItem> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ProgrammerItem programmerItem=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null) {
            view= LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder=new ViewHolder();
            viewHolder.personImage =(ImageView)view.findViewById(R.id.item_image);
            viewHolder.name =(TextView)view.findViewById(R.id.item_name);
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.personImage.setImageResource(programmerItem.getPersonImage());
        viewHolder.name.setText(programmerItem.getName());
        return view;
    }
    class ViewHolder{
        ImageView personImage =null;
        TextView name =null;
    }
}
