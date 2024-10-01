package com.example.booklib.activities;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklib.R;
import com.example.booklib.adapters.BookAdapter;
import com.example.booklib.adapters.BookCollectionAdapter;
import com.example.booklib.database.SQLiteDb;
import com.example.booklib.dialogs.BLAlertDialog;
import com.example.booklib.dictonary.BLDictionary;
import com.example.booklib.explorer.BLExplorer;
import com.example.booklib.fragments.BookCollectionFragment;
import com.example.booklib.fragments.BookFragment;
import com.example.booklib.models.Book;
import com.example.booklib.models.BookCollection;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.function.Consumer;


public class MainActivity extends AppCompatActivity {

    private boolean isFABOpen;
    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    private LinearLayout createBookLayout, createCollectionLayout;
    private NavigationView navigationView;
    private TextView createCollectionTextView, createBookTextView;
    private FloatingActionButton floatingFABMenuBtn, floatingCreateBookBtn, floatingCreateCollectionBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDb sqLiteDb = new SQLiteDb(this);


        drawerLayout = findViewById(R.id.main);

        toolbar = findViewById(R.id.topBookLibBar);
        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.open();
        });
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.cd_collection_multiChoise) {
                BookCollectionFragment bookCollectionFragment = (BookCollectionFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_COLLECTION_FRAGMENT_TAG);
                if (bookCollectionFragment != null) {
                    RecyclerView m_recyclerView = bookCollectionFragment.getM_recyclerView();
                    BookCollectionAdapter bookCollectionAdapter = (BookCollectionAdapter) m_recyclerView.getAdapter();
                    if (bookCollectionAdapter != null) {
                        if (bookCollectionAdapter.isMultiChoise()) {
                            m_recyclerView.setAdapter(new BookCollectionAdapter(MainActivity.this, sqLiteDb.getBookCollections(), false));
                        } else {
                            m_recyclerView.setAdapter(new BookCollectionAdapter(MainActivity.this, sqLiteDb.getBookCollections(), true));
                        }
                    }
                } else {
                    BookFragment bookFragment = (BookFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_FRAGMENT_TAG);
                    if (bookFragment != null) {
                        RecyclerView m_recyclerView = bookFragment.getM_recyclerView();
                        BookAdapter bookAdapter = (BookAdapter) m_recyclerView.getAdapter();
                        if (bookAdapter != null) {
                            if (bookAdapter.isMultiChoise()) {
                                m_recyclerView.setAdapter(new BookAdapter(MainActivity.this, sqLiteDb.getBooks(), false));
                            } else {
                                m_recyclerView.setAdapter(new BookAdapter(MainActivity.this, sqLiteDb.getBooks(), true));
                            }
                        }
                    }
                }
            }
            else if (item.getItemId() == R.id.cd_collection_checkbox) {
                View menuItemView = findViewById(R.id.cd_collection_checkbox);
                BookCollectionFragment bookCollectionFragment = (BookCollectionFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_COLLECTION_FRAGMENT_TAG);
                if (bookCollectionFragment != null) {
                    RecyclerView m_recyclerView = bookCollectionFragment.getM_recyclerView();
                    if (m_recyclerView != null) {
                        BookCollectionAdapter adapter = (BookCollectionAdapter) m_recyclerView.getAdapter();
                        if (adapter != null) {
                            ArrayList<BookCollection> bookCollections = adapter.getBookCollections();
                            if (!bookCollections.isEmpty()) {
                                PopupMenu popupMenu = new PopupMenu(this, menuItemView);
                                popupMenu.inflate(R.menu.popup_top_bar_settings);
                                popupMenu.getMenu().removeItem(R.id.pu_tbar_link);
                                popupMenu.setOnMenuItemClickListener(v -> {
                                    if (v.getItemId() == R.id.pu_tbar_delete) {
                                        sqLiteDb.multiDeleteBookCollection(bookCollections, () -> {
                                            m_recyclerView.setAdapter(new BookCollectionAdapter(MainActivity.this, sqLiteDb.getBookCollections(), false));
                                        });
                                        return true;
                                    }
                                    return false;
                                });
                                popupMenu.show();
                            }
                        }
                    }
                } else {
                    BookFragment bookFragment = (BookFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_FRAGMENT_TAG);
                    if (bookFragment != null) {
                        RecyclerView m_recyclerView = bookFragment.getM_recyclerView();
                        if (m_recyclerView != null) {
                            BookAdapter bookAdapter = (BookAdapter) m_recyclerView.getAdapter();
                            if (bookAdapter != null) {
                                ArrayList<Book> books = bookAdapter.getBooks();
                                if (!books.isEmpty()) {
                                    PopupMenu popupMenu = new PopupMenu(this, menuItemView);
                                    popupMenu.inflate(R.menu.popup_top_bar_settings);
                                    popupMenu.setOnMenuItemClickListener(v -> {
                                        if (v.getItemId() == R.id.pu_tbar_delete) {
                                            sqLiteDb.multiDeleteBook(books, () -> {
                                                m_recyclerView.setAdapter(new BookAdapter(MainActivity.this, sqLiteDb.getBooks(), false));
                                            });
                                            return true;
                                        } else if (v.getItemId() == R.id.pu_tbar_link) {
                                            BLAlertDialog.multiLinkBookDialog(MainActivity.this, books, () -> {
                                                m_recyclerView.setAdapter(new BookAdapter(MainActivity.this, sqLiteDb.getBooks(), false));
                                            });
                                            return true;
                                        }
                                        return false;
                                    });
                                    popupMenu.show();
                                }
                            }
                        }
                    }
                }
            }
            return true;
        });


        MenuItem item = toolbar.getMenu().findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                BookFragment bookFragment = (BookFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_FRAGMENT_TAG);
                BookCollectionFragment bookCollectionFragment = (BookCollectionFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_COLLECTION_FRAGMENT_TAG);
                if (bookFragment != null) {
                    BookAdapter bookAdapter = (BookAdapter) bookFragment.getM_recyclerView().getAdapter();
                    if (bookAdapter != null) {
                        if (newText.isEmpty()) {
                            bookAdapter.setBooks(sqLiteDb.getBooks());
                            bookAdapter.notifyDataSetChanged();
                        } else {
                            filterBooks(sqLiteDb.getBooks(), newText, arg -> {
                                bookAdapter.setBooks(arg);
                                bookAdapter.notifyDataSetChanged();
                            });
                        }
                    }
                } else if (bookCollectionFragment != null) {
                    BookCollectionAdapter bookCollectionAdapter = (BookCollectionAdapter) bookCollectionFragment.getM_recyclerView().getAdapter();
                    if (bookCollectionAdapter != null) {
                        if (newText.isEmpty()) {
                            bookCollectionAdapter.setBookCollections(sqLiteDb.getBookCollections());
                            bookCollectionAdapter.notifyDataSetChanged();
                        } else {
                            filterBookCollections(sqLiteDb.getBookCollections(), newText, arg -> {
                                bookCollectionAdapter.setBookCollections(arg);
                                bookCollectionAdapter.notifyDataSetChanged();
                            });
                        }
                    }
                }
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            BookFragment bookFragment = (BookFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_FRAGMENT_TAG);
            BookCollectionFragment bookCollectionFragment = (BookCollectionFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_COLLECTION_FRAGMENT_TAG);
            if (bookFragment != null) {
                BookAdapter bookAdapter = (BookAdapter) bookFragment.getM_recyclerView().getAdapter();
                bookAdapter.setBooks(sqLiteDb.getBooks());
                bookAdapter.notifyDataSetChanged();
            } else if (bookCollectionFragment != null) {
                BookCollectionAdapter bookCollectionAdapter = (BookCollectionAdapter) bookCollectionFragment.getM_recyclerView().getAdapter();
                bookCollectionAdapter.setBookCollections(sqLiteDb.getBookCollections());
                bookCollectionAdapter.notifyDataSetChanged();
            }
            return false;
        });

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.nm_menu_lib);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nm_menu_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
            return false;
        });

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    setFragment(new BookFragment(), BLDictionary.BOOK_FRAGMENT_TAG);
                } else if (tab.getPosition() == 1) {
                    setFragment(new BookCollectionFragment(), BLDictionary.BOOK_COLLECTION_FRAGMENT_TAG);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        floatingFABMenuBtn = findViewById(R.id.f_fab_menu_btn);
        floatingFABMenuBtn.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        });

        floatingCreateBookBtn = findViewById(R.id.f_book_add_btn);
        floatingCreateBookBtn.setOnClickListener(view -> {
            BLExplorer.startExplorer(intent -> {
                someActivityResultLauncher.launch(intent);
            });
        });

        floatingCreateCollectionBtn = findViewById(R.id.f_collection_add_btn);
        floatingCreateCollectionBtn.setOnClickListener(view -> {
            BLAlertDialog.createBookCollectionDialog(MainActivity.this, arg -> {
                BookCollectionFragment bookCollectionFragment = (BookCollectionFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_COLLECTION_FRAGMENT_TAG);
                if (bookCollectionFragment != null) {
                    BookCollectionAdapter bookCollectionAdapter = ((BookCollectionAdapter) bookCollectionFragment.getM_recyclerView().getAdapter());
                    if (bookCollectionAdapter != null) {
                        bookCollectionAdapter.getBookCollections().add(arg);
                        bookCollectionAdapter.notifyItemInserted(bookCollectionAdapter.getBookCollections().size() - 1);
                    }

                } else {
                    setTabSelected(BLDictionary.BOOK_COLLECTION_FRAGMENT_TAG);
                }
            });
        });


        createBookLayout = findViewById(R.id.book_add_layout);
        createBookLayout.setVisibility(View.GONE);

        createCollectionLayout = findViewById(R.id.collection_add_layout);
        createCollectionLayout.setVisibility(View.GONE);

        createBookTextView = findViewById(R.id.book_add_TextView);
        createBookTextView.setVisibility(View.GONE);

        createCollectionTextView = findViewById(R.id.collection_add_TextView);
        createCollectionTextView.setVisibility(View.GONE);

        setFragment(new BookFragment(), BLDictionary.BOOK_FRAGMENT_TAG);

    }

    private void filterBooks(ArrayList<Book> books, String query, Consumer<ArrayList<Book>> consumer) {
        ArrayList<Book> filtredBooks = new ArrayList<>();
        query = query.toLowerCase();
        for (Book book : books) {
            if (book.getName().toLowerCase().contains(query)) {
                filtredBooks.add(book);
            }
        }
        consumer.accept(filtredBooks);
    }

    private void filterBookCollections(ArrayList<BookCollection> bookCollections, String query, Consumer<ArrayList<BookCollection>> consumer) {
        ArrayList<BookCollection> filtredBookCollections = new ArrayList<>();
        query = query.toLowerCase();
        for (BookCollection book : bookCollections) {
            if (book.getName().toLowerCase().contains(query)) {
                filtredBookCollections.add(book);
            }
        }
        consumer.accept(filtredBookCollections);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    SQLiteDb sqLiteDb = new SQLiteDb(MainActivity.this);

                    String fileName = getFileName(data.getData());

                    Uri bookPath = data.getData();

                    int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(bookPath, takeFlags);

                    Book book = new Book(fileName, bookPath.toString(), 1);

                    BookFragment bookFragment = (BookFragment) getSupportFragmentManager().findFragmentByTag(BLDictionary.BOOK_FRAGMENT_TAG);
                    if (bookFragment != null) {
                        BookAdapter bookAdapter = (BookAdapter) bookFragment.getM_recyclerView().getAdapter();
                        if (bookAdapter != null) {
                            sqLiteDb.insertBook(book, sqlResult -> {
                                if (sqlResult) {
                                    Toast.makeText(MainActivity.this, "Книга \"" + fileName + "\" успешно добавлена", Toast.LENGTH_SHORT).show();
                                    bookAdapter.getBooks().add(book);
                                    bookAdapter.notifyItemInserted(bookAdapter.getBooks().size() - 1);
                                } else {
                                    Toast.makeText(MainActivity.this, "Такая книга уже добавлена!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        sqLiteDb.insertBook(book, sqlResult -> {
                            if (sqlResult) {
                                Toast.makeText(MainActivity.this, "Книга \"" + fileName + "\" успешно добавлена", Toast.LENGTH_SHORT).show();
                                setTabSelected(BLDictionary.BOOK_FRAGMENT_TAG);
                            } else {
                                Toast.makeText(MainActivity.this, "Такая книга уже добавлена!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void showFABMenu() {
        isFABOpen = true;
        createBookLayout.setVisibility(View.VISIBLE);
        createCollectionLayout.setVisibility(View.VISIBLE);
        floatingFABMenuBtn.animate().rotation(45);
        createBookLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_60));
        createCollectionLayout.animate().translationY(-getResources().getDimension(R.dimen.standard_120)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                createBookTextView.setVisibility(View.VISIBLE);
                createCollectionTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
    }

    private void closeFABMenu() {
        isFABOpen = false;
        floatingFABMenuBtn.animate().rotation(0);
        createBookLayout.animate().translationY(0);
        createCollectionLayout.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                createBookTextView.setVisibility(View.GONE);
                createCollectionTextView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                if (!isFABOpen) {
                    createBookLayout.setVisibility(View.GONE);
                    createCollectionLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
    }

    private void setTabSelected(String tag) {
        if (tag.equals(BLDictionary.BOOK_FRAGMENT_TAG)) {
            tabLayout.selectTab(tabLayout.getTabAt(0));
        } else if (tag.equals(BLDictionary.BOOK_COLLECTION_FRAGMENT_TAG)) {
            tabLayout.selectTab(tabLayout.getTabAt(1));
        }
    }

    public void setFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (isFABOpen) {
            closeFABMenu();
        } else {
            super.onBackPressed();
        }
    }

}