package com.media.khanware.fragmentdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Zia Khan on 04-07-2016.
 */
public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.media.khanware.fragmentdemo.action.main";
        public static String INIT_ACTION = "com.media.khanware.fragmentdemo.action.init";
        public static String PREV_ACTION = "com.media.khanware.fragmentdemo.action.prev";
        public static String PLAY_ACTION = "com.media.khanware.fragmentdemo.action.play";
        public static String NEXT_ACTION = "com.media.khanware.fragmentdemo.action.next";
        public static String STARTFOREGROUND_ACTION = "com.media.khanware.fragmentdemo.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.media.khanware.fragmentdemo.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_album_art, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}

    /*private static Bitmap defaultAlbum;

    public static Bitmap getDefaultAlbumArt(Context context) {
        if (defaultAlbum==null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            try {
                defaultAlbum = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.default_album_art, options);
            } catch (Error ee) {
            } catch (Exception e) {
            }
        }
        return defaultAlbum;
    }*/