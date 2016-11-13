package com.kempasolutions.app.hoiimessanger;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Ganesh Poojary on 7/30/2016.
 */
public class Contacts {
    private String name;
    private String status;
    private String phone;
    private Bitmap pic;
    private Date lastSeen;

    public Contacts(String name, String status, Bitmap pic) {
        this.name = name;
        this.status = status;
        this.pic = pic;
        this.phone="1234567890";
    }

    public Contacts(String name, String status, String phone, Bitmap pic) {
        this.name = name;
        this.status = status;
        this.phone = phone;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
}
