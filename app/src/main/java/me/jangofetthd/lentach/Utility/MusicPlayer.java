package me.jangofetthd.lentach.Utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by JangoFettHD on 31.08.2016.
 */
public class MusicPlayer {
    
    private static Context context;
    private static String url;
    private static TextView time;
    private static SeekBar seekBar;

    /**
     * Duration of media in milliseconds
     */
    private static int duration = -1;

    public static MediaPlayer getPlayer() {
        if (player==null)
            player = new MediaPlayer();
        return player;
    }

    private static MediaPlayer player;

    private static View.OnTouchListener onSeekBarTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getId() == seekBar.getId()) {
                if (getPlayer().isPlaying()) {
                    int playPositionInMilliseconds = (duration / 100) * seekBar.getProgress();
                    getPlayer().seekTo(playPositionInMilliseconds);
                }
            }
            return false;
        }
    };

    private static MusicPlayer instance;

    private MusicPlayer(Context context) {
        MusicPlayer.context = context;
    }

    public static MusicPlayer with(Context context) {
        instance = new MusicPlayer(context);
        player = new MediaPlayer();
        return instance;
    }

    public static MusicPlayer load(@NonNull String url) throws Exception {
        if (!isValidUrl(url))
            throw new Exception("Url is not valid");
        Log.w("Player", "url: "+url);
        MusicPlayer.url = url;
        getPlayer().setDataSource(url);
        return instance;
    }

    public static MusicPlayer withSeekBar(@NonNull SeekBar seekBar) {
        MusicPlayer.seekBar = seekBar;
        seekBar.setMax(99);
        seekBar.setOnTouchListener(onSeekBarTouchListener);
        return instance;
    }

    public static MusicPlayer withTimeField(@NonNull TextView time) {
        MusicPlayer.time = time;
        return instance;
    }

    public static MusicPlayer withDuration(int duration) {
        MusicPlayer.duration = duration;

        return instance;
    }

    public static void play() {
        if (!getPlayer().isPlaying()) {
            getPlayer().prepareAsync();
            getPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    getPlayer().start();
                }
            });
        }
    }

    /* ---------------------------- */

    private static boolean isValidUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }



}
