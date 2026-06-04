
package mx.com.ebcon.Portal.model;

public class Company
{
    private int cid;
    private String crfc;
    private String ccode;
    private String cdate;
    private String clock;
    private String cname;
    private String cpswd;
    private String cupdt;
    private String flcsd;
    private String flpfx;
    private String rtype;

    public Company(){}

    public int getCid(){ return this.cid; }
    public String getCrfc(){ return this.crfc; }
    public String getCcode(){ return this.ccode; }
    public String getCdate(){ return this.cdate; }
    public String getClock(){ return this.clock; }
    public String getCname(){ return this.cname; }
    public String getCpswd(){ return this.cpswd; }
    public String getCupdt(){ return this.cupdt; }
    public String getFlcsd(){ return this.flcsd; }
    public String getFlpfx(){ return this.flpfx; }
    public String getRtype(){ return this.rtype; }

    public void setCid(int cid){ this.cid = cid; }
    public void setCrfc(String value){ this.crfc = value; }
    public void setCcode(String value){ this.ccode = value; }
    public void setCdate(String value){ this.cdate = value; }
    public void setClock(String value){ this.clock = value; }
    public void setCname(String value){ this.cname = value; }
    public void setCpswd(String value){ this.cpswd = value; }
    public void setCupdt(String value){ this.cupdt = value; }
    public void setFlcsd(String value){ this.flcsd = value; }
    public void setFlpfx(String value){ this.flpfx = value; }
    public void setRtype(String value){ this.rtype = value; }
}

