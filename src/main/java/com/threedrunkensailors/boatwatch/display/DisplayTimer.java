package com.threedrunkensailors.boatwatch.display;

import com.adafruit.lcd.LCD;

import java.io.IOException;

/**
 * Created by marco on 5/5/14.
 */
public class DisplayTimer implements Runnable {
    private static int CHECK_INTERVAL = 100;
    private static int DEFAULT_TIMER = 60 * 1000;
    private Integer timer;
    private LCD lcd;

    public DisplayTimer(LCD lcd) {
        this.lcd = lcd;
    }

    public void run() {
        try {
            timer = DEFAULT_TIMER;
            lcd.setDisplayEnabled(true);
            while (!timerExpired()) {
                decrementTimer(CHECK_INTERVAL);
                try {
                    Thread.sleep(CHECK_INTERVAL);
                } catch (InterruptedException e) {
                }
            }
            lcd.setDisplayEnabled(false);
        } catch (IOException e)  {
        }
    }

    private boolean timerExpired() {
        synchronized (timer) {
            if (timer <= 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    private int decrementTimer(int dec) {
        synchronized (timer) {
            timer -= dec;
            return timer;
        }
    }

    public void resetTimer() {
        synchronized (timer) {
            timer = DEFAULT_TIMER;
        }
    }


}
