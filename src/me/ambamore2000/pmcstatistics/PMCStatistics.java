package me.ambamore2000.pmcstatistics;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AndrewKim.
 */
public class PMCStatistics {
    static WebStats webStats;

    static Calendar cal;

    public static void main(String[] args) {
        webStats = new WebStats();
        cal = Calendar.getInstance();
        while (true) {
            try {
                addToFile();
            } catch (Exception e) {
                System.out.println("ERROR: Received non-expected exception. Attempting to update in 15 seconds.");
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

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");

            String time = timeFormat.format(dateObject);

            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = getMonth(cal.get(Calendar.MONTH) + 1);
            String day = cal.get(Calendar.DAY_OF_MONTH) + ", " + getDay(cal.get(Calendar.DAY_OF_WEEK));

            File yearDir = new File("/var/www/" + year);
            createNewDir(yearDir);
            File monthDir = new File(yearDir.getAbsolutePath() + "/" + month);
            createNewDir(monthDir);
            File dayDir = new File(monthDir.getAbsolutePath() + "/" + day);
            createNewDir(dayDir);

            File statsFile = new File(dayDir.getAbsolutePath() + "/stats.txt");
            File topFile = new File(dayDir.getAbsolutePath() + "/top.txt");

            for (File a : new File[]{statsFile, topFile})
                if (!a.exists())
                    a.createNewFile();

            PrintWriter statsOut = new PrintWriter(new BufferedWriter(new FileWriter(statsFile, true))),
                    topOut = new PrintWriter(new BufferedWriter(new FileWriter(topFile, true)));
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
                    System.out.println("Setting new top stat...");
                    newStats = true;
                    FileOutputStream writer = new FileOutputStream(topFile);
                }
            }

            String[] values = {time, onlineString + newOnline};

            for (String val : values) {
                System.out.println(val);
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

    private static String getDay(int x) {
        switch (x) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return "";
    }

    private static String getMonth(int x) {
        switch (x) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "";
    }

    private static void createNewDir(File f) {
        if (!f.exists() || !f.isDirectory())
            f.mkdir();
    }
}