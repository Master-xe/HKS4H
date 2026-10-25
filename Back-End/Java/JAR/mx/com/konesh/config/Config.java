package mx.com.konesh.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 *
 * @author janhell
 */
public class Config
{
    private static final Properties props = new Properties();

    static
    {
        try(InputStream in = Config.class.getClassLoader().getResourceAsStream("application.properties"))
        {
            if( in == null )
            {
                throw new RuntimeException("application.properties no encontrado en classpath");
            }

            props.load(in);
        }   catch(IOException e)
        {
            throw new RuntimeException("Error cargando application.properties", e);
        }
    }

    public static String get(String key)
    {
        return props.getProperty(key);
    }
}
