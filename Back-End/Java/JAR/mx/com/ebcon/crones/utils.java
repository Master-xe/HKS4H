/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.crones;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import java.io.StringWriter;
import java.io.PrintWriter;



/**
 * 2022 OCT 28 
 * @author Angel
 * Funciones varias de uso común
 */
public class utils {
    // para obtener el string del stacktrace de errores
    private StringWriter sw = new StringWriter();
    private PrintWriter pw= new PrintWriter(sw);
    
    public String getStackTrace(Exception ex){
        ex.printStackTrace(pw);
        
        return sw.toString();
    }
    
    public static int getFrecuency(String value){
        String[] segments = value.split("D")  ;
        int days = (int)strToNumeric(segments[0]);
      
        return getFrecuency( days * 24, 0 ); 
    }
    
    public static String getDays(String value){
        String[] segments = value.split("D");
        
        return segments[0];
    } 
    
    public static String getSchedule(String value){
        String[] segments = value.split("D");
        
        return segments[1];
    }
    

    /*
    * Obtiene los milisegundos de una hora
    * @hours Segmento de hora
    * @minutes Segmento de minutos
    */
    public static int getFrecuency(float hours, int minutes){
        int milliseconds = 1000;
        int secondsByMinute = 60 * milliseconds;
        int minutesByHour = 60 * secondsByMinute;
        
        minutes = minutes * secondsByMinute;
        hours = hours * minutesByHour;
        
        return minutes + (int)hours;
    }
    
    /*
    * Evalua los valores proporcionados y genera un valor Date de acuerdo a las siguientes reglas
    * si la hora y minutos indicados son mayores que la hora actual, devuelve la fecha actual con la hora-minutos proporcionados
    * si la hora y minutos indicados son menores o iguales que la hora actual, devuelve la fecha MÁS UN DÍA con la hora-minutos proporcionados
    * @hours Segmento de hora
    * @minutes Segmento de minutos
    */
    public static Date getSchedule(int hours, int minutes){
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(currentTime);
        
        // verificar el horario establecido contra la hora actual
        if (
                (calendar.get(Calendar.HOUR_OF_DAY) > hours)
                || (calendar.get(Calendar.HOUR_OF_DAY) == hours  && calendar.get(Calendar.MINUTE)>= minutes)
            )
        {
            // Si la hora solicitada es mayor que la hora actual se prepara para el siguiente día
            calendar.set(Calendar.DAY_OF_YEAR,calendar.get( Calendar.DAY_OF_YEAR) +1);
        }
        calendar.set( Calendar.HOUR_OF_DAY , hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        
        return calendar.getTime();
    }
    
    public static float strToNumeric(String value){
        try{
            return Float.parseFloat  (value);
        }catch(NumberFormatException e){
            throw(e);
        }
    }
    
    public static String ldToStr(LocalDateTime ld){
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       
       return ld.format(formatter);
    }
    
    
    /*
    * Obtiene la primera hora del día de acuerdo a una fecha dada
    * @ld La fecha que se desea procesar
    *
    */
    public static LocalDateTime getInitialDate(LocalDateTime ld){
        LocalDateTime temp =  LocalDateTime.of(ld.getYear(), ld.getMonth(), ld.getDayOfMonth(), 0, 0, 0);

        return temp; //.minusDays(minusDays);
    }
    
    /*
    * Obtiene la última hora del día de acuerdo a una fecha dada
    * @ld La fecha que se desea procesar
    *
    */
    public static LocalDateTime getFinalDate(LocalDateTime ld){
        return ld.plusDays(1).minusSeconds(1);
    }
    
    public static byte[] getDecodedBytes(String base64Data) {
        base64Data = base64Data.substring(base64Data.indexOf(",") + 1, base64Data.length());
        byte[] dataBytes = base64Data.getBytes();

        return Base64.getDecoder().decode(dataBytes);
    }
    
    public static LocalDateTime convertToLocalDateTimeViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();
    }
}
