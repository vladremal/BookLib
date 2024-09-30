package com.example.booklib.activities;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booklib.R;
import com.example.booklib.database.SQLiteDb;
import com.example.booklib.dictonary.BLDictionary;
import com.example.booklib.models.Book;
import com.github.barteksc.pdfviewer.PDFView;

public class BookReadActivity extends AppCompatActivity {

    private Book currentBook;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_read);
        SQLiteDb sqLiteDb = new SQLiteDb(BookReadActivity.this);

        int bookId = getIntent().getIntExtra(BLDictionary.BOOK_ID, -1);
        if (bookId != -1) {
            currentBook = sqLiteDb.getBookById(bookId);
        }

        pdfView = findViewById(R.id.pdf_viewer);

        if (currentBook != null) {
            pdfView.fromUri(Uri.parse(currentBook.getPath())).defaultPage(0).load();
        }

    }
}