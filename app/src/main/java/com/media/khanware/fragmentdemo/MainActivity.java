package com.media.khanware.fragmentdemo;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;

import com.media.khanware.fragmentdemo.MyService.MyBindService;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {


    // Variable to store MP3 data
    ArrayList<String> listOfSongs = new ArrayList<String>();

    ArrayList<String> filePathToFragment = new ArrayList<String>();

    //Album Information

    ArrayList<String> albumInfoDisplay = new ArrayList<String>();

    ArrayList<String> durationOfTheSong = new ArrayList<String>();


    private final int REQUEST_GRANTED_BY_USER = 1;

    //Content provider variable data
    String MUSIC_STRING = MediaStore.Audio.Media.IS_MUSIC + "!=0";
    String[] SQL_FETCH_ALL_MUSIC = {"*"};
    Uri songPath = null;
    String ordercolumns = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALISED ASC";

    //Variables for other operation
    int music_column_index;
    Cursor cursor;

    //MP3 Metadata
    String songFileName;
    String songFilePath;
    String albumInfo;
    String albumID;
    String duration;
    String lengthOfSong;
    //Service instance
    MyService myService;
    boolean isBound;
    Intent intent;

    //Android ListView for displaying songs
    ListView songListView;


    public static final String ACTION_PLAY = "com.media.khanware.fragmentdemo.action.PLAY";

    @Override
    protected void onStart() {
        super.onStart();
        if(intent == null)
        {
            intent = new Intent(MainActivity.this, MyService.class);
            intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(intent);
            bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

        }



    }

    @Override
    protected void onDestroy() {
        if (myService != null)

        unbindService(myConnection);


        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (myService != null) {

            myService.showNotification();
        }
    }


    public ServiceConnection myConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            MyBindService myBindService = (MyBindService) iBinder;
            myService = myBindService.getBoundService();
            isBound = true;
            Toast.makeText(getApplicationContext(), "SERVICE CONNECTED", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            isBound = false;


        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songListView = (ListView) findViewById(R.id.listView);
        // Custom Adapter to display list of songs

        SongsList songsList = new SongsList(MainActivity.this, listOfSongs, albumInfoDisplay, durationOfTheSong);
        songListView.setAdapter(songsList);
        songListView.setOnItemClickListener(this);

        //Check runtime permissions in Android
        checkPermissions();
        Toast.makeText(getApplicationContext(), "Total number of Items are:" + songListView.getAdapter().getCount(), Toast.LENGTH_LONG).show();
        try {
            validate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void validate() throws InterruptedException {


        Thread.sleep(1000);
        if (isBound) {

            myService.getSongs(filePathToFragment);
            Toast.makeText(getApplicationContext(), "isBound is TRUE NOW CLICK TO PLAY", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(getApplicationContext(), "isBound is false currently", Toast.LENGTH_LONG).show();
        }

    }


    private void checkPermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GRANTED_BY_USER);
            } else {
                getListOfSongsFromDevice();
            }
        } else {
            getListOfSongsFromDevice();
        }

    }

    void getListOfSongsFromDevice() {

        //Query all the list of available Mp3 songs
        songPath = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        cursor = getContentResolver().query(songPath, SQL_FETCH_ALL_MUSIC, MUSIC_STRING, null, null);

        if (cursor != null)

        {
            if (cursor.moveToFirst()) {
                do {
                    songFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    songFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    albumInfo = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    albumID = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    int durationparse = Integer.parseInt(duration);
                    int mns = (durationparse / 60000) % 60000;
                    int scs = (durationparse % 60000) / 1000;

                    lengthOfSong = String.format(Locale.ENGLISH, "%02d:%02d", mns, scs);

                    listOfSongs.add(songFileName);
                    filePathToFragment.add(songFilePath);
                    albumInfoDisplay.add(albumInfo);
                    durationOfTheSong.add(lengthOfSong);


                } while (cursor.moveToNext());

            }

        }


        listOfSongs.size();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        {
            //Get the path of the song form the cursor results returned by the content provider

            music_column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToPosition(i);
            String songPath = cursor.getString(music_column_index);
            String songFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

            //Open a fragment which will display the music buttons

            MusicSC fragment = new MusicSC();

            // Bundle to send data to the fragment

            Bundle bundle = new Bundle();
            bundle.putInt("indexOfSong", i);
            bundle.putString("songFilePath", songPath);
            bundle.putString("songFileName", songFileName);
            bundle.putStringArrayList("listOfSongPath", filePathToFragment);
            fragment.setArguments(bundle);

            //Instantiating fragment
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, fragment);
            ft.addToBackStack(null);
            ft.commit();


        }
    }

    public void startTheService()
    {
        intent = new Intent(MainActivity.this, MyService.class);
        intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(intent);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);


    }



    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}





