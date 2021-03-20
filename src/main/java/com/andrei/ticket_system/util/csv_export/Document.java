package com.andrei.ticket_system.util.csv_export;

import java.util.List;

public interface Document<T> {
    void generateDocument(List<T> data);
}
