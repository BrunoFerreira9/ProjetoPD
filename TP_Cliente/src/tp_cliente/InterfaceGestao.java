package tp_cliente;

import java.util.HashMap;

public interface InterfaceGestao {
     public boolean efetuaRegisto(HashMap<String,String> user);
     public boolean efetuaLogin(HashMap<String,String> user);
     public boolean efetuaLogout(HashMap<String,String> user);
     public boolean trataMusicas(HashMap<String,String> musica);
     public boolean trataMusicas(String mensagem);
     public boolean trataPlaylist(HashMap <String,String> playlist);
     public boolean trataPlaylist(String mensagem);     
    
}
