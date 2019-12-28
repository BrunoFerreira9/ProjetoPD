package tp_servidor;

import java.util.HashMap;


public interface InterfaceGestao {
    
    public boolean efetuaRegisto(HashMap <String,String> user);
     public boolean efetuaLogin(HashMap <String,String> user);
     public boolean efetuaLogout(HashMap<String,String> user);
     public boolean trataMusicas(HashMap<String,String> musica);
     public boolean trataPlaylist(HashMap <String,String> playlist);
     public boolean atualizaMusicas();
}
