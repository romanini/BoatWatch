package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.A2302;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class Temperature extends AMetric {

    private static String NAME = "Temperature";

    private float reading;

    public Temperature() {
        this.setMax(100);
        this.setMin(-10);
    }

    public void run() {
        while (true) {
            try {
                reading = A2302.getTemperature();
                Thread.sleep(DEFAULT_INTERVAL);
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
            value =  String.format("%.1f *C", reading);
        }
        lcd.setText(String.format("%s:\n%s", NAME,value));
    }

}
