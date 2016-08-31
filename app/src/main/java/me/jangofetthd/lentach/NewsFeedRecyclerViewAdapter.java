package me.jangofetthd.lentach;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiVideo;
import com.vk.sdk.api.model.VKAttachments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsFeedRecyclerViewAdapter extends RecyclerView.Adapter<NewsFeedRecyclerViewAdapter.ViewHolder> {

    private List<VKApiPost> posts;
    Context context;

    public NewsFeedRecyclerViewAdapter(List<VKApiPost> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }


    @Override
    public NewsFeedRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(NewsFeedRecyclerViewAdapter.ViewHolder holder, int position) {
        VKApiPost post = posts.get(position);

        if (!post.text.isEmpty()) {
            holder.pText.setVisibility(View.VISIBLE);
            holder.pText.setText(post.text);
        } else {
            holder.pText.setVisibility(View.GONE);
        }
        long time = post.date * (long) 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.YY");
        holder.pTime.setText(format.format(date).toString());

        List<String> photos = new ArrayList<>(),
                documents = new ArrayList<>(),
                videos = new ArrayList<>();
        List<VKApiAudio> audios = new ArrayList<>();

        for (VKAttachments.VKApiAttachment attachment : post.attachments) {
            if (attachment.getType().equals(VKAttachments.TYPE_PHOTO)) {
                photos.add(((VKApiPhoto) attachment).photo_604);
            } else if (attachment.getType().equals(VKAttachments.TYPE_VIDEO)) {
                videos.add(((VKApiVideo) attachment).external);
            } else if (attachment.getType().equals(VKAttachments.TYPE_DOC)) {
                documents.add(((VKApiDocument) attachment).url);
            } else if (attachment.getType().equals(VKAttachments.TYPE_AUDIO)) {
                audios.add((VKApiAudio) attachment);
            }
        }

        if (photos.isEmpty()) {
            holder.pImagesScroll.setVisibility(View.GONE);
        } else {
            holder.pImagesScroll.setVisibility(View.VISIBLE);
            LinearLayout linearLayout = holder.llPhotos;
            linearLayout.removeAllViewsInLayout();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (String url : photos) {
                ImageView image = new ImageView(context);
                linearLayout.addView(image, layoutParams);
                Glide.with(context).load(url).into(image);
            }
            ViewGroup.LayoutParams params = holder.pImagesScroll.getLayoutParams();
            if (photos.size() != 1) {
                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, context.getResources().getDisplayMetrics());
            } else {
                params = layoutParams;
                holder.pImagesScroll.setLayoutParams(params);
            }
        }
        if (audios.isEmpty()) {
            holder.pAudiosList.setVisibility(View.GONE);
        } else {
            holder.pAudiosList.setVisibility(View.VISIBLE);
            holder.pAudiosList.setAdapter();
        }
        if (documents.isEmpty()) {
            holder.pGifsScroll.setVisibility(View.GONE);
        } else {
            holder.pGifsScroll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pText;
        private TextView pTime;
        private HorizontalScrollView pImagesScroll;
        private HorizontalScrollView pGifsScroll;
        private ListView pAudiosList;
        private ImageButton pLike;
        private ImageButton pRepost;
        private LinearLayout llPhotos;
        private LinearLayout llDocs;

        public ViewHolder(View itemView) {
            super(itemView);
            pText = (TextView) itemView.findViewById(R.id.text);
            pTime = (TextView) itemView.findViewById(R.id.time);
            pImagesScroll = (HorizontalScrollView) itemView.findViewById(R.id.photos);
            pGifsScroll = (HorizontalScrollView) itemView.findViewById(R.id.gifs);
            pAudiosList = (ListView) itemView.findViewById(R.id.audios);
            pLike = (ImageButton) itemView.findViewById(R.id.like);
            pRepost = (ImageButton) itemView.findViewById(R.id.repost);
            llPhotos = (LinearLayout) itemView.findViewById(R.id.llphotos);
            llDocs = (LinearLayout) itemView.findViewById(R.id.llgifs);

        }
    }

    class VkAudioAdapter extends BaseAdapter {

        List<VKApiAudio> audios;

        VkAudioAdapter(List<VKApiAudio> audios) {
            this.audios = audios;
        }

        @Override
        public int getCount() {
            return audios.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View returnView;
            if (view == null)
                returnView = ((LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.player, viewGroup);

            

            return null;
        }
    }
}
