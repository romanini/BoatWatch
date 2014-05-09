package com.ThreeDrunkenSailors.boatwatch.metrics;

import com.ThreeDrunkenSailors.boatwatch.display.DisplayItem;

abstract public class AMetric implements Runnable, DisplayItem {

    protected static int DEFAULT_INTERVAL = 60 * 1000;

    private boolean readingException = false;
    private int reading = 0;
    private int min = 0;
    private int max = 1023;

    public AMetric() {
    }

    public int getPercent() {
        int value = (reading < min ? min : (reading > max ? max : reading));
        return (reading > 0 ? (int)((float)((float)(value-min)/(float)(max-min)) * 100) : 0);
    }

    protected int getReading() {
        return reading;
    }

    protected void setReading(int reading) {
        this.reading = reading;
        this.readingException = false;
    }

    protected int getMin() {
        return min;
    }

    protected void setMin(int min) {
        this.min = min;
    }

    protected int getMax() {
        return max;
    }

    protected void setMax(int max) { this.max = max; }

    protected boolean isReadingException() { return readingException; }

    protected void setReadingException(boolean readingException) { this.readingException = readingException; }
}
