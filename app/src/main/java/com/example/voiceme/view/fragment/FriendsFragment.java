package com.example.voiceme.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.voiceme.Firebase;
import com.example.voiceme.R;
import com.example.voiceme.adapter.UserRecycleViewAdapter;
import com.example.voiceme.model.UserModel;
import com.example.voiceme.presenter.FriendsPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsFragment extends Fragment {

    View view;
    private RecyclerView myRecycleView;
    private FriendsPresenter presenter;

    public FriendsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends,container,false);
        initialize();
        setUpRecycleView();
        return view;
    }
    private void setUpRecycleView() {
        myRecycleView.setHasFixedSize(true);
        myRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initialize() {
        myRecycleView = view.findViewById(R.id.friends_recycle);
        presenter = new FriendsPresenter(getContext(), myRecycleView);
    }

    @Override
    public void onStart() {
        initialize();
        super.onStart();
    }

}
