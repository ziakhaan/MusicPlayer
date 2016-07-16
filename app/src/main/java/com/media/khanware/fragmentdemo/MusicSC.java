package com.media.khanware.fragmentdemo;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.ArrayList;


public class MusicSC extends Fragment implements View.OnClickListener {

    //Music player buttons
    ImageButton nextButton;
    ImageButton playButton;
    ImageButton previousButton;
    Button stopbtn;


    String songPath;

    int songIndex;

    MainActivity mainActivity;

    //Bundle to receive Activity data
    Bundle bundle;

    //Receive the songs with their paths
    ArrayList<String> listOfSongs = new ArrayList<String>();

    ArrayList<String> filePathToFragment = new ArrayList<String>();

    public MusicSC() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragmentLayout = inflater.inflate(R.layout.red, container, false);

        nextButton = (ImageButton) fragmentLayout.findViewById(R.id.next);
        playButton = (ImageButton) fragmentLayout.findViewById(R.id.play);
        previousButton = (ImageButton) fragmentLayout.findViewById(R.id.previous);
        stopbtn = (Button)fragmentLayout.findViewById(R.id.stopbutton);

        nextButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        stopbtn.setOnClickListener(this);

        //Receive Activity data => Song path to play the media file
        bundle = this.getArguments();
        if (bundle != null) {

            songPath = bundle.getString("songFilePath");
            songIndex = bundle.getInt("indexOfSong");
            filePathToFragment = bundle.getStringArrayList("listOfSongPath");

        } else {


            Toast.makeText(getActivity().getApplicationContext(), "The bundle is null", Toast.LENGTH_LONG).show();
        }


        return fragmentLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);





    }


    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.next) {

            songIndex = songIndex + 1;

            mainActivity.myService.playTheSong(filePathToFragment.get(songIndex));





        } else if (view.getId() == R.id.previous) {

            songIndex = songIndex - 1;

            mainActivity.myService.playTheSong(filePathToFragment.get(songIndex));


        }
        else if(view.getId()==R.id.play)
        {
            mainActivity.myService.playTheSong(filePathToFragment.get(songIndex));


            if((mainActivity.isMyServiceRunning(MyService.class)))
            {


                mainActivity.myService.showNotification();

            }
            else{

                mainActivity.startTheService();
                mainActivity.myService.playTheSong(filePathToFragment.get(songIndex));
            }



        }



    }


}
