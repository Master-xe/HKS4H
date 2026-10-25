
package mx.com.konesh.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mx.com.konesh.config.Config;
/**
 *
 * @author janhell
 */
public class Connector
{
    private final String user;
    private final String pwd;
    private final String url;
    private final String driver;
    
    public Connector()
    {
        this.url =  Config.get("db.url");
        this.user = Config.get("db.user");
        this.pwd = Config.get("db.pwd");        
        this.driver = "com.mysql.cj.jdbc.Driver";
    }
    
    public Connection getConnection() throws SQLException
    {
        try
        {
            Class.forName(this.driver);
        }   catch(ClassNotFoundException e)
        {
            throw new SQLException("Driver no encontrado: " + driver, e);
        }
        
        return DriverManager.getConnection(this.url, this.user, this.pwd);
    }
}
