package com.example.voiceme.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.voiceme.Firebase;
import com.example.voiceme.R;
import com.example.voiceme.adapter.MessageAdapter;
import com.example.voiceme.model.ChatModel;
import com.example.voiceme.presenter.MessagePresenter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MessageActivity extends AppCompatActivity implements MessagePresenter.Presenter {

    FirebaseUser fUser;
    DatabaseReference reference;
    Toolbar chatToolBar;
    TextView tVProgress;
    String userId;
    List<ChatModel> mChat;

    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView micImage;
    private ImageButton btnRecord;
    private PulsatorLayout pulsatorLayout;
    private Animation animScale;
    private MessagePresenter messagePresenter;
    private static final int REQUEST_PERMISSION_CODE = 1;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initialize();
        settingActionBar();
        setRecycleView();

//        reference = Firebase.DataBase.user().child(userId);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserModel user = snapshot.getValue(UserModel.class);
//                getSupportActionBar().setTitle(user.getUsername());
//                readMessage(fuser.getUid(), userId);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void initialize() {
        tVProgress = findViewById(R.id.tVProgress);
        recyclerView = findViewById(R.id.recycle_view_message);
        messagePresenter = new MessagePresenter(this);
        pulsatorLayout = findViewById(R.id.pulsator);
        micImage = findViewById(R.id.imgV_mic);
        btnRecord = findViewById(R.id.btn_record);
        animScale = AnimationUtils.loadAnimation(this,R.anim.scale);
        chatToolBar = findViewById(R.id.chat_toolbar);
        i = getIntent();
        userId = i.getStringExtra("userId");
        fUser = Firebase.currentUser();
    }

    private void settingActionBar() {
        setSupportActionBar(chatToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    private void setRecycleView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void stopPulsatorLayout() {
        pulsatorLayout.stop();
    }

    @Override
    public void startPulsatorLayout() {
        pulsatorLayout.start();
    }

    public int getPermission(String permission) {
        return ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permission);
    }

    @Override
    public boolean checkPermission() {
        int permissionWrite = getPermission(WRITE_EXTERNAL_STORAGE);
        int permissionRecord = getPermission(RECORD_AUDIO);
        return permissionWrite == PackageManager.PERMISSION_GRANTED &&
                permissionRecord == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void setTVProgressText(String text) {
        tVProgress.setText(text);
    }

    @Override
    public Context getAppContext() {
        return this.getApplicationContext();
    }

    public void sendMessageBtn(View view) {
        micImage.startAnimation(animScale);
        btnRecord.startAnimation(animScale);
        messagePresenter.start();
    }

//    private void sendMessage(String sender, String recipient, String message){
//
//        DatabaseReference reference = Firebase.DataBase.chats();
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("sender", sender);
//        hashMap.put("recipient", recipient);
//        hashMap.put("message", message);
//        reference.push().setValue(hashMap);
//
//    }


//    public void sendMessageBtn(View view) {
//        try {
//            String message = eTMessage.getText().toString();
//            if(!message.equals("")){
//                sendMessage(fuser.getUid(), userId, message);
//            } else {
//                Log.d("TAG", "Message Empty");
//            }
//        } catch (Exception e){
//            Log.d("TAG", "Error : " + e);
//        }
//        eTMessage.setText("");
//
//    }

//    private void readMessage(final String sender, final String recipient){
//
//        mChat = new ArrayList<>();
//
//        reference = Firebase.DataBase.chats();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                try{
//                    mChat.clear();
//                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                        ChatModel chat = snapshot.getValue(ChatModel.class);
//                        if(chat.getRecipient().equals(sender) && chat.getSender().equals(recipient) ||
//                                chat.getRecipient().equals(recipient) && chat.getSender().equals(sender)){
//                            mChat.add(chat);
//                        }
//
//                        adapter = new MessageAdapter(MessageActivity.this, mChat);
//                        recyclerView.setAdapter(adapter);;
//
//                    }
//                }catch(Exception e){
//                    Log.e("TAG", "Error" + e);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
}