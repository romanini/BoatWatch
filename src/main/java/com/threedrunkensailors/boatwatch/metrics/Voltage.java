package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.MCP3008;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class Voltage extends AMetric {

    private static final String NAME = "Voltage";

    private double reading;

    public Voltage(int frequency) {
        super(frequency);
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
            this.setReading(MCP3008.readVolts(MCP3008.Channel.VOLTAGE));
        } catch (SensorReadingException e) {
            this.setReadingException(true);
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
        lcd.setText(String.format("%s:\n%s", NAME,value));
    }

    @Override
    public void select() {
        this.readSensor();
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        this.reading = reading;
    }
}
