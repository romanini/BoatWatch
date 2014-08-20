package com.threedrunkensailors.boatwatch.metrics;

import com.threedrunkensailors.boatwatch.display.DisplayItem;
import com.threedrunkensailors.boatwatch.google.GoogleSpreadsheetItem;

import java.util.HashMap;

abstract public class AMetric implements Runnable, DisplayItem, GoogleSpreadsheetItem {

    protected static int DEFAULT_PAUSE = 200;
    protected static int DEFAULT_FREQUENCY = 60 * 1000;

    protected int frequency = DEFAULT_FREQUENCY;
    private boolean readingException = false;

    public AMetric(int frequency) {
        this.frequency = frequency;
    }

    protected boolean isReadingException() { return readingException; }

    protected void setReadingException(boolean readingException) { this.readingException = readingException; }

    @Override
    public HashMap<String,String> getData() {
        HashMap<String,String> values = new HashMap<>();
        return values;
    }
}
