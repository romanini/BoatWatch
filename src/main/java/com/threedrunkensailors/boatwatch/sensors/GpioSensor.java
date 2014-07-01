package com.threedrunkensailors.boatwatch.sensors;

/**
 * Created by marco on 4/29/14.
 */
import com.pi4j.io.gpio.*;

import java.util.concurrent.Semaphore;

public class GpioSensor {

    public enum DigitalPin {
        RELAY_MASTER(RaspiPin.GPIO_00),
        RELAY_BILGE(RaspiPin.GPIO_01),
        RELAY_BLACK_WATER(RaspiPin.GPIO_02),
        RELAY_UNUSED(RaspiPin.GPIO_03),
        SHORE_POWER(RaspiPin.GPIO_04);

        private final Pin pin;

        DigitalPin(Pin pin) {
            this.pin = pin;
        }

        public Pin getValue() {
            return pin;
        }
    }

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

    public static boolean isOn(DigitalPin pin) throws SensorReadingException, InterruptedException {
        boolean isOn = false;
        GpioSensor gpioSensor = new GpioSensor();
        try {
            GpioController gpio = gpioSensor.open();
            GpioPinDigitalInput checkPin = gpio.provisionDigitalInputPin(pin.getValue());
            if (checkPin.isHigh()) {
                isOn = true;
            }
        } finally {
            gpioSensor.close();
        }
        return isOn;
    }

    public static boolean isOff(DigitalPin pin) throws SensorReadingException, InterruptedException {
        boolean isOn = false;
        GpioSensor gpioSensor = new GpioSensor();
        try {
            GpioController gpio = gpioSensor.open();
            GpioPinDigitalInput checkPin = gpio.provisionDigitalInputPin(pin.getValue());
            if (checkPin.isLow()) {
                isOn = true;
            }
        } finally {
            gpioSensor.close();
        }
        return isOn;
    }

    public static void turnOn(DigitalPin pin) throws SensorReadingException, InterruptedException {
        GpioSensor gpioSensor = new GpioSensor();
        try {
            GpioController gpio = gpioSensor.open();
            gpio.provisionDigitalOutputPin(pin.getValue()).high();
        } finally {
            gpioSensor.close();
        }
    }

    public static void turnOff(DigitalPin pin) throws SensorReadingException, InterruptedException {
        GpioSensor gpioSensor = new GpioSensor();
        try {
            GpioController gpio = gpioSensor.open();
            gpio.provisionDigitalOutputPin(pin.getValue()).low();
        } finally {
            gpioSensor.close();
        }
    }



}
