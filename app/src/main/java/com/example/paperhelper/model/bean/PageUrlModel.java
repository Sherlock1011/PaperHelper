package com.example.paperhelper.model.bean;

import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PageUrlModel {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String pageName;
    private String url;
    @Generated(hash = 1981832537)
    public PageUrlModel(Long id, String pageName, String url) {
        this.id = id;
        this.pageName = pageName;
        this.url = url;
    }
    @Generated(hash = 69140203)
    public PageUrlModel() {
    }

    public PageUrlModel(String pageName, String url) {
        this.pageName = pageName;
        this.url = url;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPageName() {
        return this.pageName;
    }
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
