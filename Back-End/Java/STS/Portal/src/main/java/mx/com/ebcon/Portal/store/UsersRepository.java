
package mx.com.ebcon.Portal.store;

import java.util.List;
import java.util.ArrayList;

import java.sql.Types;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.CallableStatement;

import mx.com.ebcon.Portal.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UsersRepository
{
    private int code;
    private String error;

    public int getCode(){ return this.code; }

    public String getError(){ return this.error; }

    public User authentication(Connection connection, String username, String password)
    {
        this.code = 0;
        this.error = null;

        try
        {
            CallableStatement sp = connection.prepareCall("{call user_auth(?,?,?)}");
            sp.registerOutParameter("message", Types.VARCHAR);
            sp.setString("usrname", username);
            sp.setString("usrpswd", password);
            ResultSet rs = sp.executeQuery();

            if(!sp.getString("message").equals("OK"))
            {
                return null;
            }

            rs.next();
            User user = new User();

            user.setUsrid(rs.getInt("usrid"));
            user.setEmail(rs.getString("email"));
            user.setUname(rs.getString("username"));
            user.setFname(rs.getString("fullname"));
            user.setLogin(rs.getString("lastlogin"));
            user.setLocked(rs.getString("lockedown"));
            user.setProfile(rs.getString("rolename"));
            user.setLogged(rs.getString("registered"));

            rs.close();
            return user;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return null;
        }
    }

    public boolean pswrdused(Connection connection, String password, int userid)
    {
        this.code = 0;
        this.error = null;

        try
        {
            CallableStatement sp = connection.prepareCall("{call password_reused(?,?,?)}");

            sp.registerOutParameter("result", Types.INTEGER);
            sp.setString("hpaswrd", password);
            sp.setInt("userxid", userid);
            sp.executeUpdate();

            return (sp.getInt("result") == 0) ? false : true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return true;
        }
    }

    public boolean delete(Connection connection, int userid)
    {
        this.code = 0;
        this.error = null;

        try
        {
            CallableStatement sp = connection.prepareCall("{call user_delete(?)}");
            sp.setInt("userid", userid);
            sp.executeUpdate();
            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return false;
        }
    }

    public boolean enroll(Connection connection, int profile, String fullname, String username, String password, String email)
    {
        this.code = 0;
        this.error = null;

        try
        {
            CallableStatement sp = connection.prepareCall("{call user_insert(?,?,?,?,?)}");
            sp.setInt("rolid", profile);
            sp.setString("email", email);
            sp.setString("username", username);
            sp.setString("hashpswd", password);
            sp.setString("fullname", fullname);
            sp.executeUpdate();
            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return false;
        }
    }

    public boolean update(Connection connection, int userid, String password, String email, String locked)
    {
        this.code = 0;
        this.error = null;

        userid = (userid > 0) ? userid : 0;
        locked = (locked == null || !locked.equals("Y")) ? "N" : "Y";

        if( email != null && !email.matches("^[a-z0-9+_.-]+@[a-z0-9.-]+$") )
        {
            this.error = "Correo inválido";
            this.code = 500;

            return false;
        }

        try
        {
            CallableStatement sp = connection.prepareCall("{call user_update(?,?,?,?)}");

            sp.setInt("usrid", userid);
            sp.setString("ulock", locked);

            if( email == null ){ sp.setNull("email", Types.VARCHAR); } else { sp.setString("email", email); }
            if( password == null ){ sp.setNull("upswd", Types.VARCHAR); } else { sp.setString("upswd", password); }

            sp.executeUpdate();

            return true;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return false;
        }
    }

    public List<User> view(Connection connection, String username, String status)
    {
        this.code = 0;
        this.error = null;

        if( status != null )
        {
            status = (status.equals("N") || status.equals("Y")) ? status : null;
        }

        if( username != null && !username.matches("[a-zA-Z0-9]{4,16}") )
        {
            this.error = "Valor de búsqueda inválido";
            this.code = 500;

            return null;
        }

        try
        {
            List<User> users = new ArrayList<User>();
            CallableStatement sp = connection.prepareCall("{call users_view(?,?)}");
            if( status == null ){ sp.setNull("lckdown", Types.VARCHAR); } else { sp.setString("lckdown", status); }
            if( username == null || username.isBlank() ){ sp.setNull("usrname", Types.VARCHAR); } else { sp.setString("usrname", username); }
            ResultSet rs = sp.executeQuery();

            for(;rs.next();)
            {
                User user = new User();

                user.setUsrid(rs.getInt("usrid"));
                user.setEmail(rs.getString("email"));
                user.setUname(rs.getString("username"));
                user.setFname(rs.getString("fullname"));
                user.setLogin(rs.getString("lastlogin"));
                user.setLocked(rs.getString("lockedown"));
                user.setProfile(rs.getString("rolename"));
                user.setLogged(rs.getString("registered"));

                users.add(user);
            }

            rs.close();
            return users;
        }   catch(SQLException e)
        {
            this.code = e.getErrorCode();
            this.error = e.getMessage();

            return null;
        }
    }
}
