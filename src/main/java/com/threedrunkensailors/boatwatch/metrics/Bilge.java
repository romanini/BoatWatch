package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.MCP3008;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class Bilge extends AMetric {

    private static String NAME = "Bilge";

    public Bilge() {
        this.setMax(512);
        this.setMin(170);
    }

    public void run() {
        while (true) {
            try {
                this.setReading(MCP3008.read(0));
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
            value = String.format("(%d) %d %%", this.getReading(),this.getPercent());
        }
        lcd.setText(String.format("%s:\n%s", NAME,value));
    }

    @Override
    public int getPercent() {
        return (this.getReading() < this.getMin() ? 0 : 100-super.getPercent());
    }

}