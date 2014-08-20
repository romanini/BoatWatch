package com.threedrunkensailors.boatwatch.sensors;

/**
 * Created by marco on 4/29/14.
 */
import com.pi4j.io.gpio.*;

import java.util.concurrent.Semaphore;

public class GpioSensor {

    public static final Pin RELAY_MASTER = RaspiPin.GPIO_00;
    public static final Pin RELAY_TEMP = RaspiPin.GPIO_01;
    public static final Pin RELAY_BLACK_WATER = RaspiPin.GPIO_02;
    public static final Pin RELAY_BILGE = RaspiPin.GPIO_03;
    public static final Pin RELAY_UNUSED = RaspiPin.GPIO_04;
    public static final Pin SHORE_POWER = RaspiPin.GPIO_05;
    public static final Pin UNUSED = RaspiPin.GPIO_06;
    public static final Pin DHT22 = RaspiPin.GPIO_07;
    public static final Pin CELL_KEY = RaspiPin.GPIO_17;
    public static final Pin CELL_PS = RaspiPin.GPIO_18;
    public static final Pin UNUSED_3= RaspiPin.GPIO_19;
    public static final Pin UNUSED_4= RaspiPin.GPIO_20;

    private static GpioController gpio;
    private final Semaphore semaphore;

    public GpioSensor() {
        semaphore = new Semaphore(1);
    }

    public GpioController open() throws SensorReadingException, InterruptedException {
        semaphore.acquire();
        gpio = GpioFactory.getInstance();
        return gpio;
    }

    public void close() {
        if (gpio != null) {
            try {
                gpio.shutdown();
                gpio = null;
            } finally {
                semaphore.release();
            }
        }
    }

}
