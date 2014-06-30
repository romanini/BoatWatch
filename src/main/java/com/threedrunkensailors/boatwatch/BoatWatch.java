package com.threedrunkensailors.boatwatch;

import java.io.IOException;
import java.util.Arrays;

import com.threedrunkensailors.boatwatch.display.Display;
import com.threedrunkensailors.boatwatch.display.DisplayItem;
import com.threedrunkensailors.boatwatch.metrics.*;

public class BoatWatch {
	private final static DisplayItem[] displayItems = {
            new Bilge(),
			new BlackWater(),
            new Humidity(),
            new Temperature() };

    private static Thread[] threads = new Thread[displayItems.length];

	public static void main(String[] args) throws IOException, InterruptedException {
        int t = 0;
        for (DisplayItem displayItem: displayItems) {
            if (displayItem instanceof Runnable) {
                threads[t] = new Thread((Runnable)displayItem);
                threads[t].start();
                t++;
            }
        }

        Display display = new Display(Arrays.asList(displayItems));
        Thread displayThread = new Thread((Runnable)display);
        displayThread.start();

        for (t = 0; t < threads.length; t++) {
            threads[t].join();
        }
        displayThread.join();
	}

}

