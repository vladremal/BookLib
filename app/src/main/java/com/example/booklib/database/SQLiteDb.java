package com.example.booklib.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.booklib.models.Book;
import com.example.booklib.models.BookCollection;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SQLiteDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookLib.db";
    private static final int SCHEMA = 1;

    private static final String COLLECTIONS_TABLE_NAME = "book_collection";
    private static final String BOOKS_TABLE_NAME = "book";
    private static final String LINKING_TABLE_NAME = "bookCollectionLink";

    public static final String COLLECTION_ID_COLUMN = "collection_id";
    public static final String COLLECTION_NAME_COLUMN = "collection_name";
    public static final String BOOK_ID_COLUMN = "book_id";
    public static final String BOOK_NAME_COLUMN = "book_name";
    public static final String BOOK_PATH_COLUMN = "book_path";
    public static final String BOOK_SCORE_COLUMN = "book_score";
    public static final String LINKING_LINK_ID_COLUMN = "bcl_id";
    public static final String LINKING_BOOK_ID_COLUMN = "bcl_book_id";
    public static final String LINKING_COLLECTION_ID_COLUMN = "bcl_collection_id";

    /**
     * Класс для работы с базой данных SQLite
     */
    public SQLiteDb(@Nullable Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    /**
     * Данный метод вызывается при создании БД
     *
     * @param db объект типа {@link SQLiteDatabase}.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + COLLECTIONS_TABLE_NAME + " (" + COLLECTION_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + COLLECTION_NAME_COLUMN + " VARCHAR(255) UNIQUE NOT NULL);");
/*        db.execSQL("INSERT INTO " + COLLECTIONS_TABLE_NAME + " (" + COLLECTION_NAME_COLUMN + ") " + "VALUES" + " (" + "'Читаю сейчас'" + "), "
                + "(" + "'Избранное'" + "), " + "(" + "'Хочу прочитать'" + ")");*/

        db.execSQL("CREATE TABLE " + BOOKS_TABLE_NAME + " (" + BOOK_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + BOOK_NAME_COLUMN + " VARCHAR(255) UNIQUE NOT NULL," + BOOK_PATH_COLUMN + " VARCHAR(500) NOT NULL,"
                + BOOK_SCORE_COLUMN + " INTEGER NOT NULL, " + "CHECK " + "(" + BOOK_SCORE_COLUMN + " >= 1 AND " + BOOK_SCORE_COLUMN + " <= 10));");

        db.execSQL("CREATE TABLE " + LINKING_TABLE_NAME + "(" + LINKING_LINK_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + LINKING_BOOK_ID_COLUMN + " INTEGER NOT NULL, " + LINKING_COLLECTION_ID_COLUMN + " INTEGER NOT NULL, "
                + "FOREIGN KEY (" + LINKING_BOOK_ID_COLUMN + ") " + "REFERENCES " + BOOKS_TABLE_NAME + "(" + BOOK_ID_COLUMN + ") ON DELETE CASCADE, "
                + "FOREIGN KEY (" + LINKING_COLLECTION_ID_COLUMN + ") " + "REFERENCES " + COLLECTIONS_TABLE_NAME + "(" + COLLECTION_ID_COLUMN + ") ON DELETE CASCADE,"
                + " UNIQUE(" + LINKING_BOOK_ID_COLUMN + "," + LINKING_COLLECTION_ID_COLUMN + "));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Конфигурация БД
     * @param db The database.
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
        super.onConfigure(db);
    }

    //region BookCollectionMethods

    /**
     * Возвращает коллекцию типа {@link BookCollection} из бд
     *
     * @return ArrayList of {@link BookCollection}
     */
    public ArrayList<BookCollection> getBookCollections() {
        ArrayList<BookCollection> bookCollections = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + COLLECTIONS_TABLE_NAME, null);
        if (result.moveToPosition(0)) {
            do {
                int id = result.getInt(0);
                String name = result.getString(1);
                BookCollection bookCollection = new BookCollection();
                bookCollection.setSelected(false);
                bookCollection.setName(name);
                bookCollection.setId(id);
                bookCollections.add(bookCollection);
            } while (result.moveToNext());
        }
        result.close();
        return bookCollections;
    }

    /**
     * Возвращает ID коллекции по ее имени
     * @param collectionName имя коллекции
     * @return ID коллекции
     */

    public int getCollectionIdByName(String collectionName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + COLLECTIONS_TABLE_NAME + " WHERE " + COLLECTION_NAME_COLUMN + " =? ", new String[]{collectionName});
        if (result.moveToPosition(0)) {
            do {
                return result.getInt(0);

            } while (result.moveToNext());
        }
        result.close();
        return -1;
    }

    /**
     * Добавляет новую коллекцию типа {@link BookCollection} в бд
     *
     * @param bookCollection коллекция для добавления в бд
     **/
    public void insertBookCollection(BookCollection bookCollection, Consumer<Boolean> consumer) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLLECTION_NAME_COLUMN, bookCollection.getName());
        long result = sqLiteDatabase.insert(COLLECTIONS_TABLE_NAME, null, contentValues);
        if (result != (-1)) {
            consumer.accept(true);
        } else {
            consumer.accept(false);
        }
        sqLiteDatabase.close();
    }

    /**
     * Обновляет коллекцию книг типа {@link BookCollection} в бд
     *
     * @param lastName текущее имя колекции
     * @param newName  новое имя колекции
     */

    public void updateBookCollection(String lastName, String newName, Consumer<Boolean> consumer) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLLECTION_NAME_COLUMN, newName);

        int exstColID = getCollectionIdByName(newName);
        if (exstColID!= (-1)) {
            consumer.accept(false);
            return;
        } else {
            consumer.accept(true);
            sqLiteDatabase.update(COLLECTIONS_TABLE_NAME, contentValues, COLLECTION_NAME_COLUMN + "=?", new String[]{lastName});
            sqLiteDatabase.close();
        }
    }

    /**
     * Удаляет записи в бд, соответсвующие переданным объектам в {@link ArrayList}
     *
     * @param bookCollections коллекция {@link ArrayList}<{@link BookCollection}> для удаления
     * @param runnable интерфейс для выполненения действий после завершения работы метода
     */

    public void multiDeleteBookCollection(ArrayList<BookCollection> bookCollections, Runnable runnable) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (BookCollection bookCollection : bookCollections){
            if (bookCollection.isSelected()) {
                sqLiteDatabase.delete(COLLECTIONS_TABLE_NAME, COLLECTION_NAME_COLUMN + "=?", new String[]{bookCollection.getName()});
            }
        }
        sqLiteDatabase.close();
        runnable.run();
    }

    /**
     * Удаляет запись в БД по имени колекции
     * @param colName имя коллекции
     */

    public void deleteBookCollection(String colName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(COLLECTIONS_TABLE_NAME, COLLECTION_NAME_COLUMN + "=?", new String[]{colName});
        sqLiteDatabase.close();
    }

    //endregion

    //region BookMethods

    /**
     * Возвращает коллекцию книг
     * @return ArrayList типа {@link Book}
     */
    public ArrayList<Book> getBooks() {
        ArrayList<Book> books = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME, null);
        if (result.moveToPosition(0)) {
            do {
                int id = result.getInt(0);
                String name = result.getString(1);
                String path = result.getString(2);
                int score = result.getInt(3);
                Book book = new Book();
                book.setId(id);
                book.setName(name);
                book.setPath(path);
                book.setScore(score);
                books.add(book);

            } while (result.moveToNext());
        }
        result.close();
        return books;
    }

    /**
     * Возвращает книгу по имени
     * @param bookName имя книги
     * @return объект типа {@link Book}
     */
    public Book getBookByName(String bookName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOK_NAME_COLUMN + " =? ", new String[]{bookName});
        Book book;
        if (result.moveToPosition(0)) {
            book = new Book();
            do {
                int id = result.getInt(0);
                String name = result.getString(1);
                String path = result.getString(2);
                int score = result.getInt(3);
                book.setId(id);
                book.setName(name);
                book.setPath(path);
                book.setScore(score);
                return book;

            } while (result.moveToNext());
        }
        result.close();
        return null;
    }

    /**
     * Возвращает книгу по ID
     * @param bookId ID книги
     * @return объект типа {@link Book}
     */


    public Book getBookById(int bookId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOK_ID_COLUMN + " =? ", new String[]{String.valueOf(bookId)});
        Book book;
        if (result.moveToPosition(0)) {
            book = new Book();
            do {
                int id = result.getInt(0);
                String name = result.getString(1);
                String path = result.getString(2);
                int score = result.getInt(3);
                book.setId(id);
                book.setName(name);
                book.setPath(path);
                book.setScore(score);
                return book;

            } while (result.moveToNext());
        }
        result.close();
        return null;
    }

    /**
     * Возвращает ID книги по ее имени
     * @param bookName имя книги
     * @return ID книги
     */

    private int getBookIdByName(String bookName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + BOOKS_TABLE_NAME + " WHERE " + BOOK_NAME_COLUMN + " =? ", new String[]{bookName});
        if (result.moveToPosition(0)) {
            do {
                return result.getInt(0);

            } while (result.moveToNext());
        }
        result.close();
        return -1;
    }


    /**
     * Добавляет в БД запись объекта типа {@link Book}
     * @param book объект типа {@link Book}
     * @param consumer интерфейс, типизированный классом {@link Boolean} для выполнения действий после завершения работы метода
     */
    public void insertBook(Book book, Consumer<Boolean> consumer) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOK_NAME_COLUMN, book.getName());
        contentValues.put(BOOK_PATH_COLUMN, book.getPath());
        contentValues.put(BOOK_SCORE_COLUMN, book.getScore());

        long result = sqLiteDatabase.insert(BOOKS_TABLE_NAME, null, contentValues);
        if (result != (-1)) {
            consumer.accept(true);
        } else {
            consumer.accept(false);
        }
        sqLiteDatabase.close();
    }

    /**
     * Обновляет рейтинг книги {@link Book}
     *
     * @param bookName имя книги
     * @param rating   новый рейтинг
     */
    public void updateBookScore(String bookName, float rating) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOK_SCORE_COLUMN, rating);

        sqLiteDatabase.update(BOOKS_TABLE_NAME, contentValues, BOOK_NAME_COLUMN + "=?", new String[]{bookName});
        sqLiteDatabase.close();
    }

    /**
     * Удаляет книгу {@link Book} по ID
     * @param bookName имя книги {@link Book}
     */
    public void deleteBook(String bookName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(BOOKS_TABLE_NAME, BOOK_NAME_COLUMN + "=?", new String[]{bookName});
        sqLiteDatabase.close();
    }

    /**
     * Удаляет книги {@link Book}, переданные в коллекции {@link ArrayList}
     * @param books коллекция книг {@link ArrayList}<{@link Book}>
     * @param runnable интерфейс для выполнения действий после завершения работы метода
     */
    public void multiDeleteBook(ArrayList<Book> books, Runnable runnable){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (Book book : books){
            if (book.isSelected()) {
                sqLiteDatabase.delete(BOOKS_TABLE_NAME, BOOK_NAME_COLUMN + "=?", new String[]{book.getName()});
            }
        }
        runnable.run();
        sqLiteDatabase.close();
    }

    //endregion

    //region BookCollectionLink

    /**
     * Получает слинкованные с полкой книги
     * @param colName имя коллекции
     * @return {@link ArrayList}<{@link Book}>
     */
    public ArrayList<Book> getLinkedBooks(String colName) {
        ArrayList<Book> books = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int colId = getCollectionIdByName(colName);
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + LINKING_TABLE_NAME + " WHERE " + LINKING_COLLECTION_ID_COLUMN + " =? ", new String[]{String.valueOf(colId)});

        if (result.moveToPosition(0)) {
            do {
                int bookId = result.getInt(1);
                Book book = getBookById(bookId);
                books.add(book);

            } while (result.moveToNext());
        }
        result.close();
        return books;

    }
    /**
     * Метод для добавления связи между книгой и коллекцией
     * @param bookName имя книги
     * @param colName имя коллекции
     * @param consumer интерфейс для выполнения действий после завершения работы метода
     */

    public void insertLinkedBook(String bookName, String colName, Consumer<Boolean> consumer) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int colId = getCollectionIdByName(colName);
        int bookId = getBookIdByName(bookName);
        ContentValues contentValues = new ContentValues();
        contentValues.put(LINKING_BOOK_ID_COLUMN, bookId);
        contentValues.put(LINKING_COLLECTION_ID_COLUMN, colId);

        long result = sqLiteDatabase.insert(LINKING_TABLE_NAME, null, contentValues);
        if (result != (-1)) {
            consumer.accept(true);
        } else {
            consumer.accept(false);
        }
        sqLiteDatabase.close();
    }

    /**
     * Метод для удаления связи между книгой и коллекцией
     * @param bookName имя книги
     * @param colName имя коллекции
     */

    public void deleteLinkedBook(String bookName, String colName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int colId = getCollectionIdByName(colName);
        int bookId = getBookIdByName(bookName);
        sqLiteDatabase.delete(LINKING_TABLE_NAME, LINKING_BOOK_ID_COLUMN + "=?" + " AND " + LINKING_COLLECTION_ID_COLUMN + "=?", new String[]{String.valueOf(bookId), String.valueOf(colId)});
        sqLiteDatabase.close();
    }


    //endregion
}
