package com.andrei.ticket_system.util.csv_export;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CSVDocument<T> implements Document<T> {
    @Override
    public void generateDocument(List<T> data) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
        StringBuilder fileName = new StringBuilder();
        fileName.append(data.get(0).getClass().getSimpleName()).append("-").append(currentDateTime).append("-Report").append(".csv");
        try {
            ICsvBeanWriter csvWriter = new CsvBeanWriter(new FileWriter(String.valueOf(fileName)), CsvPreference.STANDARD_PREFERENCE);
            Field[] fields = data.get(0).getClass().getDeclaredFields();
            List<String> fieldNames = Arrays.stream(fields)
                    .map(Field::getName)
                    .collect(Collectors.toList());
            csvWriter.writeHeader(fieldNames.toArray(new String[0]));
            for(T object : data) {
                csvWriter.write(object, fieldNames.toArray(new String[0]));
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
