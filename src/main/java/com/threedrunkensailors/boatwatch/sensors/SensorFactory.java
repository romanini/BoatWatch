package com.threedrunkensailors.boatwatch.sensors;

/**
 * Created by marco on 6/27/14.
 */
public class SensorFactory {
    private static GpioSensor gpioSensor = null;

    public static GpioSensor getGpioSensorInstance() {
        if (gpioSensor == null) {
            gpioSensor = new GpioSensor();
        }
        return gpioSensor;
    }
}
