//import com.google.gdata.client.spreadsheet.SpreadsheetService;
//import com.google.gdata.data.spreadsheet.*;
//import com.google.gdata.com.ThreeDrunkenSailors.util.ServiceException;

/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.URL;
import java.com.ThreeDrunkenSailors.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
*/
public class BoatWatchOld {

    static String username = "threedrunkensailors";
    static String password = "c0untryr0s3";

    //static Properties mailServerProperties;
    //static Session getMailSession;
    //static MimeMessage generateMailMessage;

    /*
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
        row.getCustomElements().setValueLocal("Date", metrics.getDate());
        row.getCustomElements().setValueLocal("Temperature", String.format("%.1f", ((Temperature)metrics.getReading(Metrics.TEMPERATURE)).getLocalReading()));
        row.getCustomElements().setValueLocal("HumidityPercent", String.format("%.1f", ((Humidity)metrics.getReading(Metrics.HUMIDITY)).getLocalReading()));
        row.getCustomElements().setValueLocal("BilgeReading", String.format("%d", metrics.getReading(Metrics.BILGE).getReading()));
        row.getCustomElements().setValueLocal("BilgePercent", String.format("%d", metrics.getReading(Metrics.BILGE).getPercent()));
        row.getCustomElements().setValueLocal("BlackWaterReading", String.format("%d", metrics.getReading(Metrics.BLACK_WATER).getReading()));
        row.getCustomElements().setValueLocal("BlackWaterPercent", String.format("%d", metrics.getReading(Metrics.BLACK_WATER).getPercent()));

        // Send the new row to the API for insertion.
        row = service.insert(listFeedUrl, row);

    }

    public static Map<String,String> getSettings() throws ServiceException, IOException {
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

        WorksheetEntry worksheet1 = worksheets.get(1);
        URL listFeedUrl1 = worksheet1.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl1, ListFeed.class);
        // THIS CODE IS NOT FROM GOOGLE: Print all the names
        List<ListEntry> rowList = listFeed.getEntries();
        Map<String,String> map = new HashMap<String,String>();
        for (ListEntry listEntry : rowList) {
            map.put(listEntry.getCustomElements().getValue("Name"), listEntry.getCustomElements().getValue("Valueame") );
        }

        return map;
    }

    public static void generateAndSendEmail() throws AddressException, MessagingException {

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("marco@thenaturalline.com"));
        generateMailMessage.setSubject("Greetings from Crunchify.com..");
        String emailBody = "Test email by Crunchify.com JavaMail API example. " + "<br><br> Regards, <br>Crunchify Admin";
        generateMailMessage.setContent(emailBody, "text/html");

        Transport transport = getMailSession.getTransport("smtp");
        // Enter your correct gmail UserID and Password
        String username = "threedrunkensailors";
        String password = "c0untryr0s3";


        transport.connect("smtp.gmail.com", username, password);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
*/
}
