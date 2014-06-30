package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.A2302;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class Humidity extends AMetric {

    private static String NAME = "Humidity";
    private Float reading;

    public Humidity() {
        this.setMax(100);
        this.setMin(0);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Reading Humidity");
                reading = A2302.getHumidity();
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
            value =  String.format("%d %%", this.getPercent());
        }
        lcd.setText(String.format("%s:\n%s", NAME,value));
    }

    public int getPercent() {
        float value = (reading < this.getMin() ? getMin() : (reading > getMax() ? getMax() : reading));
        return (reading > 0 ? (int)((float)((float)(value-getMin())/(float)(getMax()-getMin())) * 100) : 0);
    }

}
