package com.example.voiceme.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voiceme.Firebase;
import com.example.voiceme.LevensteinCode;
import com.example.voiceme.MasseyOmura;
import com.example.voiceme.Math;
import com.example.voiceme.adapter.MessageAdapter;
import com.example.voiceme.model.ChatModel;
import com.example.voiceme.model.ChatRoomModel;
import com.example.voiceme.model.RecordWav;
import com.example.voiceme.utilities.WavUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagePresenter {

    Presenter view;
    private Handler handler = new Handler();
    private RecordWav record = new RecordWav();
    private RecyclerView recyclerView;
    private long startTime;
    private long finishTime;
    private double runningTime;
    private ChatRoomModel chatRoomModel;
    private MessageAdapter adapter;

    public MessagePresenter(Presenter view) {
        this.view = view;
        checkRoom();
    }

    public void start() {

        if (!RecordWav.isRecording) {
            view.setTVProgressText("recording...");
            view.startPulsatorLayout();
            startTime = System.currentTimeMillis();
            startRecording();
        } else {
            view.setTVProgressText("recording stopped...");
            view.stopPulsatorLayout();
            sendMessage(record.stopRecording());
            handler.removeCallbacksAndMessages(null);
        }
    }

    void sendMessage(final String filePath) {
        if (chatRoomModel == null) {
            return;
        }
        final MasseyOmura masseyOmura = new MasseyOmura();
        final ChatModel chatModel = new ChatModel();
        chatModel.setSenderId(Firebase.currentUser().getUid());
        chatModel.setPrime(masseyOmura.p);
        File audio = new File(filePath);

        try {
            startTime = System.currentTimeMillis();
            byte[] sampleAmplitudes = WavUtil.getSampleAmplitudes(audio);
            Log.d("TAG", "sendMessage: " + sampleAmplitudes.length);
            final int[] samplesInt = Math.byteToInt(sampleAmplitudes);
            final List<Integer> dataVariation1 = LevensteinCode.sampleVariation(LevensteinCode.sortByFreq(samplesInt));

            view.setTVProgressText("encrypting...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int[] encryption = masseyOmura.encryption(samplesInt);

                    view.setTVProgressText("compressing...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String data1 = LevensteinCode.compression(encryption);
                            chatModel.setData1(data1);
                            chatModel.setVariation1(dataVariation1);

                            view.setTVProgressText("sending...");
                            FirebaseFirestore.getInstance().collection("chatRoom").document(chatRoomModel.getId()).collection("messages").add(chatModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    documentReference.update("id", documentReference.getId());
                                    view.setTVProgressText("waiting...");
                                }
                            });

                            readMessages(recyclerView);
                        }
                    }, 100);

                }
            }, 100);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkRoom() {
        final String recipientId = ((Activity) view).getIntent().getStringExtra("userId");
        final String currentUserId = Firebase.currentUser().getUid();
        final List<String> users = new ArrayList<String>() {{
            add(recipientId);
            add(currentUserId);
        }};
        final CollectionReference rootRef = Firebase.DataBase.chatRoom();

        Firebase.DataBase.user().document(currentUserId).collection("chats").document(recipientId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                    rootRef.document(task.getResult().getString("chatRoomId")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                            }
                        }
                    });
                } else {
                    chatRoomModel = new ChatRoomModel();
                    chatRoomModel.setUser1(recipientId);
                    chatRoomModel.setUser2(currentUserId);
                    rootRef.add(chatRoomModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                rootRef.document(task.getResult().getId()).update("id", task.getResult().getId());
                                chatRoomModel.setId(task.getResult().getId());
                                HashMap<String, String> data = new HashMap<String, String>();
                                data.put("chatRoomId", chatRoomModel.getId());
                                Firebase.DataBase.user().document(currentUserId).collection("chats").document(recipientId).set(data);
                                Firebase.DataBase.user().document(recipientId).collection("chats").document(currentUserId).set(data);
                            }
                        }
                    });
                }

            }
        });
    }

    public void readMessages(final RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        final String currentUserId = Firebase.currentUser().getUid();
        final String recipientId = ((Activity) view).getIntent().getStringExtra("userId");
        final CollectionReference rootRef = Firebase.DataBase.chatRoom();
        final List mChat = new ArrayList<>();
        DocumentReference reference = Firebase.DataBase.user().document(currentUserId).collection("chats").document(recipientId);
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null && task.getResult().exists()){
                            rootRef.document(task.getResult().getString("chatRoomId")).collection("messages").orderBy("createAt").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty())    {
                                        mChat.clear();
                                        for(DocumentSnapshot querySnapshot: task.getResult()){
                                            ChatModel chat = new ChatModel(querySnapshot.getString("senderId"), querySnapshot.getString("dataFinal"),
                                            querySnapshot.getDate("createAt"));
                                            mChat.add(chat);
                                            adapter = new MessageAdapter(((Activity) view), mChat);
                                            recyclerView.setAdapter(adapter);;
                                        }
                                    }
                                }
                            });
                        }else{
                            view.setTVProgressText("No Messages");
                        }
                    }
                });
    }

    private void startRecording() {
        if (view.checkPermission()) {
            record.startRecording();
            int recordDuration = 20000; //20 seconds

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.stopPulsatorLayout();
                    view.setTVProgressText("recording stopped...");
                    sendMessage(record.stopRecording());
                }
            }, recordDuration);

        } else {
            view.requestPermission();
        }
    }

    public interface Presenter {
        void stopPulsatorLayout();

        void startPulsatorLayout();

        boolean checkPermission();

        void requestPermission();

        void setTVProgressText(String text);

        Context getAppContext();
    }
}
