package com.threedrunkensailors.boatwatch.metrics;

import com.adafruit.lcd.LCD;
import com.pi4j.io.gpio.GpioController;
import com.threedrunkensailors.boatwatch.sensors.GpioSensor;
import com.threedrunkensailors.boatwatch.sensors.MCP3008;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class BlackWater extends AMetric {

    private static final String NAME = "Black Water";

    private int reading;
    private int min;
    private int max;

    public BlackWater(int frequency) {
        super(frequency);
        this.setMax(512);
        this.setMin(170);
    }

    public void run() {
        while (true) {
            GpioSensor gpioSensor = new GpioSensor();
            try {
                System.out.println("Reading BlackWater");
                GpioController gpio = gpioSensor.open();
                // turn on the relay module
                gpio.provisionDigitalOutputPin(GpioSensor.RELAY_MASTER).high();
                // turn on the black-water relay and wait for it to come on
                gpio.provisionDigitalOutputPin(GpioSensor.RELAY_BLACK_WATER).high();
                Thread.sleep(DEFAULT_PAUSE);
                // take a reading from the sensor
                this.setReading(MCP3008.read(MCP3008.Channel.BILGE));
                // turn off the black-water relay
                gpio.provisionDigitalOutputPin(GpioSensor.RELAY_BLACK_WATER).low();
                // turn off the relay module
                gpio.provisionDigitalOutputPin(GpioSensor.RELAY_MASTER).low();
                this.setReading(MCP3008.read(MCP3008.Channel.BLACK_WATER));
                Thread.sleep(DEFAULT_FREQUENCY);
            } catch (SensorReadingException e) {
                setReadingException(true);
            } catch (InterruptedException e) {
            } finally {
                gpioSensor.close();
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

    public int getPercent() {
        int value = (reading < min ? min : (reading > max ? max : reading));
        return (reading > 0 ? (int)((float)((float)(value-min)/(float)(max-min)) * 100) : 0);
    }

    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
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
