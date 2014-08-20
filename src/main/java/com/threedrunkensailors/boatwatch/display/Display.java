package com.threedrunkensailors.boatwatch.display;

import com.adafruit.lcd.Button;
import com.adafruit.lcd.ButtonListener;
import com.adafruit.lcd.ButtonPressedObserver;
import com.adafruit.lcd.LCD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marco on 5/5/14.
 */
public class Display implements ButtonListener, Runnable {

    private static int DEFAULT_TIMER = 6 * 1000;
    private static int INTERVAL = 100;

    private Integer timer;
    private Object lock = new Object();
    //private ArrayList<DisplayItem> items = new ArrayList<DisplayItem>();
    private List<DisplayItem> items = null;
    private int currentItem = 0;
    private int currentLevel = 0;
    private boolean displayEnabled = false;

    private static LCD lcd;

    public Display(List<DisplayItem> items) throws IOException {
        lcd = new LCD();
        this.items = items;
        /*
        if (items.size() > 0) {
            this.items.addAll(items);
        } else {
            items.add(new DummyItem());
        }
        */
    }

    @Override
    public void run() {
        try {
            ButtonPressedObserver observer = new ButtonPressedObserver(lcd);
            observer.addButtonListener(this);

            resetDisplayTimer();
            items.get(currentItem).display(lcd);
            while (true) {
                processDisplayTimer(INTERVAL);
                Thread.sleep(INTERVAL);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        } finally {
            try {
                lcd.stop();
            } catch (Exception e) {
            }
        }

    }

    @Override
    public void onButtonPressed(Button button) {
        System.out.println("Button pressed: " + button.toString());
        try {
            if (resetDisplayTimer()) {
                if (currentLevel > 0 && items.get(currentItem) instanceof ButtonListener) {
                    ((ButtonListener) items.get(currentItem)).onButtonPressed(button);
                } else {
                    currentLevel--;
                }
                if (currentLevel <= 0) {
                    currentLevel = 0;
                    switch (button) {
                        case UP:
                            currentItem = --currentItem % items.size();
                            items.get(currentItem).display(lcd);
                            break;
                        case DOWN:
                            currentItem = ++currentItem % items.size();
                            items.get(currentItem).display(lcd);
                            break;
                        case RIGHT:
                            if (items.get(currentItem) instanceof ButtonListener) {
                                currentLevel++;
                                items.get(currentItem).display(lcd);
                            }
                            break;
                        case LEFT:
                            break;
                        case SELECT:
                            items.get(currentItem).select();
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            handleException(e);
        }
    }

    private void handleException(IOException e) {
        System.out.println("Problem talking to LCD! Exiting!");
        e.printStackTrace();
        System.exit(2);
    }

    private boolean processDisplayTimer(int dec) throws IOException {
        synchronized (lock) {
            if (timer <= 0) {
                disableDisplay();
                return true;
            } else {
                timer -= dec;
                return false;
            }
        }
    }

    public boolean resetDisplayTimer() throws IOException {
        synchronized (lock) {
            timer = DEFAULT_TIMER;
            return enableDisplay();
        }
    }

    private boolean enableDisplay() throws IOException {
        if (!displayEnabled) {
            lcd.setDisplayEnabled(true);
            lcd.setBacklight(LCD.Color.ON);
            currentItem = 0;
            currentLevel = 0;
            items.get(currentItem).display(lcd);
            displayEnabled = true;
            return false;
        } else {
            return true;
        }
    }

    private boolean disableDisplay() throws IOException {
        if (displayEnabled) {
            lcd.setDisplayEnabled(false);
            lcd.setBacklight(LCD.Color.OFF);
            displayEnabled = false;
            return true;
        } else {
            return false;
        }
    }
}
