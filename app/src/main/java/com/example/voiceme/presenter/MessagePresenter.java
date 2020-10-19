package com.example.voiceme.presenter;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.voiceme.Firebase;
import com.example.voiceme.LevensteinCode;
import com.example.voiceme.MasseyOmura;
import com.example.voiceme.Math;
import com.example.voiceme.adapter.MessageAdapter;
import com.example.voiceme.model.ChatModel;
import com.example.voiceme.model.ChatRoomModel;
import com.example.voiceme.model.Data;
import com.example.voiceme.model.Keys;
import com.example.voiceme.model.RecordWav;
import com.example.voiceme.model.UserModel;
import com.example.voiceme.utilities.WavUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessagePresenter {

    private final String recipientId;
    private final String currentUserId;
    Presenter view;
    private Handler handler = new Handler();
    private RecordWav record = new RecordWav();
    private long startTime;
    private long finishTime;
    private double runningTime;
    private ChatRoomModel chatRoomModel;
    private MessageAdapter adapter;
    private String roomId;

    private List<ListenerRegistration> registrations = new ArrayList<>();

    public MessagePresenter(Presenter view) {
        this.view = view;
        recipientId = ((Activity) view).getIntent().getStringExtra("userId");
        currentUserId = Firebase.currentUser().getUid();
        checkRoom();
        getUserName();
    }

    private void getUserName() {
        Firebase.DataBase.user().document(Objects.requireNonNull(((Activity) view).getIntent().getStringExtra("userId"))).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                UserModel userModel = task.getResult().toObject(UserModel.class);
                view.setName(userModel.getUsername());
                Log.d("TAG", "username" + userModel.getUsername());
                readMessages();
            }
        });
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
        Keys keys = new Keys(masseyOmura.p, masseyOmura.d);
        final Data data1 = new Data();
        data1.setKey(keys);

        File audio = new File(filePath);

        try {
            startTime = System.currentTimeMillis();
            byte[] sampleAmplitudes = WavUtil.getSampleAmplitudes(audio);
            Log.d("TAG", "sendMessage: " + sampleAmplitudes.length);
            final int[] samplesInt = Math.byteToInt(sampleAmplitudes);


            view.setTVProgressText("encrypting...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int[] encryption = masseyOmura.encryption(samplesInt);
                    final List<Integer> dataVariation1 = LevensteinCode.sampleVariation(LevensteinCode.sortByFreq(encryption));
                    view.setTVProgressText("compressing...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String data = LevensteinCode.compression(encryption);
                            data1.setData(data);
                            data1.setVariations(dataVariation1);
                            chatModel.setData1(data1);

                            view.setTVProgressText("sending...");
                            Firebase.DataBase.message(chatRoomModel.getId()).add(chatModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    documentReference.update("id", documentReference.getId());
                                    view.setTVProgressText("waiting...");
                                }
                            });

                            readMessages();
                        }
                    }, 100);

                }
            }, 100);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkRoom() {

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

    public void readMessages() {
        DocumentReference reference = Firebase.DataBase.user().document(currentUserId).collection("chats").document(recipientId);
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                            MessagePresenter.this.roomId = task.getResult().getString("chatRoomId");
                            getListMessage();
                        } else {
                            view.setTVProgressText("No Messages");
                        }
                    }
                });
    }

    void getListMessage() {
        registrations.add(Firebase.DataBase.message(roomId).orderBy("createAt").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                final List<ChatModel> mChat = new ArrayList<>();
                if (error == null && value != null && !value.isEmpty()) {
                    for (DocumentSnapshot querySnapshot : value.getDocuments()) {
                        final ChatModel chat = querySnapshot.toObject(ChatModel.class);
                        Log.d("TAG", "onEvent: " + chat);
                        assert chat != null;
                        if (chat.getId() != null) {
                            if (!chat.getSenderId().equals(currentUserId)) {

//                            DATA 2
                                if (chat.getData2().getData() == null) {
                                    MasseyOmura masseyOmura = new MasseyOmura(chat.getData1().getKey().getP());
                                    final int[] decompressed = LevensteinCode.decompression(chat.getData1().getData(), chat.getData1().getVariations());
                                    final int[] encryption = masseyOmura.encryption(decompressed);
                                    final List<Integer> dataVariation = LevensteinCode.sampleVariation(LevensteinCode.sortByFreq(encryption));
                                    String data = LevensteinCode.compression(encryption);
                                    Keys keys = new Keys(masseyOmura.p, masseyOmura.d);
                                    Data data2 = new Data();
                                    data2.setKey(keys);
                                    data2.setData(data);
                                    data2.setVariations(dataVariation);
                                    chat.setData2(data2);

                                    Log.d("TAG", "onEvent: " + roomId + "\t" + chat);
                                    Firebase.DataBase.message(roomId, chat.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot snapshot) {
                                            if (snapshot.exists())
                                                Firebase.DataBase.message(roomId, chat.getId()).set(chat);
                                        }
                                    });
                                }

//                                Data Final
                                if (chat.getData3().getData() != null && chat.getDataFinal() == null) {
                                    MasseyOmura masseyOmura = new MasseyOmura(chat.getData2().getKey().getP(), chat.getData2().getKey().getD());
                                    final int[] decompressed = LevensteinCode.decompression(chat.getData3().getData(), chat.getData3().getVariations());
                                    final int[] dataFinal = masseyOmura.decryption(decompressed);

                                    StringBuilder s = new StringBuilder();
                                    for (int i :
                                            dataFinal) {
                                        s.append(i).append("_");
                                    }
                                    chat.setDataFinal(s.toString());

                                    Log.d("TAG", "onEvent: " + roomId + "\t" + chat);
                                    Firebase.DataBase.message(roomId, chat.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot snapshot) {
                                            if (snapshot.exists())
                                                Firebase.DataBase.message(roomId, chat.getId()).set(chat);
                                        }
                                    });
                                }

                            } else {
                                //                            DATA 3
                                if (chat.getData3().getData() == null && chat.getData2().getData() != null) {
                                    MasseyOmura masseyOmura = new MasseyOmura(chat.getData1().getKey().getP(), chat.getData1().getKey().getD());
                                    final int[] decompressed = LevensteinCode.decompression(chat.getData2().getData(), chat.getData2().getVariations());
                                    final int[] decryption = masseyOmura.decryption(decompressed);
                                    final List<Integer> dataVariation = LevensteinCode.sampleVariation(LevensteinCode.sortByFreq(decryption));
                                    String data = LevensteinCode.compression(decryption);
                                    Keys keys = new Keys(masseyOmura.p, masseyOmura.d);
                                    Data data3 = new Data();
                                    data3.setKey(keys);
                                    data3.setData(data);
                                    data3.setVariations(dataVariation);
                                    chat.setData3(data3);

                                    Firebase.DataBase.message(roomId, chat.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot snapshot) {
                                            if (snapshot.exists())
                                                Firebase.DataBase.message(roomId, chat.getId()).set(chat);
                                        }
                                    });
                                }
                            }
                            mChat.add(chat);
                            view.updateList(mChat);
                        }

//                                            adapter = new MessageAdapter(((Activity) view), mChat);
//                                            recyclerView.setAdapter(adapter);;
                    }
                }
//                                    if(task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty())    {
//                                        mChat.clear();
//                                        for(DocumentSnapshot querySnapshot: task.getResult()){
//                                            ChatModel chat = new ChatModel(querySnapshot.getString("senderId"), querySnapshot.getString("dataFinal"),
//                                            querySnapshot.getDate("createAt"));
//                                            mChat.add(chat);
//                                            adapter = new MessageAdapter(((Activity) view), mChat);
//                                            recyclerView.setAdapter(adapter);;
//                                        }
//                                    }
            }
        }));
    }

    private void startRecording() {
        if (view.checkPermission()) {
            record.startRecording();
            int recordDuration = 20000; //20 seconds

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.stopPulsatorLayout();
                    view.setTVProgressText("recording stopped");
                    sendMessage(record.stopRecording());
                }
            }, recordDuration);

        } else {
            view.requestPermission();
        }
    }

    public void destroy() {
        for (ListenerRegistration re :
                registrations) {
            re.remove();
        }
    }

    public interface Presenter {
        void stopPulsatorLayout();

        void startPulsatorLayout();

        boolean checkPermission();

        void requestPermission();

        void setTVProgressText(String text);

        void updateList(List<ChatModel> chatModels);

        void setName(String username);
    }
}
