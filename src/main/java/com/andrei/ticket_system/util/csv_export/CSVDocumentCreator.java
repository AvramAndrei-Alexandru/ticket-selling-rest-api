package com.andrei.ticket_system.util.csv_export;

public class CSVDocumentCreator<T> extends DocumentCreator<T> {
    @Override
    public Document<T> getDocument() {
        return new CSVDocument<>();
    }
}
