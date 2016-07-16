package com.media.khanware.fragmentdemo;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Zia Khan on 11-05-2016.
 */
public class ViewHolder {
    TextView tv;
    TextView albumInfo;
    TextView durationInfo;

    ViewHolder(View v)
    {
        tv = (TextView) v.findViewById(R.id.songName);
        albumInfo = (TextView) v.findViewById(R.id.albumInfo);
        durationInfo = (TextView) v.findViewById(R.id.timeInfo);

    }


}
