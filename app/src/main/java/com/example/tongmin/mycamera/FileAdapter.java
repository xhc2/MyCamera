package com.example.tongmin.mycamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

/**
 * Created by TongMin on 2015/12/16.
 */
public class FileAdapter extends BaseAdapter {

    private List<File> listFile;
    private ViewHolder holder;
    private Context context;
    private LayoutInflater inflater;

    public FileAdapter(List<File> listFile, Context context) {
        this.listFile = listFile;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listFile.size();
    }

    @Override
    public Object getItem(int position) {
        return listFile.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.file_item, parent, false);
            holder.img = (ImageView) convertView.findViewById(R.id.img_file);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File file = listFile.get(position);
        ImageLoader.getInstance().displayImage("file:///mnt/sdcard/"+Environment.DIRECTORY_DCIM+"/"+"Camera/"+file.getName(),holder.img);
        return convertView;
    }
    class ViewHolder {
        ImageView img;
    }
}
