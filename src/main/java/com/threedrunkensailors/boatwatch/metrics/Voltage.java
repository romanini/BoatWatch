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
                System.out.println("Reading Voltage ");
                this.setReading(MCP3008.readVoltage(MCP3008.Channel.VOLTAGE));
                Thread.sleep(DEFAULT_FREQUENCY);
            } catch (SensorReadingException e) {
                this.setReadingException(true);
            } catch (InterruptedException e) {
            }
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

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        this.reading = reading;
    }
}
