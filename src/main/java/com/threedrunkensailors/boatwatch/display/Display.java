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

    private static int INTERVAL = 100;

    private ArrayList<DisplayItem> items = new ArrayList<DisplayItem>();
    private DisplayTimer displayTimer;
    private Thread displayTimerThread;
    private int currentItem = 0;
    private int currentLevel = 0;
    private static LCD lcd;

    public Display(List<DisplayItem> items) {
        try {
            lcd = new LCD();
            if (items.size() > 0) {
                this.items.addAll(items);
            } else {
                items.add(new DummyItem());
            }
            displayTimer = new DisplayTimer(lcd);
            displayTimerThread = new Thread(displayTimer);
            ButtonPressedObserver observer = new ButtonPressedObserver(lcd);
            observer.addButtonListener(this);

            resetDisplayTimer();
            items.get(currentItem).display(lcd);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void onButtonPressed(Button button) {
        resetDisplayTimer();
        try {
            if (currentLevel > 0 &&  items.get(currentItem) instanceof ButtonListener) {
                ((ButtonListener)items.get(currentItem)).onButtonPressed(button);
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
                        break;
                    default:
                        break;
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

    private void resetDisplayTimer() {
        if (displayTimerThread.isAlive()) {
            displayTimer.resetTimer();
        } else {
            displayTimerThread.start();
        }
    }
}
