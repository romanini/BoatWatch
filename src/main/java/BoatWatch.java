import java.io.IOException;
import java.util.Arrays;

import com.ThreeDrunkenSailors.boatwatch.display.Display;
import com.ThreeDrunkenSailors.boatwatch.display.DisplayItem;
import com.ThreeDrunkenSailors.boatwatch.metrics.*;

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

        for (t = 0; t < threads.length; t++) {
            threads[t].join();
        }
	}

}

