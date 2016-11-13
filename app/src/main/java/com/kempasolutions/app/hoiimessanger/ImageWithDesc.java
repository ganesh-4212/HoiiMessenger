package com.kempasolutions.app.hoiimessanger;

import android.graphics.Bitmap;

/**
 * Created by Archana on 8/8/2016.
 */

public class ImageWithDesc {
    private Bitmap image;
    private String desc;

    public ImageWithDesc(Bitmap image, String desc) {
        this.image = Bitmap.createBitmap(image);
        this.desc = desc;
    }

    public ImageWithDesc(Bitmap image) {
        this.image = Bitmap.createBitmap(image);
        desc = "";
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
