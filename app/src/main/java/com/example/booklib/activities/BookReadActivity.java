package com.example.booklib.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booklib.R;
import com.example.booklib.database.SQLiteDb;
import com.example.booklib.dictonary.BLDictionary;
import com.example.booklib.models.Book;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class BookReadActivity extends AppCompatActivity {

    private Book currentBook;
    private PDFView pdfView;

    /**
     * This activity displays a book in a PDF format.
     * It retrieves the book name from the intent and sets it as the title of the toolbar.
     * It also sets up the PDFView to display the book.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_read);
        SQLiteDb sqLiteDb = new SQLiteDb(BookReadActivity.this);

        String bookName = getIntent().getStringExtra(BLDictionary.BOOK_NAME);
        if (!Objects.equals(bookName, "")) {
            currentBook = sqLiteDb.getBookByName(bookName);
        }

        pdfView = findViewById(R.id.pdf_viewer);

        if (currentBook != null) {
            pdfView.fromUri(Uri.parse(currentBook.getPath())).defaultPage(0).load();
        }

    }

}