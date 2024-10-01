package com.example.booklib.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.booklib.models.Book;
import com.example.booklib.models.BookCollection;

import java.util.ArrayList;

/**
 * This class is an adapter for the Spinner that displays the books or collections.
 * It takes a Context, an ArrayList of objects of type T, and the resource id of the TextView.
 * It provides methods for getting the count of items, getting an item at a specific position, getting the id of an item at a specific position, and getting a view for the dropdown list.
 * It also provides a method for getting a view for the selected item.
 * @param <T> The type of the objects in the ArrayList.
 */
public class SpinAdapter<T> extends ArrayAdapter<T> {

    private Context context;
    private ArrayList<T> values;

    /**
     * Constructor for the SpinAdapter class.
     * @param context The context of the application.
     * @param textViewResourceId The resource id of the TextView.
     * @param values An ArrayList of objects of type T.
     */
    public SpinAdapter(Context context, int textViewResourceId,
                       ArrayList<T> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    /**
     * This method returns the number of items in the ArrayList.
     * @return The number of items in the ArrayList.
     */
    @Override
    public int getCount(){
        return values.size();
    }

    /**
     * This method returns the object at the specified position in the ArrayList.
     * @param position The position of the object.
     * @return The object at the specified position.
     */
    @Override
    public T getItem(int position){
        return values.get(position);
    }

    /**
     * This method returns the id of the object at the specified position in the ArrayList.
     * @param position The position of the object.
     * @return The id of the object at the specified position.
     */
    @Override
    public long getItemId(int position){
        return position;
    }

    /**
     * This method returns a view for the selected item.
     * @param position The position of the selected item.
     * @param convertView The view to be reused.
     * @param parent The parent view.
     * @return A TextView with the text of the selected item.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(values.get(position).toString());

        return label;
    }

    /**
     * This method returns a view for the dropdown list.
     * @param position The position of the item in the dropdown list.
     * @param convertView The view to be reused.
     * @param parent The parent view.
     * @return A TextView with the text of the item in the dropdown list.
     */
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(values.get(position).toString());

        return label;
    }
}

