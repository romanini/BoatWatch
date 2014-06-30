package com.threedrunkensailors.boatwatch.sensors;

/**
 * Created by marco on 4/29/14.
 */
import com.pi4j.io.gpio.*;

import java.util.concurrent.Semaphore;

public class GpioSensor {
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
        try {
            gpio.shutdown();
        } finally {
            semaphore.release();
        }
    }

}
