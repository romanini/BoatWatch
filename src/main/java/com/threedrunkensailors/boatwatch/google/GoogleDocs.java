package com.threedrunkensailors.boatwatch.google;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by marco on 7/7/14.
 */
public class GoogleDocs {
    private static final String username = "threedrunkensailors";
    private static final String password = "c0untryr0s3";

    public static void uploadMetric() throws ServiceException, IOException {
        SpreadsheetService service = new SpreadsheetService("MySpreadsheetIntegration-v1");
        service.setUserCredentials(username, password);

        String ID = "0Ag88R7hbHmicdEhFU0hCc25FTnI4STY0RU5fa2tNNFE";
        URL entryURL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/" + ID);

        SpreadsheetEntry spreadsheet = service.getEntry(entryURL,SpreadsheetEntry.class);

        // FROM HERE IT'S GOOGLE CODE

        WorksheetFeed worksheetFeed = service.getFeed(
                spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
        WorksheetEntry worksheet = worksheets.get(0);

        // Fetch the list feed of the worksheet.
        URL listFeedUrl = worksheet.getListFeedUrl();

        // Create a local representation of the new row.
        ListEntry row = new ListEntry();
        row.getCustomElements().setValueLocal("Date", new Date().toString());

        // Send the new row to the API for insertion.
        row = service.insert(listFeedUrl, row);

    }

}
