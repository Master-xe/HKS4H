
package mx.com.ebcon.Portal.utils;

public class JListResponse<T>
{
    public final T list;
    public final int total;
    public final boolean success;
    public final String emessage;

    public JListResponse(T list, int total, boolean success, String emessage){ this.list = list; this.total = total; this.success = success; this.emessage = emessage; }
    public T getList(){ return this.list; }
    public int getTotal(){ return this.total; }
    public boolean getSuccess(){ return this.success; }
    public String getEmessage(){ return this.emessage; }
}
