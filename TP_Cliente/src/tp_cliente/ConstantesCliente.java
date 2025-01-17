package tp_cliente;

import java.util.HashMap;
import java.util.StringTokenizer;

public class ConstantesCliente {
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 5000;
    public static final int portoDS = 5001;
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
    public static final int ATUALIZAMUSICAS = 0;
    public static final int ATUALIZAPLAYLISTS = 1;
    public static final int TERMINASERVIDOR = 2;
    public static final int MUDASERVIDOR = 3;
        public static final int DESLIGOU = 4;

    public static final String PATHLOCATION = "C:\\ClientePD";
}
