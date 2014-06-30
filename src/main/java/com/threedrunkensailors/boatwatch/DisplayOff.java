package com.threedrunkensailors.boatwatch;

import com.adafruit.lcd.LCD;
import com.threedrunkensailors.boatwatch.display.Display;
import com.threedrunkensailors.boatwatch.display.DisplayItem;
import com.threedrunkensailors.boatwatch.metrics.Bilge;
import com.threedrunkensailors.boatwatch.metrics.BlackWater;
import com.threedrunkensailors.boatwatch.metrics.Humidity;
import com.threedrunkensailors.boatwatch.metrics.Temperature;

import java.io.IOException;
import java.util.Arrays;

public class DisplayOff {
	public static void main(String[] args) throws IOException, InterruptedException {
        LCD lcd = new LCD();
        lcd.stop();
	}

}

