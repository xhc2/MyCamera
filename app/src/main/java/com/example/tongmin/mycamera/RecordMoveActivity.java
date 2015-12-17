package com.example.tongmin.mycamera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordMoveActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private GridView gridView;
    private List<File> listFile;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_record_move);
        findViewById();
        initView();
        new LoadFileThread().start();
    }

    private void findViewById() {
        gridView = (GridView) findViewById(R.id.gridview);
    }

    private void initView() {
        listFile = new ArrayList<File>();
        handler = new MyHandler(gridView,this);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(RecordMoveActivity.this,PlayVideoActivity.class);
        intent.putExtra("position", position);
        ArrayList<String> listFilePath = new ArrayList<String>();

        for(File file : listFile){
            listFilePath.add(file.getAbsolutePath());
        }

        intent.putStringArrayListExtra("listPath", listFilePath);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    static class MyHandler extends Handler {
        GridView gridView;
        Context context;
        FileAdapter fileAdapter;

        MyHandler(GridView gridView, Context context) {
            this.gridView = gridView;
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<File> list = (List<File>)msg.obj;
            fileAdapter = new FileAdapter(list,context);
            gridView.setAdapter(fileAdapter);
        }
    }

    class LoadFileThread extends Thread {

        @Override
        public void run() {
            super.run();
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "Camera");
//            if (mediaStorageDir.isDirectory()) {
                for (File file : mediaStorageDir.listFiles()) {
//                    if (!file.isDirectory()) {
                        if (file.getName().startsWith("IPEARL_VID_")) {
                            listFile.add(file);
                        }
//                    }
//                }
            }
            Message msg = new Message();
            msg.obj = listFile;
            handler.sendMessage(msg);
        }
    }
}
