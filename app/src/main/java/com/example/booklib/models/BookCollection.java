package com.example.booklib.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BookCollection{

    private int id;
    private String name;
    private boolean isSelected;
    private ArrayList<Book> booksList;

    /**
     * Конструктор по умолчанию
     */
    public BookCollection(){

    }

    /**
     * Конструктор для создания объекта типа {@link BookCollection}
     * @param id ID коллекции
     * @param name имя коллекции
     * @param booksList список книг в коллекции
     */
    public BookCollection(int id ,String name, ArrayList<Book> booksList) {
        this.id = id;
        this.name = name;
        this.booksList = booksList;
    }

    /**
     * Метод для получения ID коллекции
     * @return ID коллекции
     */
    public int getId() {
        return id;
    }

    /**
     * Метод для установки ID коллекции
     * @param id ID коллекции
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Метод для получения имени коллекции
     * @return имя коллекции
     */
    public String getName() {
        return name;
    }

    /**
     * Метод для установки имени коллекции
     * @param name имя коллекции
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Метод для проверки, выбрана ли коллекция
     * @return true, если коллекция выбрана, false - в противном случае
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Метод для установки статуса выбора коллекции
     * @param selected статус выбора коллекции
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Метод для получения списка книг в коллекции
     * @return список книг в коллекции
     */
    public ArrayList<Book> getBooksList() {
        return booksList;
    }

    /**
     * Метод для установки списка книг в коллекции
     * @param booksList список книг в коллекции
     */
    public void setBooksList(ArrayList<Book> booksList) {
        this.booksList = booksList;
    }

    /**
     * Метод для преобразования объекта типа {@link BookCollection} в строку
     * @return строковое представление объекта типа {@link BookCollection}
     */
    @NonNull
    @Override
    public String toString() {
        return name;
    }


}
