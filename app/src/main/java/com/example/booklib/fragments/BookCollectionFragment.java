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
import com.example.booklib.adapters.BookCollectionAdapter;
import com.example.booklib.database.SQLiteDb;
import com.example.booklib.models.BookCollection;


public class BookCollectionFragment extends Fragment {

    private RecyclerView m_recyclerView;

    public BookCollectionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_collection, container, false);
        SQLiteDb sqLiteDb = new SQLiteDb(view.getContext());
        m_recyclerView = view.findViewById(R.id.f_collection_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        m_recyclerView.setLayoutManager(layoutManager);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setAdapter(new BookCollectionAdapter(view.getContext(), sqLiteDb.getBookCollections(), false));

        /*m_addCollectionBtnView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
            builder.setTitle("Создание коллекции");
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BookCollection bookCollection = new BookCollection();
                    bookCollection.setName(input.getText().toString());
                    insertDataInRecyclerView(bookCollection, view.getContext());
                }
            });
            builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        });*/
        return view;
    }


    public RecyclerView getM_recyclerView() {
        return m_recyclerView;
    }
}