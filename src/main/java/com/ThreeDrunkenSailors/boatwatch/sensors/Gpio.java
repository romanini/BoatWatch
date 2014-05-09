package com.ThreeDrunkenSailors.boatwatch.sensors;

/**
 * Created by marco on 4/29/14.
 */
import com.pi4j.io.gpio.*;

public class Gpio {
    private static GpioController gpio;

    public static synchronized GpioController getGpio() throws SensorReadingException {
        try {
            if (gpio == null) {
                gpio = GpioFactory.getInstance();
            }
        } catch (Exception e) {
            throw new SensorReadingException();
        }
        return gpio;
    }

}
