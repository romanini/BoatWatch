package com.threedrunkensailors.boatwatch.sensors;

import com.pi4j.io.gpio.GpioController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by marco on 4/29/14.
 */
public class DHT22 {

    private static final int DEFAULT_PAUSE = 200;

    private static final int READING_INTERVAL = 30 * 1000;

    private static class Readings {
        public Double temperature;
        public Double humidity;
        public Date lastReading;
    }

    private static Readings readings = new Readings();

    private static Readings read() throws SensorReadingException {
        return read(0);
    }

    private static Readings read(int attempt) throws SensorReadingException {
        if ((readings.lastReading == null) ||
            ((new Date().getTime() - readings.lastReading.getTime()) > READING_INTERVAL)) {
                Process process = null;
                GpioSensor gpioSensor = new GpioSensor();
                try {
                    GpioController gpio = gpioSensor.open();
                    // turn on the relay module
                    gpio.provisionDigitalOutputPin(GpioSensor.RELAY_MASTER).high();
                    // turn on the temp sensor relay and wait for it to come on
                    gpio.provisionDigitalOutputPin(GpioSensor.RELAY_TEMP).high();
                    Thread.sleep(DEFAULT_PAUSE);
                    process = new ProcessBuilder("/home/sailor/AM2302").start();
                    InputStream is = process.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    List<String> lines = new ArrayList<String>();

                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                    if (lines.size() == 2) {
                        String[] tempParts = lines.get(0).split("=");
                        String[] humidityParts = lines.get(1).split("=");

                        readings.temperature = Double.parseDouble(tempParts[1]);
                        readings.humidity = Double.parseDouble(humidityParts[1]);
                        readings.lastReading = new Date();
                    } else if (attempt < 10) {
                        Thread.sleep(500);
                        gpioSensor.close();
                        readings = read(attempt++);
                    }
                    // turn off the temp sensor relay
                    gpio.provisionDigitalOutputPin(GpioSensor.RELAY_TEMP).low();
                    // turn off the relay module
                    gpio.provisionDigitalOutputPin(GpioSensor.RELAY_MASTER).low();
                } catch (Exception e) {
                    throw new SensorReadingException();
                } finally {
                    gpioSensor.close();
                }
        }
        return readings;
    }

    synchronized public static Double getTemperature() throws SensorReadingException {
        return read().temperature;
    }

    synchronized public static Double getHumidity() throws SensorReadingException {
        return read().humidity;
    }

}
