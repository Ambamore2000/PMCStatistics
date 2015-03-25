package me.ambamore2000.pmcstatistics;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AndrewKim.
 */
public class PMCStatistics {
    static WebStats webStats;

    public static void main(String[] args) {
        webStats = new WebStats();
        while (true) {
            try {
                addToFile();
            } catch (Exception e) {
                System.out.println("§cERROR: Received non-expected exception. Attempting to update in 15 seconds.");
                try {
                    Thread.sleep(15 * 1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                addToFile();
            }
        }
    }

    private static void addToFile() {
        try {
            Date dateObject = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMMM dd, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

            String todayDate = dateFormat.format(dateObject);
            String time = timeFormat.format(dateObject);

            File todayDir = new File(todayDate);
            if (!todayDir.exists()) {
                todayDir.mkdir();
            }

            File statsFile = new File(todayDir.getAbsolutePath() + "/stats.txt");
            File topFile = new File(todayDir.getAbsolutePath() + "/top.txt");

            for (File a : new File[]{statsFile, topFile})
                if (!a.exists())
                    a.createNewFile();

            PrintWriter statsOut = new PrintWriter(new BufferedWriter(new FileWriter(statsFile, true))),
                    topOut = new PrintWriter(new BufferedWriter(new FileWriter(statsFile, true)));
            String newOnline = webStats.getOnline();

            boolean newStats = false;

            FileReader fileReader = new FileReader(topFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            fileReader.close();

            String topContent = stringBuffer.toString();
            String onlineString = "Online: ";

            if (topContent.isEmpty()) {
                newStats = true;
            } else {
                String topOnlineString = topContent.substring(
                        topContent.indexOf(onlineString) + onlineString.length(), topContent.length() - 1
                ).replace(",", "");
                int topOnline = Integer.parseInt(topOnlineString);
                int newOnlineInt = Integer.parseInt(newOnline.replace(",", ""));
                if (newOnlineInt > topOnline) {
                    System.out.println("§aSetting new top stat...");
                    newStats = true;
                    FileOutputStream writer = new FileOutputStream(topFile);
                }
            }

            String[] values = {time, onlineString + newOnline};

            for (String val : values) {
                System.out.println("§b" + val);
                statsOut.println(val);
                if (newStats)
                    topOut.println(val);
            }

            statsOut.close();
            topOut.close();

            Thread.sleep(900 * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
