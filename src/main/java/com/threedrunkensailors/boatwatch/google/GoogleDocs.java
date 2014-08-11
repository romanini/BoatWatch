package com.threedrunkensailors.boatwatch.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by marco on 7/7/14.
 */
public class GoogleDocs {
    private static final String username = "threedrunkensailors";
    private static final String password = "c0untryr0s3";
    private static final String SERVICE_ACCOUNT_EMAIL = "473960089476-gc27e17sn5ni8tivn1ldb4gsiennekf6@developer.gserviceaccount.com";

    public static void uploadMetric() throws ServiceException, IOException, GeneralSecurityException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountScopes(Arrays.asList(DriveScopes.DRIVE,
                                "https://spreadsheets.google.com/feeds",
                                "https://docs.google.com/feeds"))
                .setServiceAccountPrivateKeyFromP12File(
                        new java.io.File("/keys/BoatWatch.p12"))
                .build();

        SpreadsheetService service = new SpreadsheetService("MySpreadsheetIntegration-v1");

        service.setOAuth2Credentials(credential);

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
