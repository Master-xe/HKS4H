
package mx.com.ebcon.Portal.model;

public class CancellationsFilter
{
    private int userid;
    private int company;
    private boolean receipt;
    private boolean replied;

    private String startdate;
    private String receiver;
    private String cfdiuuid;
    private String enddate;

    public CancellationsFilter(){}

    public String getEnddate(){ return this.enddate; }
    public String getCfdiuuid(){ return this.cfdiuuid; }
    public String getReceiver(){ return this.receiver; }
    public String getStartdate(){ return this.startdate; }

    public int getUserid(){ return this.userid; }
    public int getCompany(){ return this.company; }
    public boolean getReceipt(){ return this.receipt; }
    public boolean getReplied(){ return this.replied; }

    public void setUserid(int value){ this.userid = value; }
    public void setCompany(int value){ this.company = value; }
    public void setReceipt(boolean value){ this.receipt = value; }
    public void setReplied(boolean value){ this.replied = value; }

    public void setEnddate(String value){ this.enddate = value; }
    public void setCfdiuuid(String value){ this.cfdiuuid = value; }
    public void setReceiver(String value){ this.receiver = value; }
    public void setStartdate(String value){ this.startdate = value; }
}

