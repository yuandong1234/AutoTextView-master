package com.yuong.textviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private AutoTextView autoTextView;
    private String mText = "抢票时间：2019.08.20 14:55-2019.08.31 14:55";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        autoTextView = findViewById(R.id.autoTextView);
        //autoTextView.setText(mText);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoTextView.setText(mText);
            }
        },5000);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoTextView.setText("It raked in more than 675 million yuan on Aug. 3-4, contributing nearly 65 percent to the total box office revenue of the outgoing weekend, said the network.");
            }
        });
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
