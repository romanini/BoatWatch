package com.threedrunkensailors.boatwatch.sensors;

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
public class A2302 {

    private static boolean DEBUG = true;

    private static int READING_INTERVAL = 30 * 1000;

    private static class Readings {
        public Float temperature;
        public Float humidity;
        public Date lastReading;
    }

    private static Readings readings = new Readings();

    private static Readings read() throws SensorReadingException {
        return read(0);
    }

    private static Readings read(int attempt) throws SensorReadingException {
        if ((readings.lastReading == null) ||
            ((new Date().getTime() - readings.lastReading.getTime()) > READING_INTERVAL)) {
            if (DEBUG) {
                System.out.println("A2302 Setting fake sensor");
                Random random = new Random();
                readings.temperature = random.nextFloat() * 100;
                readings.humidity = random.nextFloat() * 100;
                readings.lastReading = new Date();
            } else {
                Process process = null;
                try {
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

                        readings.temperature = Float.parseFloat(tempParts[1]);
                        readings.humidity = Float.parseFloat(humidityParts[1]);
                        readings.lastReading = new Date();
                    } else if (attempt < 10) {
                        Thread.sleep(500);
                        readings = read(attempt++);
                    }
                } catch (Exception e) {
                    throw new SensorReadingException();
                }
            }
        }
        return readings;
    }

    synchronized public static Float getTemperature() throws SensorReadingException {
        return read().temperature;
    }

    synchronized public static Float getHumidity() throws SensorReadingException {
        return read().humidity;
    }

}
