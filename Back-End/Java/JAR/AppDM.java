
import java.util.Timer;
import java.util.Properties;
import java.time.LocalDate;
import java.io.InputStream;
import java.io.FileInputStream;

import mx.com.ebcon.crones.checker;
import mx.com.ebcon.crones.utils;

public class AppDM
{
    private static Properties _properties = new Properties();
    private static InputStream _input;
  
    public static void main(String[] args)
    {
        try
        {
            System.out.println(LocalDate.now() + " >>> INICIANDO   ===== App de Descarga masiva =====");
            
            _input =new FileInputStream("config.properties");
            _properties.load(_input); 
            
            String strCnn = _properties.getProperty("strCnn");
            String user = _properties.getProperty("user");
            String pwd = _properties.getProperty("pwd");
            
            int checkerFrecuency =  (int)utils.strToNumeric( _properties.getProperty("checkerFrecuency"));
            
            _input.close();
            
            Timer checkerTimer = new Timer();
            int frecuency = utils.getFrecuency(checkerFrecuency, 0);
            checker checkerTask = new checker();
            checkerTask.init(strCnn, user, pwd);
            checkerTimer.scheduleAtFixedRate( checkerTask ,0,frecuency);
            
            System.out.println(LocalDate.now() + " >>> ===== App de Descarga masiva =====    INICIADA");
        }   catch(Exception ex)
        {
            System.out.println(LocalDate.now() + " >>> " + ex.getMessage());
        }
    }
}
