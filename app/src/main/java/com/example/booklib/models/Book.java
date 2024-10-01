package com.example.booklib.models;

import androidx.annotation.NonNull;

public class Book {

    private int id;
    private String name;
    private boolean isSelected;
    private String path;
    private float score;

    /**
     * Конструктор по умолчанию
     */
    public Book(){

    }

    /**
     * Конструктор для создания объекта типа {@link Book}
     * @param name имя книги
     * @param path путь к книге
     * @param score рейтинг книги
     */
    public Book(String name, String path, float score) {
        this.name = name;
        this.path = path;
        this.score = score;
    }

    /**
     * Метод для получения ID книги
     * @return ID книги
     */
    public int getId() {
        return id;
    }

    /**
     * Метод для установки ID книги
     * @param id ID книги
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Метод для получения имени книги
     * @return имя книги
     */
    public String getName() {
        return name;
    }

    /**
     * Метод для установки имени книги
     * @param name имя книги
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Метод для проверки, выбрана ли книга
     * @return true, если книга выбрана, false - в противном случае
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Метод для установки статуса выбора книги
     * @param selected статус выбора книги
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Метод для получения пути к книге
     * @return путь к книге
     */
    public String getPath() {
        return path;
    }

    /**
     * Метод для установки пути к книге
     * @param path путь к книге
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Метод для получения рейтинга книги
     * @return рейтинг книги
     */
    public float getScore() {
        return score;
    }

    /**
     * Метод для установки рейтинга книги
     * @param score рейтинг книги
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Метод для преобразования объекта типа {@link Book} в строку
     * @return строковое представление объекта типа {@link Book}
     */
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}


