package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.pi4j.io.gpio.GpioController;
import com.threedrunkensailors.boatwatch.sensors.GpioSensor;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class ShorePower extends AMetric {

    private static final String NAME = "Shore Power";

    private boolean reading = false;

    public ShorePower(int frequency) {
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
        GpioSensor gpioSensor = new GpioSensor();
        try {
            GpioController gpio = gpioSensor.open();
            this.setReading(gpio.provisionDigitalInputPin(GpioSensor.SHORE_POWER).isHigh());
        } catch (SensorReadingException e) {
            setReadingException(true);
        } catch (InterruptedException e) {
        } finally {
            gpioSensor.close();
        }
    }

    @Override
    public void display(LCD lcd) throws IOException {
        lcd.clear();
        String value;
        if (isReadingException()) {
            value = "Error!";
        } else {
            value = (this.isReading() ? "On" : "Off");
        }
        lcd.setText(String.format("%s:\n%s", NAME,value));
    }

    @Override
    public void select() {
        this.readSensor();
    }

    public boolean isReading() {
        return reading;
    }

    public void setReading(boolean reading) {
        this.reading = reading;
    }
}
