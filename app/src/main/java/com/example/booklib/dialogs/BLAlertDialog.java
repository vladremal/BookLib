package com.example.booklib.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.booklib.R;
import com.example.booklib.adapters.SpinAdapter;
import com.example.booklib.database.SQLiteDb;
import com.example.booklib.dictonary.BLDictionary;
import com.example.booklib.models.Book;
import com.example.booklib.models.BookCollection;

import java.util.ArrayList;
import java.util.function.Consumer;
/**
 * This class is used to create and manage alert dialogs for the Book Library application.
 */
public final class BLAlertDialog {

    /**
     * Creates an alert dialog for creating a new book collection.
     *
     * @param context The context of the application.
     * @param consumer A consumer that accepts a BookCollection object when the dialog is completed.
     */

    public static void createBookCollectionDialog(Context context, Consumer<BookCollection> consumer) {
        if (context != null) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(new ContextThemeWrapper(context, androidx.appcompat.R.style.AlertDialog_AppCompat));
            builder.setTitle(R.string.alert_coll_create_title);
            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton(R.string.alert_coll_create_posBtn, (dialog, which) -> {
                if (!input.getText().toString().isEmpty()) {
                    BookCollection bookCollection = new BookCollection();
                    bookCollection.setName(input.getText().toString());
                    SQLiteDb sqLiteDb = new SQLiteDb(context);
                    sqLiteDb.insertBookCollection(bookCollection, arg -> {
                        if (arg) {
                            consumer.accept(bookCollection);
                        } else {
                            Toast.makeText(context, "Такая полка уже есть!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "Поле ввода не должно быть пустым!", Toast.LENGTH_SHORT).show();
                }

            });
            builder.setNegativeButton(R.string.alert_cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        }

    }

    /**
     * Creates an alert dialog for editing a book collection.
     *
     * @param context The context of the application.
     * @param lastCollectionName The name of the last collection.
     * @param consumer A consumer that accepts a String when the dialog is completed.
     */

    public static void editBookCollectionDialog(Context context, String lastCollectionName, Consumer<String> consumer) {
        if (context != null) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(new ContextThemeWrapper(context, androidx.appcompat.R.style.AlertDialog_AppCompat));
            builder.setTitle(R.string.alert_coll_change_title);
            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton(R.string.alert_coll_change_posBtn, (dialog, which) -> {
                SQLiteDb sqLiteDb = new SQLiteDb(context);
                sqLiteDb.updateBookCollection(lastCollectionName, input.getText().toString());
                consumer.accept(input.getText().toString());
            });
            builder.setNegativeButton(R.string.alert_cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        }
    }

    /**
     * Creates an alert dialog for deleting a book collection.
     *
     * @param context The context of the application.
     * @param collectionId The id of the collection to delete.
     * @param onComplete A runnable that is run when the dialog is completed.
     */

    public static void deleteBookCollectionDialog(Context context, int collectionId, Runnable onComplete) {
        if (context != null) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(new ContextThemeWrapper(context, androidx.appcompat.R.style.AlertDialog_AppCompat));
            builder.setTitle(R.string.alert_coll_delete_title);

            builder.setPositiveButton(R.string.alert_coll_delete_posBtn, (dialog, which) -> {
                SQLiteDb sqLiteDb = new SQLiteDb(context);
                sqLiteDb.deleteBookCollection(collectionId);
                onComplete.run();
            });
            builder.setNegativeButton(R.string.alert_cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        }
    }

    /**
     * Creates an alert dialog for linking multiple books to a collection.
     *
     * @param context The context of the application.
     * @param books The list of books to link.
     * @param runnable A runnable that is run when the dialog is completed.
     */

    public static void multiLinkBookDialog(Context context, ArrayList<Book> books, Runnable runnable) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, androidx.appcompat.R.style.AlertDialog_AppCompat));
            LayoutInflater li = LayoutInflater.from(context);
            View mView = li.inflate(R.layout.dialog_spinner, null);

            builder.setTitle(R.string.alert_lnbk_add_title);

            Spinner mSpinner = mView.findViewById(R.id.spinner);
            SQLiteDb sqLiteDb = new SQLiteDb(context);

            SpinAdapter<BookCollection> adapter = new SpinAdapter<>(context, android.R.layout.simple_spinner_item, sqLiteDb.getBookCollections());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);

            builder.setPositiveButton(R.string.alert_lnbk_add_posBtn, (dialog, which) -> {
                if (mSpinner.getSelectedItem() != null && mSpinner.getSelectedItem() instanceof BookCollection) {
                    int collectionId = ((BookCollection) mSpinner.getSelectedItem()).getId();
                    if (!books.isEmpty()) {
                        for (Book book : books) {
                            if (book.isSelected()) {
                                sqLiteDb.insertLinkedBook(book.getId(), collectionId, SQlResult -> {
                                });
                            }
                        }
                        runnable.run();
                        Toast.makeText(context, "Книги добавлены на полку!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton(R.string.alert_cancel, (dialog, which) -> {
                dialog.cancel();
            });

            builder.setView(mView);
            builder.show();

        }
    }

    /**
     * Creates an alert dialog for linking a single book to a collection.
     *
     * @param context The context of the application.
     * @param bookId The id of the book to link.
     */

    public static void linkBookDialog(Context context, int bookId) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, androidx.appcompat.R.style.AlertDialog_AppCompat));
            LayoutInflater li = LayoutInflater.from(context);
            View mView = li.inflate(R.layout.dialog_spinner, null);

            builder.setTitle(R.string.alert_lnbk_add_title);

            Spinner mSpinner = mView.findViewById(R.id.spinner);
            SQLiteDb sqLiteDb = new SQLiteDb(context);

            SpinAdapter<BookCollection> adapter = new SpinAdapter<>(context, android.R.layout.simple_spinner_item, sqLiteDb.getBookCollections());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);

            builder.setPositiveButton(R.string.alert_lnbk_add_posBtn, (dialog, which) -> {
                if (mSpinner.getSelectedItem() != null && mSpinner.getSelectedItem() instanceof BookCollection) {
                    sqLiteDb.insertLinkedBook(bookId, ((BookCollection) mSpinner.getSelectedItem()).getId(), SQLResult -> {
                        if (SQLResult) {
                            Toast.makeText(context, "Книга успешна добавлена на полку \"" + ((BookCollection) mSpinner.getSelectedItem()).getName() + "\"", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Такая книга уже есть на этой полке!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            builder.setNegativeButton(R.string.alert_cancel, (dialog, which) -> {
                dialog.cancel();
            });

            builder.setView(mView);
            builder.show();
        }
    }

    /**
     * Creates an alert dialog for deleting a linked book from a collection.
     *
     * @param context The context of the application.
     * @param bookId The id of the book to delete.
     * @param collectionId The id of the collection to delete the book from.
     * @param onComplete A runnable that is run when the dialog is completed.
     */

    public static void deleteLinkedBookDialog(Context context, int bookId, int collectionId, Runnable onComplete) {
        if (context != null) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(new ContextThemeWrapper(context, androidx.appcompat.R.style.AlertDialog_AppCompat));
            builder.setTitle(R.string.alert_lnbk_delete_title);

            builder.setPositiveButton(R.string.alert_lnbk_delete_posBtn, (dialog, which) -> {
                SQLiteDb sqLiteDb = new SQLiteDb(context);
                sqLiteDb.deleteLinkedBook(bookId, collectionId);
                onComplete.run();
            });
            builder.setNegativeButton(R.string.alert_cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        }
    }

    /**
     * Creates an alert dialog for deleting a book.
     *
     * @param context The context of the application.
     * @param bookId The id of the book to delete.
     * @param onComplete A runnable that is run when the dialog is completed.
     */

    public static void deleteBookDialog(Context context, int bookId, Runnable onComplete) {
        if (context != null) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(new ContextThemeWrapper(context, androidx.appcompat.R.style.AlertDialog_AppCompat));
            builder.setTitle(R.string.alert_book_delete_title);

            builder.setPositiveButton(R.string.alert_coll_delete_posBtn, (dialog, which) -> {
                SQLiteDb sqLiteDb = new SQLiteDb(context);
                sqLiteDb.deleteBook(bookId);
                onComplete.run();
            });
            builder.setNegativeButton(R.string.alert_cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        }
    }

    /**
     * Creates an alert dialog for updating the score of a book.
     *
     * @param context The context of the application.
     * @param bookId The id of the book to update the score of.
     * @param consumer A consumer that accepts a Float when the dialog is completed.
     */

    public static void updateBookScoreDialog(Context context, int bookId, Consumer<Float> consumer) {
        if (context != null) {
            SQLiteDb sqLiteDb = new SQLiteDb(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, androidx.appcompat.R.style.AlertDialog_AppCompat));
            builder.setTitle("Оценка книги");
            RatingBar ratingBar = new RatingBar(context);
            LinearLayout ratingBarContainer = new LinearLayout(context);

            LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.topMargin = 50;
            lp.leftMargin = 100;
            ratingBar.setLayoutParams(lp);
            ratingBar.setNumStars(5);
            ratingBar.setStepSize(0.5f);

            ratingBarContainer.addView(ratingBar);
            builder.setView(ratingBarContainer);

            builder.setPositiveButton("Обновить", (dialog, which) -> {
                sqLiteDb.updateBookScore(bookId, ratingBar.getRating());
                consumer.accept(ratingBar.getRating());
            });
            builder.setNegativeButton(R.string.alert_cancel, ((dialog, which) -> {
                dialog.cancel();
            }));

            builder.show();


        }
    }


}
