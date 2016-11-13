package com.kempasolutions.app.hoiimessanger;

import android.graphics.Bitmap;

/**
 * Created by Ganesh Poojary on 7/29/2016.
 */
public class RecentChat {
    private String chatTitle;
    private String recentMsg;
    private String lastMsgTime;
    private String prflPic;

    public RecentChat(String chatTitle, String recentMsg, String lastMsgTime) {
        this.chatTitle = chatTitle;
        this.recentMsg = recentMsg;
        this.lastMsgTime = lastMsgTime;
        this.prflPic = null;
    }

    public RecentChat(String chatTitle, String recentMsg, String lastMsgTime, String prflPic) {
        this.chatTitle = chatTitle;
        this.recentMsg = recentMsg;
        this.lastMsgTime = lastMsgTime;
        this.prflPic = prflPic;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public String getRecentMsg() {
        return recentMsg;
    }

    public void setRecentMsg(String recentMsg) {
        this.recentMsg = recentMsg;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public String getPrflPic() {
        return prflPic;
    }

    public void setPrflPic(String prflPic) {
        this.prflPic = prflPic;
    }
}
