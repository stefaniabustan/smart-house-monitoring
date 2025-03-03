package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;


public class Utils {
    private static final String SENSOR_CSV_PATH = "src/main/resources/sensor.csv";

    private static CSVReader csvReader;
    private static String[] currentRecord;

    static {
        try {
            csvReader = new CSVReader(new FileReader(SENSOR_CSV_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected static Double readMeasurementFromCSV() {
        try {
            if ((currentRecord = csvReader.readNext()) != null) {
                return Double.parseDouble(currentRecord[0]);
            }
        } catch (IOException | CsvValidationException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }




}
