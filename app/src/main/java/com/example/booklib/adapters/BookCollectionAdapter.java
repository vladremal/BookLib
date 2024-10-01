package com.example.booklib.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklib.R;
import com.example.booklib.activities.BookCollectionReadActivity;
import com.example.booklib.dialogs.BLAlertDialog;
import com.example.booklib.dictonary.BLDictionary;
import com.example.booklib.models.BookCollection;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class BookCollectionAdapter extends RecyclerView.Adapter<BookCollectionAdapter.ViewHolder>{
    private Context context;
    private ArrayList<BookCollection> bookCollections;

    private boolean multiChoise;
    //private final ArrayList<Pair<BookCollection, Integer>> selectedBookCollections = new ArrayList<>();


    public BookCollectionAdapter(Context context, ArrayList<BookCollection> bookCollections, boolean multiChoise) {
        this.context = context;
        this.bookCollections = bookCollections;
        this.multiChoise = multiChoise;
    }

    @NonNull
    @Override
    public BookCollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_book_collection, parent, false);
        return new ViewHolder(view);
    }





    @Override
    public void onBindViewHolder(@NonNull BookCollectionAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BookCollection bookCollection = bookCollections.get(position);

        if (multiChoise){
            holder.collectionSettingsButton.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.collectionSettingsButton.setVisibility(View.VISIBLE);
            holder.checkBox.setVisibility(View.GONE);
            bookCollections.get(position).setSelected(false);
        }


        holder.collectionNameView.setText(bookCollection.getName());
        //holder.collectionNameView.setSelected(true);
        holder.collectionCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookCollectionReadActivity.class);
            intent.putExtra(BLDictionary.COLLECTION_NAME, holder.collectionNameView.getText().toString());
            context.startActivity(intent);
        });


        /*holder.collectionCardView.setOnLongClickListener(v -> {
            if (bookCollection.isSelected()) {
                holder.collectionCardView.setStrokeColor(Color.TRANSPARENT);
                bookCollections.get(position).setSelected(false);
            } else {
                holder.collectionCardView.setStrokeColor(context.getColor(R.color.purple));
                bookCollections.get(position).setSelected(true);
            }
            return true;
        });*/

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bookCollections.get(position).setSelected(isChecked);
        });


        holder.collectionSettingsButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.popup_collection_settings);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.pu_bkcl_edit){
                    BLAlertDialog.editBookCollectionDialog(context, bookCollection.getName(), arg -> {holder.collectionNameView.setText(arg);});
                    //notifyItemChanged(position);
                    return true;
                } else if (item.getItemId() == R.id.pu_bkcl_delete){
                    BLAlertDialog.deleteBookCollectionDialog(context, bookCollection.getName(), () -> {
                        bookCollections.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, bookCollections.size());
                    });
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }
    @Override
    public int getItemCount() {
        return bookCollections.size();
    }

    public boolean isMultiChoise() {
        return multiChoise;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView collectionCardView;
        TextView collectionNameView;
        ImageButton collectionSettingsButton;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            collectionCardView = itemView.findViewById(R.id.cd_collection_card);
            collectionNameView = itemView.findViewById(R.id.cd_collection_name);
            collectionSettingsButton = itemView.findViewById(R.id.cd_book_collection_settings_btn);
            checkBox = itemView.findViewById(R.id.cd_collection_checkbox);
        }
    }

    public ArrayList<BookCollection> getBookCollections() {
        return bookCollections;
    }

    public void setBookCollections(ArrayList<BookCollection> bookCollections) {
        this.bookCollections = bookCollections;
    }
}
