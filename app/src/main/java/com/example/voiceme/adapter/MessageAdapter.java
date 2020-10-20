package com.example.voiceme.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voiceme.Firebase;
import com.example.voiceme.Math;
import com.example.voiceme.R;
import com.example.voiceme.model.ChatModel;
import com.example.voiceme.model.CreateWav;
import com.example.voiceme.model.RecordWav;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private CreateWav createWav;

    Context mContext;
    private List<ChatModel> mDataMessage = new ArrayList<>();
    private String currentUserId;

    public MessageAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_LEFT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            MyViewHolder vHolder = new MyViewHolder(view);
            return vHolder;
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            MyViewHolder vHolder = new MyViewHolder(view);
            return vHolder;
        }
    }

    public String getFilename(String filename){

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,"AudioRecorder");
        if(!file.exists()){
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + filename + ".wav");
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ChatModel chat = mDataMessage.get(position);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        holder.tVCreateAt.setText(formatter.format(chat.getCreateAt().getTime()));
        final String source = getFilename("Wav_final");
        try{
            holder.iVPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (chat.getDataFinal() != null) {
                        createFile(chat.getDataFinal());
                        if (holder.mediaPlayer.isPlaying()) {
                            holder.handler.removeCallbacks(holder.updater);
                            holder.mediaPlayer.pause();
                            holder.iVPlayPause.setImageResource(R.drawable.ic_play);
                        } else {
                            holder.mediaPlayer.start();
                            holder.iVPlayPause.setImageResource(R.drawable.ic_pause);
                            holder.updateSeekBar();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.prepareMediaPlayer(source);

        holder.seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SeekBar seekBar = (SeekBar) view;
                int playPosition = (holder.mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                holder.mediaPlayer.seekTo(playPosition);
                holder.tVCurrentTime.setText(holder.millisecondsToTimer(holder.mediaPlayer.getCurrentPosition()));
                return false;
            }
        });

        holder.mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                holder.seekBar.setSecondaryProgress(i);
            }
        });

        holder.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                holder.seekBar.setProgress(0);
                holder.iVPlayPause.setImageResource(R.drawable.ic_play);
                holder.tVCurrentTime.setText(R.string.zero);
                mediaPlayer.reset();
                holder.prepareMediaPlayer(source);
            }
        });
    }

    private void createFile(String dataFinal) {
        byte[] data = new byte[dataFinal.split("_").length];
        int i = 0;
        for (String s :
                dataFinal.split("_")) {
            byte temp = (byte)Integer.parseInt(s);
            data[i] = temp;
            i++;
        }
        createWav = new CreateWav(data);
        createWav.createWaveFile();
    }

    @Override
    public int getItemCount() {
        return mDataMessage.size();
    }

    public void setmDataMessage(List<ChatModel> mDataMessage) {
        this.mDataMessage = mDataMessage;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tVCreateAt;
        private ImageView iVPlayPause;
        private SeekBar seekBar;
        private TextView tVCurrentTime;
        private MediaPlayer mediaPlayer;
        private Handler handler = new Handler();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tVCreateAt = itemView.findViewById(R.id.createAt);
            iVPlayPause = itemView.findViewById(R.id.imagePlayPause);
            seekBar = itemView.findViewById(R.id.playSeekBar);
            tVCurrentTime = itemView.findViewById(R.id.currentTime);
            mediaPlayer = new MediaPlayer();
            seekBar.setMax(100);
        }

        public void prepareMediaPlayer(String source){
            try {
                mediaPlayer.setDataSource(source);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private Runnable updater = new Runnable() {
            @Override
            public void run() {
                updateSeekBar();
                long currentDuration = mediaPlayer.getCurrentPosition();
                tVCurrentTime.setText(millisecondsToTimer(currentDuration));
            }
        };

        public void updateSeekBar(){
            if(mediaPlayer.isPlaying()){
                seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
                handler.postDelayed(updater, 1000);
            }
        }

        public String millisecondsToTimer(long milliSeconds){
            String timeString = "";
            String secondString = "";

            int hours = (int) (milliSeconds / (1000 * 60 * 60));
            int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
            int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

            if(hours > 0) timeString = hours + ":";

            if(seconds < 10){
                secondString = "0" + seconds;
            } else {
                secondString = "" + seconds;
            }
            timeString = timeString + minutes + ":" + secondString;
            return timeString;
        }
    }

    @Override
    public int getItemViewType(int position) {

        currentUserId= Firebase.currentUser().getUid();
        if(mDataMessage.get(position).getSenderId().equals(currentUserId)){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }
}