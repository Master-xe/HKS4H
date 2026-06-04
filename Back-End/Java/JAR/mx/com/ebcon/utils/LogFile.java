
package mx.com.ebcon.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogFile
{
    private static final String slash = java.io.File.separator;

    public static void write(String prefix, String message)
    {
        final DateTimeFormatter ftime = DateTimeFormatter.ofPattern("HH:mm:ss");
        final DateTimeFormatter fdate = DateTimeFormatter.ISO_LOCAL_DATE;
        String log = System.getProperty("user.home") + slash + prefix;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        try
        {
            String logfile = log +"-"+ date.format(fdate);
            FileWriter fw = new FileWriter(logfile, true);
            String msg = "[" + time.format(ftime) + "][" + message + "]\n";
            fw.write(msg);
            fw.close();
        }   catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}

