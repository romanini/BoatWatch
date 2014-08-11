package com.threedrunkensailors.boatwatch;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.sensors.MCP3008;
import com.threedrunkensailors.boatwatch.sensors.SensorReadingException;

import java.io.IOException;

public class MCP3008Test {
	public static void main(String[] args) throws IOException, InterruptedException, SensorReadingException {
        /*
        System.out.println("Bilge: " + MCP3008.read(MCP3008.Channel.BILGE));
        System.out.println("Black Water: " + MCP3008.read(MCP3008.Channel.BLACK_WATER));
        System.out.println("Shunt1: " + MCP3008.read(MCP3008.Channel.BATTERY_1));
        System.out.println("Shunt2: " + MCP3008.read(MCP3008.Channel.BATTERY_2));
        System.out.println("Voltage: " + MCP3008.read(MCP3008.Channel.VOLTAGE));
        System.out.println("Unused1: " + MCP3008.read(MCP3008.Channel.UNUESD_1));
        System.out.println("Unused2: " + MCP3008.read(MCP3008.Channel.UNUESD_2));
        System.out.println("Unused3: " + MCP3008.read(MCP3008.Channel.UNUESD_3));
        */
        while (true) {
            System.out.println("Unused2: " + MCP3008.readVolts(MCP3008.Channel.UNUESD_2));
            Thread.sleep(250);
        }
	}

}

