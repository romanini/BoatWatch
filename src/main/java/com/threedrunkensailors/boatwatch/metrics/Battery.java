package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.MCP3008;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class Battery extends AMetric {

    private static final String NAME = "Battery";
    private static final double ZERO_CURRENT = 2500;
    // Amplifier Resistance >= 2.823 Ohms
    private static final double AMPLIFIER_RESISTANCE = 2.823;
    private static final double SHUNT_MAX_AMPS = 50;
    private static final double SHUNT_MAX_MILLIVOLTS = 75;

    public static final int BATTERY_1 = 1;
    public static final int BATTERY_2 = 2;

    private int id;
    private double reading;
    private double batteryCapacity;
    private double currentCapacity;
    private double OneMinuteAverage = 0;
    private double FiveMinuteAverage = 0;
    private double FifteenMinuteAverage = 0;
    private LinkedList<Double> currentAmps = new LinkedList<>();

    private final int MAX_READINGS;
    private final int READINGS_IN_1_MINUTE;
    private final int READINGS_IN_5_MINUTE;
    private final int READINGS_IN_15_MINUTE;

    public Battery(int frequency, int id,double ampHours) {
        super(frequency);
        this.id = (id == BATTERY_1 ? BATTERY_1 : BATTERY_2);
        this.batteryCapacity = ampHours * 60 * 60 * 1000;
        this.currentCapacity = this.batteryCapacity;
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
        String value;
        if (isReadingException()) {
            value = "Error!";
        } else {
            value = String.format("%d", this.getReading());
        }
        // TODO: add time left on battery and average consumption for last N min/hours etc
        lcd.setText(String.format("%s %d:\n%s", NAME,id,value));
    }

    @Override
    public void select() {
        this.currentCapacity = this.batteryCapacity;
        this.readSensor();

    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        // TODO convert Vout (0-5v) back to amps
        double amps = ((reading - ZERO_CURRENT) / ( 5 + (80/ AMPLIFIER_RESISTANCE))) * (SHUNT_MAX_AMPS/SHUNT_MAX_MILLIVOLTS);
        this.currentCapacity += amps * this.frequency;
        this.setCurrentAmps(amps);
        this.reading = reading;
    }

    private void setCurrentAmps(double amps) {
       this.currentAmps.add(amps);
        if (this.currentAmps.size() > MAX_READINGS) {
            this.currentAmps.removeFirst();
        }
        Iterator iter = this.currentAmps.iterator();
        int n = 0;
        double total = 0;
        while (iter.hasNext()) {
            Double r = (Double)iter.next();
            total += r;
            if (n == READINGS_IN_1_MINUTE) {
                OneMinuteAverage = total / n;
                FiveMinuteAverage = OneMinuteAverage;
                FifteenMinuteAverage = OneMinuteAverage;
            } else if (n == READINGS_IN_5_MINUTE) {
                FiveMinuteAverage = total / n;
                FifteenMinuteAverage = OneMinuteAverage;
            } else if (n == READINGS_IN_15_MINUTE) {
                FifteenMinuteAverage = total / n;
                break;
            }
        }
    }
}
