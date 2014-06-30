package com.threedrunkensailors.boatwatch.sensors;

/**
 * Created by marco on 4/29/14.
 */

import com.pi4j.wiringpi.Spi;

import java.util.concurrent.Semaphore;

public class SpiSensor {
    private final Semaphore[] semaphore = new Semaphore[2];

    public SpiSensor() {
        semaphore[0] = new Semaphore(1);
        semaphore[1] = new Semaphore(1);
    }

    public int open(int channel, int speed) throws InterruptedException {
        semaphore[channel].acquire();
        return Spi.wiringPiSPISetup(channel, speed);
    }

    public void close(int channel) {
        semaphore[channel].release();
    }

}
