package com.example.voiceme.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.voiceme.R;
import com.example.voiceme.presenter.VerificationPresenter;

public class VerificationActivity extends AppCompatActivity implements VerificationPresenter.Presenter {
    private VerificationPresenter presenter;
    EditText eTVerificationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        eTVerificationCode = findViewById(R.id.eTVerificationCode);

        presenter = new VerificationPresenter(this);
    }

    public void verifikasiAction(View view) {
        presenter.setSentCode(eTVerificationCode.getText().toString());
        presenter.signIn();
    }
}