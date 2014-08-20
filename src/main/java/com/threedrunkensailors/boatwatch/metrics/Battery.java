package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.MCP3008;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Battery extends AMetric {

    private static final String NAME = "Battery";
    private static final double ZERO_CURRENT = 2500;
    // Amplifier Resistance >= 2.823 Ohms
    private static final double AMPLIFIER_RESISTANCE = 3.3;
    private static final double SHUNT_MAX_AMPS = 50;
    private static final double SHUNT_MAX_MILLIVOLTS = 75;

    private static final String TOTAL_CAPACITY = "TotalCapacity";
    private static final String CURRENT_CAPACITY = "CurrentCapacity";
    private static final String CURRENT_AMPS = "CurrentAmps";
    private static final String ONE_MINUTE_AVERAGE_AMPS = "OneMinuteAverageAmps";
    private static final String FIVE_MINUTE_AVERAGE_AMPS = "FiveMinuteAverageAmps";
    private static final String FIFTEEN_MINUTE_AVERAGE_AMPS = "FifteenMinuteAverageAmps";

    public static final int BATTERY_1 = 1;
    public static final int BATTERY_2 = 2;

    private int id;
    private double reading;
    private double totalCapacity;
    private double currentCapacity;
    private double currentAmps = 0;
    private double oneMinuteAverageAmps = 0;
    private double fiveMinuteAverageAmps = 0;
    private double fifteenMinuteAverageAmps = 0;
    private LinkedList<Double> readingHistory = new LinkedList<>();

    private final int MAX_READINGS;
    private final int READINGS_IN_1_MINUTE;
    private final int READINGS_IN_5_MINUTE;
    private final int READINGS_IN_15_MINUTE;

    public Battery(int frequency, int id,double ampHours) {
        super(frequency);
        this.id = (id == BATTERY_1 ? BATTERY_1 : BATTERY_2);
        this.totalCapacity = ampHours * 60 * 60 * 1000;
        this.currentCapacity = this.totalCapacity;
        int readingsPerSecond = 1000 / this.frequency;
        READINGS_IN_1_MINUTE = readingsPerSecond * 60;
        READINGS_IN_5_MINUTE = readingsPerSecond * 60 * 5;
        READINGS_IN_15_MINUTE = readingsPerSecond * 60 * 15;
        MAX_READINGS = READINGS_IN_15_MINUTE;
    }

    public void run() {
        while (true) {
            try {
                this.readSensor();
                Thread.sleep(this.frequency);
            } catch (InterruptedException e) {
            }
        }
    }

    private void readSensor() {
        try {
            if (id == BATTERY_1) {
                this.setReading(MCP3008.readMillivolts(MCP3008.Channel.SHUNT_1));
            } else {
                this.setReading(MCP3008.readMillivolts(MCP3008.Channel.SHUNT_2));
            }
        } catch (SensorReadingException e) {
            setReadingException(true);
        }
    }

    @Override
    public void display(LCD lcd) throws IOException {
        lcd.clear();
        // TODO: add time left on battery and average consumption for last N min/hours etc
        lcd.setText(String.format("%s %d:\n%s", NAME,id,
                String.format("%d", this.getReading())));
    }

    @Override
    public void select() {
        resetCurrentCapacity();
        this.readSensor();

    }

    private String getReading() {
        String value = null;
        if (isReadingException()) {
            value = "Error!";
        } else {
            value = String.format("%d", reading);
        }
        return value;
    }

    public void setReading(double reading) {
        double amps = ((reading - ZERO_CURRENT) / ( 5 + (80/ AMPLIFIER_RESISTANCE))) * (SHUNT_MAX_AMPS/SHUNT_MAX_MILLIVOLTS);
        addCurrentCapacity(amps * this.frequency);
        setReadingHistory(amps);
        this.reading = reading;
    }

    private void setReadingHistory(double amps) {
        this.currentAmps = amps;
        this.readingHistory.add(amps);
        if (this.readingHistory.size() > MAX_READINGS) {
            this.readingHistory.removeFirst();
        }
        Iterator iter = this.readingHistory.iterator();
        int n = 0;
        double total = 0;
        while (iter.hasNext()) {
            Double r = (Double)iter.next();
            total += r;
            if (n == READINGS_IN_1_MINUTE) {
                oneMinuteAverageAmps = total / n;
                fiveMinuteAverageAmps = oneMinuteAverageAmps;
                fifteenMinuteAverageAmps = oneMinuteAverageAmps;
            } else if (n == READINGS_IN_5_MINUTE) {
                fiveMinuteAverageAmps = total / n;
                fifteenMinuteAverageAmps = oneMinuteAverageAmps;
            } else if (n == READINGS_IN_15_MINUTE) {
                fifteenMinuteAverageAmps = total / n;
                break;
            }
        }
    }

    @Override
    public HashMap<String,String> getData() {
        HashMap<String,String> values = super.getData();
        values.put(NAME + id + TOTAL_CAPACITY,String.format("%d", totalCapacity));
        values.put(NAME + id + CURRENT_CAPACITY,String.format("%d",currentCapacity));
        values.put(NAME + id + CURRENT_AMPS,String.format("%d",currentAmps));
        values.put(NAME + id + ONE_MINUTE_AVERAGE_AMPS,String.format("%d", oneMinuteAverageAmps));
        values.put(NAME + id + FIVE_MINUTE_AVERAGE_AMPS,String.format("%d", fiveMinuteAverageAmps));
        values.put(NAME + id + FIFTEEN_MINUTE_AVERAGE_AMPS,String.format("%d", fifteenMinuteAverageAmps));
        return values;
    }

    public void resetCurrentCapacity() {
        this.currentCapacity = totalCapacity;
    }

    public void addCurrentCapacity(double amps) {
        double newCapacity = this.currentCapacity + amps;
        if (newCapacity > totalCapacity) {
            resetCurrentCapacity();
        } else {
            currentCapacity = newCapacity;
        }
    }
}
