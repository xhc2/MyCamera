package com.example.tongmin.mycamera;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlayVideoActivity extends AppCompatActivity implements View.OnClickListener,
        MediaPlayer.OnCompletionListener, MySeekBar.OnChangeLinstener {

    private MyVideoView videoView;
    private int position;
    private ImageButton btLastVideo, btPlayVideo, btNextVideo;
    private MySeekBar seekBar;
    private TextView tvWanCheng, tvTime, tvAllTime;
    private List<String> listPath;
    private CurrentTimeThread thread;
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvTime.setText(sdf.format(videoView.getCurrentPosition()));
            seekBar.setCurrent(videoView.getCurrentPosition());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_play_video);
        findViewById();
        initView();

    }

    private void findViewById() {
        videoView = (MyVideoView) findViewById(R.id.videoview);
        btLastVideo = (ImageButton) findViewById(R.id.last);
        btPlayVideo = (ImageButton) findViewById(R.id.play);
        btNextVideo = (ImageButton) findViewById(R.id.next);

        seekBar = (MySeekBar) findViewById(R.id.seekbar);
        tvWanCheng = (TextView) findViewById(R.id.wancheng);
        tvTime = (TextView) findViewById(R.id.time);
        tvAllTime = (TextView) findViewById(R.id.all_time);
        btNextVideo.setRotation(-90);
        btLastVideo.setRotation(-90);
        btPlayVideo.setRotation(-90);

        tvWanCheng.setRotation(-90);
        tvTime.setRotation(-90);
        tvAllTime.setRotation(-90);

        btLastVideo.setOnClickListener(this);
        btPlayVideo.setOnClickListener(this);
        btNextVideo.setOnClickListener(this);

    }

    @Override
    public void changeValue(float ratio) {
        if(ratio == -1){return;}
        videoView.seekTo((int) (videoView.getDuration() * ratio));
        tvTime.setText(sdf.format(videoView.getCurrentPosition()));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }


    private void initView() {

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        listPath = intent.getStringArrayListExtra("listPath");
        videoView.setVideoPath(listPath.get(position));
        videoView.setOnCompletionListener(this);
        seekBar.setChangeLinstener(this);
        play();
    }

    private void next() {
        if (position < listPath.size() - 1) {
            position++;
            videoView.setVideoPath(listPath.get(position));
            play();

        } else {
            pause();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null) {
            thread.stop_();
        }
    }

    private void pause() {
        videoView.pause();
        btPlayVideo.setImageResource(R.drawable.playvideo);
    }

    private void play() {
        if (thread != null) {
            thread.stop_();
        }
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                tvAllTime.setText(sdf.format(videoView.getDuration()));
                seekBar.setMax(videoView.getDuration());
            }
        });
        btPlayVideo.setImageResource(R.drawable.stopvideo);
        thread = new CurrentTimeThread();
        thread.start();

    }

    private void last() {
        if (position > 0) {
            position--;
        }
        videoView.setVideoPath(listPath.get(position));
        play();
    }

    class CurrentTimeThread extends Thread {
        private boolean flag = true;

        void stop_() {
            this.flag = false;
        }

        @Override
        public void run() {
            super.run();
            while (flag) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                handler.sendEmptyMessage(1);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.last:
                last();
                break;
            case R.id.play:
                if (videoView.isPlaying()) {
                    pause();
                } else {
                    play();
                }
                break;
            case R.id.next:
                next();
                break;
        }
    }
}
