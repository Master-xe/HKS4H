
package mx.com.ebcon.Portal.model;

public class User
{
    private int rolid;
    private int usrid;
    private String email;
    private String fname;
    private String login;
    private String paswd;
    private String uname;
    private String locked;
    private String logged;
    private String profile;

    public User(){}

    public int getRolid(){ return this.rolid; }
    public int getUsrid(){ return this.usrid; }
    public String getEmail(){ return this.email; }
    public String getFname(){ return this.fname; }
    public String getLogin(){ return this.login; }
    public String getPaswd(){ return this.paswd; }
    public String getUname(){ return this.uname; }
    public String getLocked(){ return this.locked; }
    public String getLogged(){ return this.logged; }
    public String getProfile(){ return this.profile; }

    public void setRolid(int value){ this.rolid = value; }
    public void setUsrid(int value){ this.usrid = value; }
    public void setEmail(String value){ this.email = value; }
    public void setFname(String value){ this.fname = value; }
    public void setLogin(String value){ this.login = value; }
    public void setPaswd(String value){ this.paswd = value; }
    public void setUname(String value){ this.uname = value; }
    public void setLocked(String value){ this.locked = value; }
    public void setLogged(String value){ this.logged = value; }
    public void setProfile(String value){ this.profile = value; }
}
