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
        VOLTAGE(4),
        UNUESD_1(5),
        UNUESD_2(6),
        UNUESD_3(7);

        private final int channel;

        Channel(int value) {
            this.channel = value;
        }

        public int getValue() {
            return channel;
        }
    }

    public static synchronized int read(Channel channel) throws SensorReadingException {
        int fd = Spi.wiringPiSPISetup(Spi.CHANNEL_0, 1000000);
        if (fd <= -1) {
            System.out.println(" ==>> SPI SETUP FAILED");
            throw new SensorReadingException();
        }

        byte[] spiData = new byte[3];
        spiData[0] = 0x01 ;
        spiData[1] = (byte) (0x01 << 7 | ((channel.getValue() & 0x07)<<4));
        spiData[2] = 0x00 ;

        Spi.wiringPiSPIDataRW(Spi.CHANNEL_0, spiData, 3) ;
        return (((spiData[1] & 0x03) << 8) | ((int) spiData[2] & 0xFF));
    }

    public static double readVolts(Channel channel) throws SensorReadingException {
        return (5.0 / 1024) * read(channel);
    }

    public static double readMillivolts(Channel channel) throws SensorReadingException {
        return (5000.0 / 1024.0) * read(channel);
    }
}
