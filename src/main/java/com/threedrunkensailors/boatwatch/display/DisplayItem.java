package com.threedrunkensailors.boatwatch.display;

import com.adafruit.lcd.LCD;

import java.io.IOException;

public interface DisplayItem {
    public void display(LCD lcd) throws IOException;
}
