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
            addToFile();
        }
    }

    private static void addToFile() {
        Date dateObject = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMMM dd, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

        String todayDate = dateFormat.format(dateObject);
        String time = timeFormat.format(dateObject);

        File todayDir = new File(todayDate);
        if (!todayDir.exists())
            todayDir.mkdir();

        File statsFile = new File(todayDir.getAbsolutePath() + "/stats.txt");
        File topFile = new File(todayDir.getAbsolutePath() + "/top.txt");

        for (File a : new File[] {statsFile, topFile}) {
            if (!a.exists())
                try {
                    a.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        PrintWriter statsOut = null, topOut = null;
        try {
            statsOut = new PrintWriter(new BufferedWriter(new FileWriter(statsFile, true)));
            topOut = new PrintWriter(new BufferedWriter(new FileWriter(topFile, true)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String newOnline = webStats.getOnline();

        boolean newStats = false;

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(topFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fileReader != null;
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuffer = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String topContent = stringBuffer.toString();

        String onlineString = "Online: ";

        if (topContent.isEmpty()) {
            newStats = true;
        } else {
            String topOnlineString = topContent.substring(
                    topContent.indexOf(onlineString), topContent.length()
            ).replace(onlineString, "");
            int topOnline = Integer.parseInt(topOnlineString.replace(",", "").trim());
            int newOnlineInt = Integer.parseInt(newOnline.replace(",", "").trim());
            if (newOnlineInt > topOnline) {
                System.out.println("Setting new top stat...");
                newStats = true;
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(topFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                assert writer != null;
                writer.print("");
                writer.close();
            }
        }

        String[] values = {time, onlineString + newOnline};

        assert statsOut != null;
        assert topOut != null;

        for (String val : values) {
            System.out.println(val);
            statsOut.println(val);
            if (newStats)
                topOut.println(val);
        }

        statsOut.close();
        topOut.close();

        try {
            Thread.sleep(720 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
