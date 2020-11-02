package com.example.voiceme.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.voiceme.R;
import com.example.voiceme.model.UserModel;
import com.example.voiceme.view.activity.MessageActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecycleViewAdapter extends RecyclerView.Adapter<UserRecycleViewAdapter.MyViewHolder> {

    Context mContext;
    List<UserModel> mData;

    public UserRecycleViewAdapter(Context mContext, List<UserModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.friends_item, parent, false);
        MyViewHolder vHolder = new MyViewHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.tVUsername.setText(mData.get(position).getUsername());
        holder.tVPhoneNumber.setText(mData.get(position).getPhoneNumber());
        holder.circleImageView.setImageResource(R.drawable.ic_friend);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, MessageActivity.class);
                i.putExtra("userId", mData.get(position).getId());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tVUsername;
        private TextView tVPhoneNumber;
        private CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tVUsername = itemView.findViewById(R.id.tVUsername);
            tVPhoneNumber = itemView.findViewById(R.id.tVPhoneNumber);
            circleImageView = itemView.findViewById(R.id.profile_image);

        }
    }
}
