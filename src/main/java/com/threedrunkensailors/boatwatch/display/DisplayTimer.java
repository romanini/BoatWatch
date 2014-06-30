package com.threedrunkensailors.boatwatch.display;

import com.adafruit.lcd.LCD;

import java.io.IOException;

/**
 * Created by marco on 5/5/14.
 */
public class DisplayTimer implements Runnable {
    private static int CHECK_INTERVAL = 100;
    private static int DEFAULT_TIMER = 6 * 1000;
    private Object lock = new Object();
    private Integer timer;
    private LCD lcd;

    public DisplayTimer(LCD lcd) {
        this.lcd = lcd;
    }

    public void run() {
        System.out.println("Starting Display Timer Thread");
        try {
            resetTimer();
            lcd.setDisplayEnabled(true);
            lcd.clear();
            while (!timerExpired()) {
                decrementTimer(CHECK_INTERVAL);
                try {
                    Thread.sleep(CHECK_INTERVAL);
                } catch (InterruptedException e) {
                }
            }
            lcd.setDisplayEnabled(false);
            lcd.clear();
//            lcd.stop();
            System.out.println("Stopping Display Timer Thread");
        } catch (IOException e)  {
        }
    }

    private boolean timerExpired() {
        synchronized (lock) {
            System.out.println("display timer is: " + timer);
            if (timer <= 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    private int decrementTimer(int dec) {
        synchronized (lock) {
            timer -= dec;
            return timer;
        }
    }

    public void resetTimer() {
        synchronized (lock) {
            timer = DEFAULT_TIMER;
        }
    }


}
