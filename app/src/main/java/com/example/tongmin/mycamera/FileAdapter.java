package com.example.tongmin.mycamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
//        holder.img.setImageBitmap(getVideoThumbnail(file.getAbsolutePath(),50,50,MediaStore.Video.Thumbnails.MINI_KIND));
        /*ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND)*/
        return convertView;
    }
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                           int kind)
    {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
//        System.out.println("w" + bitmap.getWidth());
//        System.out.println("h" + bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
    class ViewHolder {
        ImageView img;
    }
}
