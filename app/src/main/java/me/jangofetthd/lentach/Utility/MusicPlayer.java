package me.jangofetthd.lentach.Utility;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by JangoFettHD on 31.08.2016.
 */
public final class MusicPlayer {

    private static Context context;
    private static String url;
    private static TextView time;

    private MusicPlayer(){

    }

    public static MusicPlayer with (Context context) {
        this.context = context;
    }

}
