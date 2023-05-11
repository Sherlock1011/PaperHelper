package com.example.paperhelper.model.bean;

import com.example.paperhelper.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule {
    private String time;
    private String content;
    private int duration;
    private String difficulty;
    private String notes;
    private boolean isFinished;
    private boolean isImportant;

    public Schedule(String content) {
        Date date = new Date();
        time = DateUtil.getDateString(date, "yyyy-MM-dd");
        this.content = content;
        duration = 1;
        isFinished = false;
        isImportant = false;
        notes = "";
        difficulty = "Easy";
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }
}
