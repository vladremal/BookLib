package com.example.booklib.explorer;

import android.content.Intent;

import java.util.function.Consumer;

public final class BLExplorer {
    /**
     * Метод для запуска эксплорера
     * @param consumer интерфейс для выполнения действий после завершения работы метода
     */
    public static void startExplorer(Consumer<Intent> consumer){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {
                "application/pdf", // .pdf
        });
        consumer.accept(intent);
    }
}

