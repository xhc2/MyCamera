package com.example.tongmin.mycamera;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class PlayVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_play_video);
        videoView = (VideoView)findViewById(R.id.videoview);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        videoView.setVideoPath(path);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT
        );
        videoView.setLayoutParams(lp);
        videoView.start();
    }
}
