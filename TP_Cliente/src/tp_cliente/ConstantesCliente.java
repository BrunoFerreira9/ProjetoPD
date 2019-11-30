package tp_cliente;

import java.util.HashMap;
import java.util.StringTokenizer;

public class ConstantesCliente {
    
    static final int MAX_SIZE = 4000;
    static final int TIMEOUT = 5000;
    static final int portoDS = 5001;
    public static final int BUFSIZE = 256;
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
    static final int ATUALIZAMUSICAS = 0;
    static final int ATUALIZAPLAYLISTS = 1;
}
