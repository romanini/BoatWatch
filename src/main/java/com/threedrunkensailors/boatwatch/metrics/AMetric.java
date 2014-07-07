package com.threedrunkensailors.boatwatch.metrics;

import com.threedrunkensailors.boatwatch.display.DisplayItem;

abstract public class AMetric implements Runnable, DisplayItem {

    protected static int DEFAULT_PAUSE = 200;
    protected static int DEFAULT_FREQUENCY = 60 * 1000;

    private int frequency = DEFAULT_FREQUENCY;
    private boolean readingException = false;

    public AMetric(int frequency) {
        this.frequency = frequency;
    }

    protected boolean isReadingException() { return readingException; }

    protected void setReadingException(boolean readingException) { this.readingException = readingException; }
}
