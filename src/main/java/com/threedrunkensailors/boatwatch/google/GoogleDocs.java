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
import com.threedrunkensailors.boatwatch.device.Cellular;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by marco on 7/7/14.
 */
public class GoogleDocs implements Runnable {
    private final static String  DATE = "Date";
    private static int DEFAULT_FREQUENCY = 4 * 60 * 60 * 1000;

    private Object lock = new Object();
    private List<GoogleSpreadsheetItem> items = null;
    private int frequency = DEFAULT_FREQUENCY;

    //private static final String username = "threedrunkensailors";
    //private static final String password = "c0untryr0s3";

    private static final String SERVICE_ACCOUNT_EMAIL = "473960089476-gc27e17sn5ni8tivn1ldb4gsiennekf6@developer.gserviceaccount.com";

    public GoogleDocs(List<GoogleSpreadsheetItem> items, int frequency) {
        this.frequency = frequency;
        this.items = items;
    }

    @Override
    public void run() {
        try {
            this.uploadData();
            Thread.sleep(frequency);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        }

    }
    public void uploadData() throws IOException, GeneralSecurityException {
        try {
            if (!Cellular.enable()) {
                System.out.println("No network, so bailing on google!");
                throw new Exception();
            }
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
                            new java.io.File("/usr/boatwatch/cert/BoatWatch.p12"))
                    .build();

            SpreadsheetService service = new SpreadsheetService("MySpreadsheetIntegration-v1");

            service.setOAuth2Credentials(credential);

            String ID = "0Ag88R7hbHmicdEhFU0hCc25FTnI4STY0RU5fa2tNNFE";
            URL entryURL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/" + ID);

            SpreadsheetEntry spreadsheet = null;
            spreadsheet = service.getEntry(entryURL,SpreadsheetEntry.class);

            // FROM HERE IT'S GOOGLE CODE

            WorksheetFeed worksheetFeed = service.getFeed(
                    spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
            List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
            WorksheetEntry worksheet = worksheets.get(0);

            // Fetch the list feed of the worksheet.
            URL listFeedUrl = worksheet.getListFeedUrl();

            HashMap<String,String> dataValues = new HashMap<>();
            for (GoogleSpreadsheetItem item: items) {
                dataValues.putAll(item.getData());
            }
            // Create a local representation of the new row.
            ListEntry row = new ListEntry();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.getCustomElements().setValueLocal("Date", dateFormat.format(new Date()));

            for (Map.Entry<String,String> dataValue: dataValues.entrySet()) {
                row.getCustomElements().setValueLocal(dataValue.getKey(),dataValue.getValue());
            }
            // Send the new row to the API for insertion.
            row = service.insert(listFeedUrl, row);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Cellular.disable();
        }
    }

}
