package com.ThreeDrunkenSailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.ThreeDrunkenSailors.boatwatch.sensors.MCP3008;
import com.ThreeDrunkenSailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class BlackWater extends AMetric {

    private static String NAME = "Black Water";

    public BlackWater() {
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
            value = String.format("(%d) %d %%",this.getReading(),this.getPercent());
        }
        lcd.setText(String.format("%s:\n%s", NAME,value));
    }

}
