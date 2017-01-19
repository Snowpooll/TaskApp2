package jp.techacademy.kubota.satoru.taskapp;

import java.io.Serializable;
import java.util.Date;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by snowpool on 17/01/15.
 */

public class Task extends RealmObject implements Serializable{
    private String title;
    private String contents;
    private Date date;

    @PrimaryKey
    private int id;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
