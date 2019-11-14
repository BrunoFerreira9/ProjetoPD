package tp_ds;

import java.util.HashMap;
import java.util.StringTokenizer;

public class ConstantesDS {
    public static final int portoDS = 5001;
    public static final int portoPingsDS = 5002;
    public static final int BUFSIZE = 256;
    public static final int PINGTIME = 10000;

    
    public static HashMap<String,String> ResolveMessages(String message){
        StringTokenizer t,tokens = new StringTokenizer(message,";");
        String key,val;
        HashMap<String,String> messages = new HashMap<>();
        while (tokens.hasMoreElements()) {
            t = new StringTokenizer(tokens.nextElement().toString()," | ");  
            key = t.nextElement().toString();
            val = t.nextElement().toString();
            messages.put(key, val);
        }
        return messages;
    }
}
