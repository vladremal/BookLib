package com.example.booklib.models;

import androidx.annotation.NonNull;

public class Book {

    private int id;
    private String name;
    private boolean isSelected;
    private String path;
    private float score;
    public Book(){

    }
    public Book(String name, String path, float score) {
        this.name = name;
        this.path = path;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
