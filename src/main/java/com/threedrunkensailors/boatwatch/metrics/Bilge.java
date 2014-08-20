package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.pi4j.io.gpio.GpioController;
import com.threedrunkensailors.boatwatch.sensors.GpioSensor;
import com.threedrunkensailors.boatwatch.sensors.MCP3008;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;
import java.util.HashMap;

public class Bilge extends AMetric {

    private static final String NAME = "Bilge";

    private static final String BILGE_PERCENT = "BilgePercent";

    private int reading;
    private int min;
    private int max;

    public Bilge(int frequency) {
        super(frequency);
        this.setMax(512);
        this.setMin(170);
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
            System.out.println("Reading Bilge");
            GpioController gpio = gpioSensor.open();
            // turn on the relay module
            gpio.provisionDigitalOutputPin(GpioSensor.RELAY_MASTER).high();
            // turn on the bilge relay and wait for it to come on
            gpio.provisionDigitalOutputPin(GpioSensor.RELAY_BILGE).high();
            Thread.sleep(DEFAULT_PAUSE);
            // take a reading from the sensor
            this.setReading(MCP3008.read(MCP3008.Channel.BILGE));
            // turn off the bilge relay
            gpio.provisionDigitalOutputPin(GpioSensor.RELAY_BILGE).low();
            // turn off the relay module
            gpio.provisionDigitalOutputPin(GpioSensor.RELAY_MASTER).low();
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
            value = String.format("%d %%", this.getPercent());
        }
        lcd.setText(String.format("%s:\n%s", NAME,value));
    }

    @Override
    public void select() {
        this.readSensor();
    }

    @Override
    public HashMap<String,String> getData() {
        HashMap<String,String> values = super.getData();
        values.put(BILGE_PERCENT,String.format("%d", (isReadingException()? "Error" : String.format("%d", this.getPercent()))));
        return values;
    }

    public double getPercent() {
        if (this.getReading() < this.getMin())
            return 0.0;
        int value = (reading < min ? min : (reading > max ? max : reading));
        return 100 - (reading > 0 ? (int)((double)((double)(value-min)/(double)(max-min)) * 100) : 0);
    }

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
        this.setReadingException(false);
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
