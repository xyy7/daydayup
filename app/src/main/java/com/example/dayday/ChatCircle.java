package com.example.dayday;

import org.litepal.crud.LitePalSupport;

//圈子自身的信息
public class ChatCircle  extends LitePalSupport implements Comparable<ChatCircle>{
    String name;
    String cid;//标志位应该得是cid和adminId才行
    String adminId;//管理员账号，默认只有创始人才是管理员
    String uid;//隶属于哪个账号
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ChatCircle(String name, String cid, String adminId, String uid) {
        this.name = name;
        this.cid = cid;
        this.adminId = adminId;
        this.uid = uid;
    }

    @Override
    public int compareTo(ChatCircle o) {
        int a=Integer.parseInt(cid);
        int b=Integer.parseInt(o.cid);
        if(a>b){
            return 1;
        }
        return 0;
    }
}
