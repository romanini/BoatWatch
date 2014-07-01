package com.threedrunkensailors.boatwatch.sensors;

import com.pi4j.wiringpi.Spi;

import java.util.Random;

/**
 * Created by marco on 4/29/14.
 */
public class MCP3008 {

    public enum Channel {
        BILGE(0),
        BLACK_WATER(1),
        SHUNT_1(2),
        SHUNT_2(3),
        VOLTAGE(4);

        private final int channel;

        Channel(int value) {
            this.channel = value;
        }

        public int getValue() {
            return channel;
        }
    }

    public static synchronized int read(Channel channel) throws SensorReadingException {
        if (Boolean.getBoolean(System.getProperty("fakeSensor", "0"))) {
            Random random = new Random();
            return random.nextInt(1024);
        }
        int fd = Spi.wiringPiSPISetup(Spi.CHANNEL_0, 10000000);
        if (fd <= -1) {
            System.out.println(" ==>> SPI SETUP FAILED");
            throw new SensorReadingException();
        }

        byte[] spiData = new byte[3];
        spiData[0] = 1 ;
        spiData[1] = (byte) (0x10000000 | ((channel.getValue() & 7)<<4));
        spiData[2] = 0 ;

        Spi.wiringPiSPIDataRW(Spi.CHANNEL_0, spiData, 2) ;

        return ((spiData [0] << 7) | (spiData [1] >> 1)) & 0x3FF ;
    }

}
