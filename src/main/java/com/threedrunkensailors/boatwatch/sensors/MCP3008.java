package com.threedrunkensailors.boatwatch.sensors;

import com.pi4j.io.gpio.*;

/**
 * Created by marco on 4/29/14.
 */
public class MCP3008 extends Gpio {

    public static synchronized int read(int channel) throws SensorReadingException {
        GpioPinDigitalOutput clockpin = getGpio().provisionDigitalOutputPin(
                RaspiPin.GPIO_01, "SPICLK", PinState.LOW);
        GpioPinDigitalInput misopin = getGpio().provisionDigitalInputPin(
                RaspiPin.GPIO_04, "SPIMISO", PinPullResistance.PULL_DOWN);
        GpioPinDigitalOutput mosipin = getGpio().provisionDigitalOutputPin(
                RaspiPin.GPIO_05, "SPIMOSI", PinState.LOW);
        GpioPinDigitalOutput cspin = getGpio().provisionDigitalOutputPin(
                RaspiPin.GPIO_06, "SPICS", PinState.LOW);

        if ((channel > 7) || (channel < 0))
            return -1;

        cspin.high();
        clockpin.low();
        cspin.low();

        int commandout = channel;
        commandout |= 0x18;
        commandout <<= 3;
        for (int i = 0; i < 5; i++) {
            if ((commandout & 0x80) != 0) {
                mosipin.high();
            }

            else {
                mosipin.low();
            }
            commandout <<= 1;
            clockpin.high();
            clockpin.low();
        }

        int adcout = 0;
        for (int i = 0; i < 12; i++) {
            clockpin.high();
            clockpin.low();
            adcout <<= 1;
            if (misopin.getState() == PinState.HIGH) {
                adcout |= 0x1;
            }

        }

        cspin.high();

        adcout >>= 1;

        return adcout;
    }

}
