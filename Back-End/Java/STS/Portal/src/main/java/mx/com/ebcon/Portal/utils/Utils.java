
package mx.com.ebcon.Portal.utils;

import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalTime;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Base64;
import java.util.Random;

import java.math.BigInteger;

import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mx.com.ebcon.Portal.model.Company;

public class Utils
{
    private final static String numbers = "0123456789";
    private final static String special = "=@#$%&?/*+-.";
    private static final String slash = java.io.File.separator;
    private final static String lower = "abcdefghijklmnopqrstuvwxyz";
    private final static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

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
        }   catch(IOException e){}
    }

    public static String getHash(String input, String algorithm)
    {
        if( input == null || algorithm == null || input.isBlank() || algorithm.isBlank() ){ return null; }

        try
        {
            final byte[] bytes = input.getBytes();
            MessageDigest hash = MessageDigest.getInstance(algorithm);
            hash.update(bytes);
            BigInteger checksum = new BigInteger(1, hash.digest());
            return checksum.toString(16);
        }   catch(NoSuchAlgorithmException e)
        {
            return null;
        }
    }

    public static String randomString(int length)
    {
        String string = "", aux = "";
        Random rand = new Random();

        for(int i=0; i<length; i++)
        {
            aux += upper.charAt(rand.nextInt(upper.length()));
            aux += lower.charAt(rand.nextInt(lower.length()));
            aux += numbers.charAt(rand.nextInt(numbers.length()));
            aux += special.charAt(rand.nextInt(special.length()));
            string += aux.charAt(rand.nextInt(aux.length())); aux = "";
        }   return string;
    }

    public static String sendMail(String address, String username, String password, String token)
    {
        final String input = address + "/" + username + "/" + password;
        final String base64 = Base64.getEncoder().encodeToString(input.getBytes());
        final String params = "token=" + token + "&input-string=" + base64;

        try
        {
            URL url = new URL("https://servicios-ti.mx/services/send-mail.php");
            URLConnection connection = url.openConnection();
            HttpsURLConnection https = (HttpsURLConnection)connection;
            https.setRequestProperty("User-Agent", "Mozilla/5.0");
            https.setRequestMethod("POST");
            https.setDoOutput(true);

            OutputStream output = https.getOutputStream();
            output.write(params.getBytes());
            output.flush();
            output.close();

            InputStreamReader stream = new InputStreamReader(https.getInputStream());
            int code = https.getResponseCode();

            if( code != HttpURLConnection.HTTP_OK )
            {
                stream = new InputStreamReader(https.getErrorStream());
            }

            BufferedReader buffer = new BufferedReader(stream);
            String text = "", line = buffer.readLine();

            while( line != null )
            {
                text += line;
                line = buffer.readLine();
            }   return "OK\n" + text;
        }   catch(IOException e)
        {
            return "ERROR\n" + e.getMessage();
        }
    }

    public static String validateString(String sample, StringTypes type)
    {
        String regex = null;
        String emsg = null;

        if( sample == null || sample.isBlank() )
        {
            return "Valores nulos no permitidos";
        }

        switch(type)
        {
            case EMAIL: regex = "^[a-z0-9+_.-]+@[a-z0-9.-]+$"; emsg = " Formato de correo electrónico inválido"; break;
            case FULLNAME: regex = "[\\w\\s]{2,64}"; emsg = "Formato de nombre completo inválido"; break;
            case USERNAME: regex = "[a-zA-Z0-9]{4,16}"; emsg = "Nombre de usuario inválido"; break;
            case PASSWORD: regex = "((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]).{12,20})"; emsg = "Formato de contraseña inválido"; break;
            default: emsg = "NOT-FOUND";
        }

        if( emsg.equals("NOT-FOUND") )
        {
            return "No existe validación para el tipo de dato";
        }

        if(!sample.matches(regex))
        {
            return emsg;
        }

        return "OK";
    }

    public static String validateUserData(String email, String fullname, String username)
    {
        String result = null;

        result = validateString(email, StringTypes.EMAIL);

        if(!result.equals("OK"))
        {
            return result;
        }

        result = validateString(fullname, StringTypes.FULLNAME);

        if(!result.equals("OK"))
        {
            return result;
        }

        result = validateString(username, StringTypes.USERNAME);

        if(!result.equals("OK"))
        {
            return result;
        }

        return "OK";
    }

    public static String validateCompanyData(Company company)
    {
        String rfc = company.getCrfc();
        String code = company.getCcode();
        String fpfx = company.getFlpfx();
        String name = company.getCname();
        String pswd = company.getCpswd();
        String rtype = company.getRtype();
        String locked = company.getClock();

        if( rfc == null || code == null || fpfx == null || name == null || pswd == null || rtype == null || locked == null )
        {
            return "Valores nulos no permitidos";
        }

        if(!rtype.equals("CFDI") && !rtype.equals("Metadata"))
        {
            return "Tipo de solicitud al SAT inválida";
        }

        if(!locked.equals("N") && !locked.equals("Y"))
        {
            return "Estatus inválido";
        }

        if( pswd.length() < 10 || pswd.length() > 20 )
        {
            return "Longitud de la contraseña debe ser entre 10 y 20 caracteres";
        }

        if( fpfx.length() < 1024 )
        {
            return "El archivo PFX no parece ser una firma electrónica";
        }

        rfc = rfc.toUpperCase();
        code = code.toUpperCase();

        if(!code.matches("[A-Z0-9]{1,4}"))
        {
            return "Código de sociedad inválido";
        }

        if(!rfc.matches("[A-Z0-9]{12,13}"))
        {
            return "RFC inválido";
        }

        return "OK";
    }
}
