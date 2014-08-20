package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.MCP3008;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;
import java.util.HashMap;

public class Voltage extends AMetric {

    private static final String NAME = "Voltage";

    private static final String VOLTAGE = "Voltage";

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

    @Override
    public HashMap<String, String> getData() {
        HashMap<String,String> values = super.getData();
        values.put(VOLTAGE,String.format("%d", (isReadingException()? "Error" : String.format("%d", this.getReading()))));
        return values;
    }
}
