package com.example.booklib.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklib.R;
import com.example.booklib.activities.BookCollectionReadActivity;
import com.example.booklib.activities.BookReadActivity;
import com.example.booklib.activities.MainActivity;
import com.example.booklib.database.SQLiteDb;
import com.example.booklib.dialogs.BLAlertDialog;
import com.example.booklib.dictonary.BLDictionary;
import com.example.booklib.models.Book;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Book> books;
    private int collectionId = -1;
    private boolean multiChoise;

    public BookAdapter(Context context, ArrayList<Book> books, boolean multiChoise) {
        this.context = context;
        this.books = books;
        this.multiChoise = multiChoise;
    }

    public BookAdapter(Context context, ArrayList<Book> books, int collectionId, boolean multiChoise) {
        this.context = context;
        this.books = books;
        this.collectionId = collectionId;
        this.multiChoise = multiChoise;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_book, parent, false);
        return new BookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        Book book = books.get(position);

        if (multiChoise){
            holder.bookSettingsButton.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.bookSettingsButton.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
            books.get(position).setSelected(false);
        }

        holder.bookCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookReadActivity.class);
            intent.putExtra(BLDictionary.BOOK_ID, book.getId());
            context.startActivity(intent);
        });

        holder.bookNameView.setText(book.getName());
        holder.bookNameView.setSelected(true);
        holder.bookScoreView.setText(String.valueOf(book.getScore()));
        holder.bookSettingsButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.popup_book_settings);
            if (context instanceof MainActivity) {
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.pu_bk_link) {
                        BLAlertDialog.linkBookDialog(context, book.getId());
                        return true;
                    } else if (item.getItemId() == R.id.pu_bk_delete) {
                        BLAlertDialog.deleteBookDialog(context, book.getId(), () -> {
                            books.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, books.size());
                        });
                        return true;
                    } else if (item.getItemId() == R.id.pu_update_score){
                        BLAlertDialog.updateBookScoreDialog(context, book.getId(), newScore -> {
                            holder.bookScoreView.setText(String.valueOf(newScore));
                        });
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            } else if (context instanceof BookCollectionReadActivity) {
                popupMenu.getMenu().removeItem(R.id.pu_bk_link);
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.pu_bk_delete) {
                        if (collectionId != -1) {
                            BLAlertDialog.deleteLinkedBookDialog(context, book.getId(), collectionId, () -> {
                                books.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, books.size());
                            });
                        }
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            }
        });
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            books.get(position).setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView bookCardView;
        TextView bookNameView, bookScoreView;
        ImageButton bookSettingsButton;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCardView = itemView.findViewById(R.id.cd_book_card);
            bookNameView = itemView.findViewById(R.id.cd_book_name);
            bookScoreView = itemView.findViewById(R.id.cd_book_score_txt);
            bookSettingsButton = itemView.findViewById(R.id.cd_book_settings_btn);
            checkBox = itemView.findViewById(R.id.cd_book_checkbox);
        }
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public boolean isMultiChoise() {
        return multiChoise;
    }
}
