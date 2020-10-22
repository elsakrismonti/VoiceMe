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
import com.example.voiceme.model.DataFinal;
import com.example.voiceme.model.Keys;
import com.example.voiceme.model.RecordWav;
import com.example.voiceme.model.RunningTime;
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
    private double encRunningTime;
    private double decRunningTime;
    private double comRunningTime;
    private double decomRunningTime;
    private ChatRoomModel chatRoomModel;
    private MessageAdapter adapter;
    private String roomId;

    private List<ListenerRegistration> registrations = new ArrayList<>();

    public MessagePresenter(Presenter view) {
        this.view = view;
        recipientId = ((Activity) view).getIntent().getStringExtra("userId");
        currentUserId = Firebase.currentUser().getUid();
        checkRoom();
        getUserName(recipientId);
    }

    private void getUserName(String recipientId) {
//        ((Activity) view).getIntent().getStringExtra("userId"))
        Firebase.DataBase.user().document(Objects.requireNonNull(recipientId)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                view.setName(task.getResult().getString("userName"));
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
            byte[] sampleAmplitudes = WavUtil.getSampleAmplitudes(audio);
            Log.d("TAG", "sendMessage: " + sampleAmplitudes.length);
            final int[] samplesInt = Math.byteToInt(sampleAmplitudes);
            view.setTVProgressText("encrypting...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startTime = System.currentTimeMillis();
                    final int[] encryption = masseyOmura.encryption(samplesInt);
                    finishTime = System.currentTimeMillis();
                    encRunningTime = (finishTime - startTime)*0.001;
                    final List<Integer> dataVariation1 = LevensteinCode.sampleVariation(LevensteinCode.sortByFreq(encryption));
                    data1.setVariations(dataVariation1);
                    view.setTVProgressText("compressing...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startTime = System.currentTimeMillis();
                            String data = LevensteinCode.compression(encryption);
                            finishTime = System.currentTimeMillis();
                            data1.setData(data);
                            comRunningTime = (finishTime - startTime)*0.001;
                            RunningTime time = new RunningTime(encRunningTime, 0 , comRunningTime, 0);
                            data1.setTime(time);
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
                                    try {
                                        final MasseyOmura masseyOmura = new MasseyOmura(chat.getData1().getKey().getP());
                                        view.setTVProgressText("decompressing...");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startTime = System.currentTimeMillis();
                                                final int[] decompressed = LevensteinCode.decompression(chat.getData1().getData(), chat.getData1().getVariations());
                                                finishTime = System.currentTimeMillis();
                                                decomRunningTime = (finishTime - startTime) * 0.001;
                                                view.setTVProgressText("encrypting...");
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startTime = System.currentTimeMillis();
                                                        final int[] encryption = masseyOmura.encryption(decompressed);
                                                        finishTime = System.currentTimeMillis();
                                                        encRunningTime = (finishTime - startTime) * 0.001;
                                                        view.setTVProgressText("compressing...");
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                final List<Integer> dataVariation = LevensteinCode.sampleVariation(LevensteinCode.sortByFreq(encryption));
                                                                startTime = System.currentTimeMillis();
                                                                String data = LevensteinCode.compression(encryption);
                                                                finishTime = System.currentTimeMillis();
                                                                comRunningTime = (finishTime - startTime) * 0.001;
                                                                Keys keys = new Keys(masseyOmura.p, masseyOmura.d);
                                                                Data data2 = new Data();
                                                                data2.setKey(keys);
                                                                data2.setData(data);
                                                                data2.setVariations(dataVariation);
                                                                RunningTime time = new RunningTime(encRunningTime, 0, comRunningTime, decomRunningTime);
                                                                data2.setTime(time);
                                                                chat.setData2(data2);
                                                                Log.d("TAG", "onEvent: " + roomId + "\t" + chat);
                                                                view.setTVProgressText("sending...");
                                                                Firebase.DataBase.message(roomId, chat.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot snapshot) {
                                                                        if (snapshot.exists())
                                                                            Firebase.DataBase.message(roomId, chat.getId()).set(chat);
                                                                        view.setTVProgressText("waiting...");
                                                                    }
                                                                });
                                                            }
                                                        }, 100);
                                                    }
                                                }, 100);
                                            }
                                        }, 100);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
//                                Data Final
                                if (chat.getData3().getData() != null && chat.getDataFinal().getDataFinal() == null) {
                                    try{
                                        final MasseyOmura masseyOmura = new MasseyOmura(chat.getData2().getKey().getP(), chat.getData2().getKey().getD());
                                        view.setTVProgressText("decompressing...");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startTime = System.currentTimeMillis();
                                                final int[] decompressed = LevensteinCode.decompression(chat.getData3().getData(), chat.getData3().getVariations());
                                                finishTime = System.currentTimeMillis();
                                                decomRunningTime = (finishTime - startTime) * 0.001;
                                                view.setTVProgressText("decrypting...");
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startTime = System.currentTimeMillis();
                                                        final int[] dataFinal = masseyOmura.decryption(decompressed);
                                                        finishTime = System.currentTimeMillis();
                                                        decRunningTime = (finishTime - startTime) * 0.001;
                                                        StringBuilder s = new StringBuilder();
                                                        for (int i : dataFinal) {
                                                            s.append(i).append("_");
                                                        }
                                                        DataFinal dataFinal1 = new DataFinal();
                                                        dataFinal1.setDataFinal(s.toString());
                                                        RunningTime time = new RunningTime(0, decRunningTime, 0, decomRunningTime);
                                                        dataFinal1.setTime(time);
                                                        chat.setDataFinal(dataFinal1);
                                                        Log.d("TAG", "onEvent: " + roomId + "\t" + chat);

                                                        view.setTVProgressText("sending...");
                                                        Firebase.DataBase.message(roomId, chat.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot snapshot) {
                                                                if (snapshot.exists())
                                                                    Firebase.DataBase.message(roomId, chat.getId()).set(chat);
                                                                view.setTVProgressText("done");
                                                            }
                                                        });
                                                    }
                                                }, 100);
                                            }
                                        }, 100);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                //                            DATA 3
                                if (chat.getData3().getData() == null && chat.getData2().getData() != null) {
                                    try{
                                        final MasseyOmura masseyOmura = new MasseyOmura(chat.getData1().getKey().getP(), chat.getData1().getKey().getD());
                                        view.setTVProgressText("decompressing...");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startTime = System.currentTimeMillis();
                                                final int[] decompressed = LevensteinCode.decompression(chat.getData2().getData(), chat.getData2().getVariations());
                                                finishTime = System.currentTimeMillis();
                                                decomRunningTime = (finishTime - startTime) * 0.001;
                                                view.setTVProgressText("decrypting..");
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startTime = System.currentTimeMillis();
                                                        final int[] decryption = masseyOmura.decryption(decompressed);
                                                        finishTime = System.currentTimeMillis();
                                                        decRunningTime = (finishTime - startTime) * 0.001;
                                                        final List<Integer> dataVariation = LevensteinCode.sampleVariation(LevensteinCode.sortByFreq(decryption));
                                                        view.setTVProgressText("compressing..");
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                startTime = System.currentTimeMillis();
                                                                String data = LevensteinCode.compression(decryption);
                                                                finishTime = System.currentTimeMillis();
                                                                comRunningTime = (finishTime - startTime) * 0.001;
                                                                Keys keys = new Keys(masseyOmura.p, masseyOmura.d);
                                                                Data data3 = new Data();
                                                                data3.setKey(keys);
                                                                data3.setData(data);
                                                                data3.setVariations(dataVariation);
                                                                RunningTime time = new RunningTime(0, decRunningTime, comRunningTime, decomRunningTime);
                                                                data3.setTime(time);
                                                                chat.setData3(data3);

                                                                Firebase.DataBase.message(roomId, chat.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot snapshot) {
                                                                        if (snapshot.exists())
                                                                            Firebase.DataBase.message(roomId, chat.getId()).set(chat);
                                                                    }
                                                                });
                                                            }
                                                        }, 100);

                                                    }
                                                }, 100);

                                            }
                                        }, 100);

                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
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
