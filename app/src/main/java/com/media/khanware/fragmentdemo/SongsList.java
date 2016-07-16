package com.media.khanware.fragmentdemo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zia Khan on 11-05-2016.
 */
public class SongsList extends BaseAdapter {

    ArrayList<String> songs;
    Context c;
    ArrayList<String> albumInfoDisplay;
    ArrayList<String> durationOfTheSong;

    SongsList(Context c, ArrayList<String> listOfSongs, ArrayList<String> albumInfoDisplay, ArrayList<String> durationOfTheSong)
    {
        this.c=c;
        this.songs = listOfSongs;
        this.albumInfoDisplay = albumInfoDisplay;
        this.durationOfTheSong=durationOfTheSong;

    }


    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return songs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        ViewHolder vh;
        LayoutInflater inflater = (LayoutInflater)
                c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(view ==null)
        {

           view = inflater.inflate(R.layout.green,viewGroup,false);

           vh  = new ViewHolder(view);
            view.setTag(vh);

            vh.tv.setText(songs.get(i));
            vh.albumInfo.setText(albumInfoDisplay.get(i));
            vh.durationInfo.setText(durationOfTheSong.get(i));

        }
        else{

            vh = (ViewHolder) view.getTag();
            vh.tv.setText(songs.get(i));
            vh.albumInfo.setText(albumInfoDisplay.get(i));
            vh.durationInfo.setText(durationOfTheSong.get(i));

        }

        return view;
    }
}
