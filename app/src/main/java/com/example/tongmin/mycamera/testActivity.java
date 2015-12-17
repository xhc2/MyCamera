package com.example.tongmin.mycamera;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class testActivity extends AppCompatActivity {

    MySeekBar seekBar;
    Button bt;
    int current = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_test);
        seekBar = (MySeekBar) findViewById(R.id.seekbar);
        bt = (Button)findViewById(R.id.button);
        seekBar.setMax(100);
        seekBar.setChangeLinstener(new MySeekBar.OnChangeLinstener() {
            @Override
            public void changeValue(float ratio) {
                Log.e("xhc","radio"+ratio);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current += 10;
                seekBar.setCurrent(current);
            }
        });
    }

}
