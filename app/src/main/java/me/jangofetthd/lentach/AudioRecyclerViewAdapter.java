package me.jangofetthd.lentach;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiAudio;

import java.util.List;

/**
 * Created by JangoFettHD on 31.08.2016.
 */
public class AudioRecyclerViewAdapter extends RecyclerView.Adapter<AudioRecyclerViewAdapter.ViewHolder> {

    List<VKApiAudio> audios;
    Context context;

    public AudioRecyclerViewAdapter(List<VKApiAudio> audios, Context context) {
        this.audios = audios;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.player, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VKApiAudio audio = audios.get(position);

        holder.mPlay.setImageResource(R.drawable.play);
        holder.mTitle.setText(audio.artist+" - "+audio.title);
        holder.mTime.setText(audio.duration/60+":"+audio.duration%60);
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTime;
        private TextView mTitle;
        private SeekBar mSeekBar;
        private ImageButton mPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            mTime = (TextView) itemView.findViewById(R.id.mTime);
            mTitle = (TextView) itemView.findViewById(R.id.mTitle);
            mSeekBar = (SeekBar) itemView.findViewById(R.id.mSeekBar);
            mPlay = (ImageButton) itemView.findViewById(R.id.mPlay);
        }
    }
}
