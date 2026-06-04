
package mx.com.ebcon.pac;

import java.util.List;

public class Response
{
    public int code;
    public String acuse;
    public String detail;
    public String status;
    public String message;
    public List<String> uuids;

    public Response(int code, String status, String message, String detail, String acuse, List<String> uuids)
    {
        this.code = code;
        this.acuse = acuse;
        this.detail = detail;
        this.status = status;
        this.message = message;
        this.uuids = uuids;
    }
}
