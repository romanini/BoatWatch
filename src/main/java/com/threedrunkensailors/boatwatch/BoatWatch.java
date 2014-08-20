package com.threedrunkensailors.boatwatch;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import com.threedrunkensailors.boatwatch.display.Display;
import com.threedrunkensailors.boatwatch.display.DisplayItem;
import com.threedrunkensailors.boatwatch.google.GoogleDocs;
import com.threedrunkensailors.boatwatch.google.GoogleSpreadsheetItem;
import com.threedrunkensailors.boatwatch.metrics.*;

public class BoatWatch {
    private final static int FREQUENCY_EVERY_4_HOURS = 4 * 60 * 60 * 1000;
    private final static int FREQUENCY_EVERY_60_SECONDS = 60 * 1000;
    private final static int FREQUENCY_EVERY_10_SECONDS = 10 * 1000;
    private final static int FREQUENCY_1HZ = 1000;
    private final static int FREQUENCY_10HZ = 100;

    private final static int BATTERY_AMP_HOURS = 650;

	private final static DisplayItem[] displayItems = {
            new Voltage(FREQUENCY_1HZ),
            new ShorePower(FREQUENCY_EVERY_60_SECONDS),
            new Battery(FREQUENCY_10HZ, Battery.BATTERY_1,BATTERY_AMP_HOURS),
            new Battery(FREQUENCY_10HZ, Battery.BATTERY_2,BATTERY_AMP_HOURS),
            new Bilge(FREQUENCY_EVERY_60_SECONDS),
			new BlackWater(FREQUENCY_EVERY_60_SECONDS),
            new Humidity(FREQUENCY_EVERY_60_SECONDS),
            new Temperature(FREQUENCY_EVERY_60_SECONDS) };

    private static Thread[] threads = new Thread[displayItems.length];

	public static void main(String[] args) throws IOException, InterruptedException {
        int t = 0;
        int g = 0;
        GoogleSpreadsheetItem[] googleSpreadsheetItems = null;
        for (DisplayItem displayItem: displayItems) {
            if (displayItem instanceof Runnable) {
                threads[t] = new Thread((Runnable)displayItem);
                threads[t].start();
                t++;
            }
            if (displayItem instanceof GoogleSpreadsheetItem) {
                googleSpreadsheetItems[g] = (GoogleSpreadsheetItem) displayItem;
                g++;
            }
        }

        Display display = new Display(Arrays.asList(displayItems));
        Thread displayThread = new Thread((Runnable)display);
        displayThread.start();

        GoogleDocs googleDocs = new GoogleDocs(Arrays.asList(googleSpreadsheetItems),FREQUENCY_EVERY_4_HOURS);
        Thread googleDocsThread = new Thread((Runnable)googleDocs);
        googleDocsThread.start();

        for (t = 0; t < threads.length; t++) {
            threads[t].join();
        }
        displayThread.join();
        googleDocsThread.join();
	}


}

