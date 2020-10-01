package com.example.voiceme.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.voiceme.Firebase;
import com.example.voiceme.R;
import com.example.voiceme.model.ChatModel;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    Context mContext;
    List<ChatModel> mDataMessage;


    private String currentUserId;

    public MessageAdapter(Context mContext, List<ChatModel> mDataMessage) {
        this.mContext = mContext;
        this.mDataMessage = mDataMessage;
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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ChatModel chat = mDataMessage.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        holder.tVCreateAt.setText(formatter.format(chat.getCreateAt().getTime()));
        holder.tVShowMessage.setText("hello");

    }

    @Override
    public int getItemCount() {
        return mDataMessage.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tVShowMessage;
        private TextView tVCreateAt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tVCreateAt = itemView.findViewById(R.id.createAt);
            tVShowMessage = itemView.findViewById(R.id.show_message);

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