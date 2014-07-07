package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.MCP3008;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class Shunt extends AMetric {

    public static final int SHUNT_1 = 1;
    public static final int SHUNT_2 = 2;
    private static final String NAME = "Shunt";

    private int id;
    private double reading;

    public Shunt(int frequency, int id) {
        super(frequency);
        this.id = (id == SHUNT_1 ? SHUNT_1 : SHUNT_2);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Reading Shunt " + id);
                if (id == SHUNT_1) {
                    this.setReading(MCP3008.readVoltage(MCP3008.Channel.SHUNT_1));
                } else {
                    this.setReading(MCP3008.readVoltage(MCP3008.Channel.SHUNT_2));
                }
                Thread.sleep(DEFAULT_FREQUENCY);
            } catch (SensorReadingException e) {
                setReadingException(true);
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
        // TODO: add time left on battery and average consumption for last N min/hours etc
        lcd.setText(String.format("%s %d:\n%s", NAME,id,value));
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        // TODO convert Vout (0-5v) back to amps
        this.reading = reading;
    }
}
