package com.example.booklib.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booklib.R;
import com.example.booklib.adapters.BookAdapter;
import com.example.booklib.adapters.BookCollectionAdapter;
import com.example.booklib.database.SQLiteDb;
import com.example.booklib.models.Book;
import com.example.booklib.models.BookCollection;


public class BookFragment extends Fragment {


    private RecyclerView m_recyclerView;

    public BookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        SQLiteDb sqLiteDb = new SQLiteDb(view.getContext());
        m_recyclerView = view.findViewById(R.id.f_book_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        m_recyclerView.setLayoutManager(layoutManager);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setAdapter(new BookAdapter(view.getContext(), sqLiteDb.getBooks(), false));

        return view;
    }

    public RecyclerView getM_recyclerView() {
        return m_recyclerView;
    }
}