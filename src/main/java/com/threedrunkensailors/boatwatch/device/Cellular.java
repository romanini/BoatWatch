package com.threedrunkensailors.boatwatch.device;

import com.pi4j.io.gpio.GpioController;
import com.threedrunkensailors.boatwatch.sensors.GpioSensor;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by marco on 8/19/14.
 */
public class Cellular {
    private final static int KEY_PULSE = 2 * 1000;
    private final static int BOOTUP_PAUSE = 5 * 1000;

    public static boolean enable() {
        return changeEnabled(true);
    }

    public static boolean disable() {
        return changeEnabled(false);
    }

    private static boolean changeEnabled(boolean newEnabled) {
        boolean result = false;
        GpioSensor gpioSensor = new GpioSensor();
        try {
            GpioController gpio = gpioSensor.open();

            // check to see its power status.
            boolean currentEnabled = gpio.provisionDigitalInputPin(GpioSensor.CELL_PS).isHigh();
            if (currentEnabled == newEnabled) {
                // its already in the state we asked for
                result = true;
            } else  {
                //its on and we want it off or its off and we want it on
                gpio.provisionDigitalOutputPin(GpioSensor.CELL_KEY).low();
                Thread.sleep(KEY_PULSE);
                gpio.provisionDigitalOutputPin(GpioSensor.CELL_KEY).high();
            }
            Thread.sleep(BOOTUP_PAUSE);
            if (newEnabled == gpio.provisionDigitalInputPin(GpioSensor.CELL_PS).isHigh()) {
                result = true;
            } else {
                System.out.println("Could not enable/disable cellular device");
            }
        } catch (Exception e) {
        } finally {
            gpioSensor.close();
        }
        return result;
    }
}
