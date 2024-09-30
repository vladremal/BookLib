package com.example.booklib.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BookCollection{

    private int id;
    private String name;
    private boolean isSelected;
    private ArrayList<Book> booksList;
    public BookCollection(){

    }
    public BookCollection(int id ,String name, ArrayList<Book> booksList) {
        this.id = id;
        this.name = name;
        this.booksList = booksList;
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

    public ArrayList<Book> getBooksList() {
        return booksList;
    }

    public void setBooksList(ArrayList<Book> booksList) {
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }


}
