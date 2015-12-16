package com.example.tongmin.mycamera;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class PlayVideoActivity extends AppCompatActivity {

    private MyVideoView videoView;
    private String path;
//    private Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_play_video);
        videoView = (MyVideoView)findViewById(R.id.videoview);
//        bt = (Button)findViewById(R.id.bt);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        videoView.setVideoPath(path);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        videoView.start();
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoView.getLayoutParams();
//                params.width =  metrics.widthPixels;
//                params.height = metrics.heightPixels;
//                params.leftMargin = 0;
//                videoView.setLayoutParams(params);
//                //试试能不能全屏
//
//            }
//        });
    }



    @Override
    protected void onResume() {
        super.onResume();

    }
}
