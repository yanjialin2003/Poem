package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;

public class AdviceActivity extends AppCompatActivity {

    private TextView poetryTitle;
    private MultiLineEditText advice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        Intent intent = getIntent();
        String poetryId = intent.getStringExtra("poemId");
        String Title = intent.getStringExtra("poemTitle");
        String username = intent.getStringExtra("username");

        initView();

        poetryTitle.setText(Title);
    }

    private void initView() {
        poetryTitle = findViewById(R.id.poetryTitle);
        advice = findViewById(R.id.advice);
    }
}