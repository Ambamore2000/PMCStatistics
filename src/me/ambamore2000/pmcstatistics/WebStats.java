package me.ambamore2000.pmcstatistics;

import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by AndrewKim.
 */
public class WebStats {

    public static String getHTML() {
        try {
            String urlString = "http://www.planetminecraft.com/";
            URL url = new URL(urlString);
            URLConnection uc = url.openConnection();
            uc.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            uc.connect();
            uc.getInputStream();
            BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
            StringBuilder parsedContentFromUrl = new StringBuilder();
            int ch;
            while ((ch = in.read()) != -1) {
                parsedContentFromUrl.append((char) ch);
            }
            return String.valueOf(parsedContentFromUrl);
        } catch (Exception e) {
            System.out.println("ERROR: Attempting to recieve HTML again in 10 seconds....");
            System.out.println("Cause: " + e.getCause());
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return getHTML();
        }
    }

    public String getOnline() {
        return Jsoup.parse(getHTML()).body()
                .getElementById("container")
                .getElementById("right")
                .getElementsByClass("pane_content").get(1)
                .getElementsByClass("statistics")
                .select("tbody").select("tr").select("td").get(0)
                .getElementsByClass("stat").text();
    }
}
