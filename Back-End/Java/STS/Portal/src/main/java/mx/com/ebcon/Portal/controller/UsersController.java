
package mx.com.ebcon.Portal.controller;

import java.util.List;
import java.util.Base64;
import java.util.ArrayList;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import mx.com.ebcon.Portal.model.User;
import mx.com.ebcon.Portal.utils.Utils;
import mx.com.ebcon.Portal.utils.JResponse;
import mx.com.ebcon.Portal.utils.JListResponse;
import mx.com.ebcon.Portal.store.UsersRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/Users")
public class UsersController
{
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UsersRepository usersDAO;

    @PostMapping("/delete/{userid}") // @DeleteMapping
    public @ResponseBody JResponse delete(HttpSession session, @PathVariable(value = "userid") int userid)
    {
        Connection connection = null;
        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null || !jusersession.getProfile().equals("Admin") )
        {
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JResponse(e.getErrorCode(), -1, "SQL", e.getMessage());
        }

        if(!usersDAO.delete(connection, userid))
        {   try { connection.close(); } catch(SQLException e){}
            return new JResponse(usersDAO.getCode(), -1, "SQL", usersDAO.getError());
        }   try { connection.close(); } catch(SQLException e){}

        return new JResponse(0, 0, "OK", "Usuario eliminado");
    }

    @PostMapping("/listview")
    public @ResponseBody JListResponse<List<User>> list(HttpSession session, @RequestBody User user)
    {
        Connection connection = null;
        List<User> users = new ArrayList<User>();

        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null || !jusersession.getProfile().equals("Admin") )
        {
            return new JListResponse<List<User>>(users, 0, false, "Acceso Denegado");
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JListResponse<List<User>>(users, 0, false, e.getMessage());
        }

        users = usersDAO.view(connection, user.getUname(), user.getLocked());
        try { connection.close(); } catch(SQLException e){}
        return new JListResponse<List<User>>(users, users.size(), true, usersDAO.getError());
    }

    @PostMapping("/update")
    public @ResponseBody JResponse update(HttpSession session, @RequestBody User user)
    {
        final User jusersession = (User)session.getAttribute("jusersession");
        Connection connection = null;

        if( jusersession == null )
        {
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        if(!jusersession.getProfile().equals("Admin"))
        {
            if( jusersession.getUsrid() != user.getUsrid() )
            {
                return new JResponse(500, -1, "SRV", "Acceso Denegado");
            }
        }

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JResponse(e.getErrorCode(), -1, "SQL", e.getMessage());
        }

        final String password = Utils.getHash(user.getPaswd(), "SHA-256");
        user.setPaswd(password);

        if( password != null )
        {
            if( usersDAO.pswrdused(connection, password, user.getUsrid()) )
            {   try { connection.close(); } catch(SQLException e){}
                return new JResponse(usersDAO.getCode(), -1, "SQL", "Reuso de contraseña no permitido");
            }
        }

        if(!usersDAO.update(connection, user.getUsrid(), user.getPaswd(), user.getEmail(), user.getLocked()))
        {   try { connection.close(); } catch(SQLException e){}
            return new JResponse(usersDAO.getCode(), -1, "SQL", usersDAO.getError());
        }   try { connection.close(); } catch(SQLException e){}

        return new JResponse(0, 0, "OK", "Usuario actualizado");
    }

    @PostMapping("/signup")
    public @ResponseBody JResponse signup(HttpSession session, @RequestBody User user)
    {
        final User jusersession = (User)session.getAttribute("jusersession");

        if( jusersession == null || !jusersession.getProfile().equals("Admin") )
        {
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        final String validation = Utils.validateUserData(user.getEmail(), user.getFname(), user.getUname());

        if(!validation.equals("OK"))
        {
            return new JResponse(500, -1, "SRV", validation);
        }

        Connection connection = null;
        final int pswrdlen = (user.getRolid() == 2) ? 20 : 12;
        final String password = Utils.randomString(pswrdlen);
        user.setPaswd(Utils.getHash(password, "SHA-256"));

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JResponse(e.getErrorCode(), -1, "SQL", e.getMessage());
        }

        if(!usersDAO.enroll(connection, user.getRolid(), user.getFname(), user.getUname(), user.getPaswd(), user.getEmail()))
        {   try { connection.close(); } catch(SQLException e){}
            return new JResponse(500, -1, "SQL", usersDAO.getError());
        }   try { connection.close(); } catch(SQLException e){}

        final String token = "T2lYQ0t4L0RHVkR4dHZ5Nkk1VHNEakZ3Y0J4Nk9GODZuRyt4cE1wVm5t";
        Utils.sendMail(user.getEmail(), user.getUname(), password, token);

        return new JResponse(0, 0, "OK", "Usuario creado");
    }

    @PostMapping("/signin/{token}")
    public @ResponseBody JResponse signin(HttpSession session, @PathVariable(value = "token") String token)
    {
        String decoded = null;

        try
        {
            decoded = new String(Base64.getDecoder().decode(token));
        }   catch(IllegalArgumentException e)
        {// 498, -1, "SRV", e.getMessage()
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        final int index = decoded.indexOf(47);

        if( index < 5 )
        {// 400, -1, "SRV", "Formato Inválido"
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        final String username = decoded.substring(0, index);
        final String password = decoded.substring(index + 1);
        token = Utils.getHash(password, "SHA-256");
        Connection connection = null;

        try
        {
            connection = dataSource.getConnection();
        }   catch(SQLException e)
        {
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        final User user = usersDAO.authentication(connection, username, token);
        try { connection.close(); } catch(SQLException e){}
        if( user == null )
        {
            return new JResponse(500, -1, "SRV", "Acceso Denegado");
        }

        session.setAttribute("jusersession", user);

        token = "{\"profile\":\"" +user.getProfile()+ "\",\"uname\":\"" +user.getUname()+ "\",\"fname\":\"" +user.getFname()+ "\",";
        token += "\"usrid\":" +user.getUsrid()+ ",\"email\":\"" +user.getEmail()+ "\",\"locked\":\"" +user.getLocked()+ "\",\"logged\":\"" +user.getLogin()+ "\"}";

        return new JResponse(0, 0, "OK", token);
    }

    @PostMapping("/logout") // @RequestMapping @GetMapping
    public @ResponseBody JResponse logout(HttpSession session)
    {
        session.setAttribute("jusersession", null);
        return new JResponse(0, 0, "OK", "OK");
    }
}
