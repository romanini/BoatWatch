package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.A2302;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class Temperature extends AMetric {

    private static final String NAME = "Temperature";

    private Double reading;

    public Temperature(int frequency) {
        super(frequency);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Reading Temperature");
                this.setReading(A2302.getTemperature());
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
            value =  String.format("%.1f *C", this.getReading());
        }
        lcd.setText(String.format("%s:\n%s", NAME,value));
    }

    public Double getReading() {
        return reading;
    }

    public void setReading(Double reading) {
        this.reading = reading;
    }
}
