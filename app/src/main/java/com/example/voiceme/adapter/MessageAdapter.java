package com.example.voiceme.adapter;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.voiceme.Firebase;
import com.example.voiceme.R;
import com.example.voiceme.model.ChatModel;
import com.example.voiceme.view.activity.MessageActivity;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    Context mContext;
    List<ChatModel> mDataMessage;


    FirebaseUser fUser;

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
       //holder.tVShowMessage.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return mDataMessage.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tVShowMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tVShowMessage = itemView.findViewById(R.id.show_message);

        }
    }

//    @Override
//    public int getItemViewType(int position) {
//
//        fUser = Firebase.currentUser();
////        if(mDataMessage.get(position).getSender().equals(fUser.getUid())){
////            return MSG_TYPE_RIGHT;
////        } else {
////            return MSG_TYPE_LEFT;
////        }
//
//    }
}