package com.andrei.ticket_system.util.csv_export;

public abstract class DocumentCreator<T> {
    public abstract Document<T> getDocument();
}
