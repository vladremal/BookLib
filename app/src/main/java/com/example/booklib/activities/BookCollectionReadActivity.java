package com.example.booklib.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklib.R;
import com.example.booklib.adapters.BookAdapter;
import com.example.booklib.database.SQLiteDb;
import com.example.booklib.dictonary.BLDictionary;
import com.google.android.material.appbar.MaterialToolbar;

/**
 * This activity displays the books in a specific collection.
 * It retrieves the collection name from the intent and sets it as the title of the toolbar.
 * It also sets up the RecyclerView to display the books in the collection.
 */

public class BookCollectionReadActivity extends AppCompatActivity {

    private RecyclerView m_recyclerView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_collection_read);
        String collectionName = getIntent().getStringExtra(BLDictionary.COLLECTION_NAME);

        m_recyclerView = findViewById(R.id.linked_book_rec_view);
        toolbar = findViewById(R.id.topOtherBar);
        toolbar.setTitle(collectionName);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        if (!collectionName.equals("")) {
            SQLiteDb sqLiteDb = new SQLiteDb(BookCollectionReadActivity.this);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BookCollectionReadActivity.this);
            m_recyclerView.setLayoutManager(layoutManager);
            m_recyclerView.setHasFixedSize(true);
            m_recyclerView.setAdapter(new BookAdapter(BookCollectionReadActivity.this, sqLiteDb.getLinkedBooks(collectionName), collectionName, false));
        }


    }
}