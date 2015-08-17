package com.fcu.imagepicker;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.fcu.R;

/**
 * PhotoWall中GridView的適配器
 */

public class CloudPhotoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CloudPhotoItem> imagePathList = null;


    //記錄是否被選擇
    private SparseBooleanArray selectionMap;

    public CloudPhotoAdapter(Context context, ArrayList<CloudPhotoItem> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;
        selectionMap = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return imagePathList == null ? 0 : imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String filePath = imagePathList.get(position).getpPath(); 
        
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cloud_photo_item, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.cloud_photo_item_photo);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cloud_photo_item_cb);
            holder.textView = (TextView) convertView.findViewById(R.id.cloud_photo_item_text);
            convertView.setTag(holder);            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //tag的key必須使用id的方式定義以保證唯一，否則會出現IllegalArgumentException.
        holder.checkBox.setTag(R.id.tag_first, position);
        holder.checkBox.setTag(R.id.tag_second, holder.imageView);
    
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Integer position = (Integer) buttonView.getTag(R.id.tag_first);
                ImageView image = (ImageView) buttonView.getTag(R.id.tag_second);

                selectionMap.put(position, isChecked);
                if (isChecked) {
                    image.setColorFilter(context.getResources().getColor(R.color.image_checked_bg));
                } else {
                    image.setColorFilter(null);
                }
            }
        });

        holder.checkBox.setChecked(selectionMap.get(position));

        Glide.with(context)
	    .load(filePath)
	    .override(150, 150)
	    .centerCrop()
	    .placeholder(R.drawable.empty_photo)
	    .error(R.drawable.empty_photo)
	    .into(holder.imageView);
        //holder.imageView.setTag(filePath);
        //取得拍攝日期
        holder.textView.setText( imagePathList.get(position).getTakeDate() );

//	        loader.loadImage(4, filePath, holder.imageView);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        TextView textView;
    }

    public SparseBooleanArray getSelectionMap() {
        return selectionMap;
    }

    public void clearSelectionMap() {
        selectionMap.clear();
    }
}