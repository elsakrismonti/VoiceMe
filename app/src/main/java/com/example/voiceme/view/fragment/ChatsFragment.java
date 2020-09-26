package com.example.voiceme.view.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.voiceme.R;
import com.example.voiceme.presenter.ChatsPresenter;

public class ChatsFragment extends Fragment {

    private RecyclerView myRecyclerView;
    private View view;
    private ChatsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chats, container, false);
        initialize();
        setRecycleView();
        return view;
    }

    @Override
    public void onStart() {
        initialize();
        super.onStart();
    }

    private void initialize() {
        myRecyclerView = view.findViewById(R.id.chats_recycle);
        presenter = new ChatsPresenter(getContext(), myRecyclerView);
    }

    private void setRecycleView() {
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
