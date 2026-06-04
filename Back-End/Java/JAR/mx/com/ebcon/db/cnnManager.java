/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.com.ebcon.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 2022 oct 26
 * @author Angel Hernández
 * Obtiene un conexión a una DB MySQL
 * 
 */
public class cnnManager {
    String _user;
    String _pwd;
    String _stringConnection;
    String _driver = "com.mysql.cj.jdbc.Driver";
    
    public cnnManager( String stringConnection, String user, String pwd ){
        this._stringConnection = stringConnection;
        this._user = user;
        this._pwd = pwd;
    }
    
    public Connection getConnection() throws Exception {
        Class.forName(this._driver);
        
        return DriverManager.getConnection(this._stringConnection, this._user, this._pwd);
    }
}
