package com.example.imagedownload;

import android.widget.ImageView;

public class GameItem {
    private ImageView imageView;
    private String tag;

    public GameItem(ImageView imageView, String tag) {
        this.imageView = imageView;
        this.tag = tag;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getTag() {
        return tag;
    }
}
