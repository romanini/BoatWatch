package com.threedrunkensailors.boatwatch.sensors;

import com.pi4j.wiringpi.Spi;

import java.util.Random;

/**
 * Created by marco on 4/29/14.
 */
public class MCP3008SPI {

    public static synchronized int read(int channel) {
        if (Boolean.getBoolean(System.getProperty("fakeSensor", "0"))) {
            Random random = new Random();
            return random.nextInt(1024);
        }
        int fd = Spi.wiringPiSPISetup(Spi.CHANNEL_0, 10000000);
        if (fd <= -1) {
            System.out.println(" ==>> SPI SETUP FAILED");
            return 0;
        }

        byte[] spiData = new byte[2];
        byte chanBits ;

        if (channel == 0)
            chanBits = (byte) 0x11010000 ;
        else
            chanBits = (byte) 0x11110000 ;

        spiData[0] = chanBits ;
        spiData[1] = 0 ;

        Spi.wiringPiSPIDataRW(Spi.CHANNEL_0, spiData, 2) ;

        return ((spiData [0] << 7) | (spiData [1] >> 1)) & 0x3FF ;
    }

}
