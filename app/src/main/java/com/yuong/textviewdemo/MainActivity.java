package com.yuong.textviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private AutoTextView tv_content;
    private String mText = "抢票时间：2019.08.20 14:55-2019.08.31 14:55";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        tv_content = findViewById(R.id.tv_content);
        tv_content.setText(mText);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_content.setText("抢票时间：2019.08.20 14:55-15:55");
            }
        });
    }
}
