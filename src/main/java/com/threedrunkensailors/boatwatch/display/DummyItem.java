package com.threedrunkensailors.boatwatch.display;

import com.adafruit.lcd.LCD;

import java.io.IOException;

/**
 * Created by marco on 5/6/14.
 */
public class DummyItem implements DisplayItem {
    @Override
    public void display(LCD lcd) throws IOException {
        lcd.clear();
        lcd.setText("No sensors to\ndisplay!");
    }
}
