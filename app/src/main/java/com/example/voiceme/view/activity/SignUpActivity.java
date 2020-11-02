package com.example.voiceme.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.voiceme.R;
import com.example.voiceme.presenter.SignupPresenter;

public class SignUpActivity extends AppCompatActivity implements SignupPresenter.Presenter {

    private SignupPresenter presenter;
    private EditText eTUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize();
    }

    private void initialize(){
        eTUserName = findViewById(R.id.eTUserName);
        presenter = new SignupPresenter(this);
    }


    public void signUp(View view) {
        presenter.setUserName(eTUserName.getText().toString());
        presenter.restoreUsername();
    }
}