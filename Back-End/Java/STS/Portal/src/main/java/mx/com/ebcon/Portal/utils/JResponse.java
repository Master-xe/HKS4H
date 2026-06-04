
package mx.com.ebcon.Portal.utils;

public class JResponse
{
    private final int code;
    private final int flag;
    private final String text;
    private final String type;
    // flag: -1 -> ERROR | 0 -> OK	| 1 -> INFO | 2 -> QUESTION | 3 -> WARNING	type: [SAP|SAT|SQL|SRV|ABAP]
    public JResponse(int code, int flag, String type, String text){ this.code = code; this.flag = flag; this.type = type; this.text = text; }
    public int getCode(){ return this.code; }
    public int getFlag(){ return this.flag; }
    public String getText(){ return this.text; }
    public String getType(){ return this.type; }
}
