package com.example.dayday;

import org.litepal.crud.LitePalSupport;

public class Msg extends LitePalSupport implements Comparable<Msg> {
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SEND=1;
    int type;//这个只是用来辅助而已
    String content;
    long time;//转话成微秒进行排序
    String uid;
    String cid;
    String sendId;
    String adminId;

    public Msg(int type, String content, long time, String uid, String cid, String sendId, String adminId) {
        this.type = type;
        this.content = content;
        this.time = time;
        this.uid = uid;
        this.cid = cid;
        this.sendId = sendId;
        this.adminId = adminId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public Msg(int type, String content, long time, String uid, String cid, String sendId) {
        this.type = type;
        this.content = content;
        this.time = time;
        this.uid = uid;
        this.cid = cid;
        this.sendId = sendId;
    }

    public Msg( String content, long time, String uid, String cid) {
        this.type = 0;
        this.content = content;
        this.time = time;
        this.uid = uid;
        this.cid = cid;
    }

    public static int getTypeReceived() {
        return TYPE_RECEIVED;
    }

    public static int getTypeSend() {
        return TYPE_SEND;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(String a) {
        if(getUid().equals(a)){
            setType(Msg.TYPE_SEND);
        }else{
            setType(Msg.TYPE_RECEIVED);
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Msg(int type, String content, long time, String uid, String cid) {
        this.type = type;
        this.content = content;
        this.time = time;
        this.uid = uid;
        this.cid = cid;
    }

    @Override
    public int compareTo(Msg o) {
        if(time<o.time){
            return 1;
        }
        return 0;
    }
}
