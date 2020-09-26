package com.example.voiceme.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.example.voiceme.Firebase;
import com.example.voiceme.LevensteinCode;
import com.example.voiceme.MasseyOmura;
import com.example.voiceme.Math;
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

import androidx.annotation.NonNull;

public class MessagePresenter {

    Presenter view;
    private Handler handler = new Handler();
    private RecordWav record = new RecordWav();
    private long startTime;
    private long finishTime;
    private double runningTime;
    private ChatRoomModel chatRoomModel;

    public MessagePresenter(Presenter view) {
        this.view = view;
        checkRoom();
    }

    public void start(){

        if(!record.isRecording) {
            view.startPulsatorLayout();
            startTime = System.currentTimeMillis();
            startRecording();
        }
        else {
            view.stopPulsatorLayout();
            sendMessage(record.stopRecording());
            handler.removeCallbacksAndMessages(null);
        }
    }

    void sendMessage(String filePath) {
        if(chatRoomModel == null){
            return;
        }
        MasseyOmura masseyOmura = new MasseyOmura();
        ChatModel chatModel = new ChatModel();
        chatModel.setSenderId(Firebase.currentUser().getUid());
        chatModel.setPrime(masseyOmura.p);
        File audio = new File(filePath);

        try {
            byte[] sampleAmplitudes = WavUtil.getSampleAmplitudes(audio);
            int[] samplesInt = Math.byteToInt(sampleAmplitudes);
            int[] encryption = masseyOmura.encryption(samplesInt);
            String data1 = LevensteinCode.compression(encryption);
            chatModel.setData1(data1);
            FirebaseFirestore.getInstance().collection("chatRoom").document(chatRoomModel.getId()).collection("messages").add(chatModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    documentReference.update("id", documentReference.getId());

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText((Context) view, filePath, Toast.LENGTH_SHORT).show();
    }

    private void checkRoom() {
        final String recipientId = ((Activity) view).getIntent().getStringExtra("userId");
        final String currentUserId = Firebase.currentUser().getUid();
        final List<String> users = new ArrayList<String>(){{add(recipientId); add(currentUserId);}};
        final CollectionReference rootRef = Firebase.DataBase.chatRoom();

        FirebaseFirestore.getInstance().collection("Users").document(currentUserId).collection("chat").document(recipientId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful() && task.getResult() != null && task.getResult().exists()){
                   rootRef.document(task.getResult().getString("chatRoomId")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           if(task.isSuccessful() && task.getResult() != null){
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
                           if(task.isSuccessful() && task.getResult() != null){
                               rootRef.document(task.getResult().getId()).update("id", task.getResult().getId());
                               chatRoomModel.setId(task.getResult().getId());
                               HashMap<String,String> data = new HashMap<String, String>();
                               data.put("chatRoomId", chatRoomModel.getId());
                               Firebase.DataBase.user().document(currentUserId).collection("chat").document(recipientId).set(data);
                               Firebase.DataBase.user().document(recipientId).collection("chat").document(currentUserId).set(data);
                           }
                       }
                   });
               }

            }
        });
//        rootRef.whereIn("user1", users).whereIn("user2", users).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful() && task.getResult() != null && task.getResult().size() == 1){
//                    chatRoomModel = task.getResult().toObjects(ChatRoomModel.class).get(0);
//                }
//                else {
//                    chatRoomModel = new ChatRoomModel();
//                    chatRoomModel.setUser1(recipientId);
//                    chatRoomModel.setUser2(currentUserId);
//                    rootRef.add(chatRoomModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            if(task.isSuccessful() && task.getResult() != null){
//                                rootRef.document(task.getResult().getId()).update("id", task.getResult().getId());
//                                chatRoomModel.setId(task.getResult().getId());
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }

    void encryption(){

    }

    private void startRecording() {
        if (view.checkPermission()) {
            record.startRecording();
            int recordDuration = 20000; //20 seconds

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMessage(record.stopRecording());
                    view.stopPulsatorLayout();
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
        Context getAppContext();
    }
}
