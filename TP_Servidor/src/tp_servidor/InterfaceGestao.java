package tp_servidor;

import java.util.HashMap;


public interface InterfaceGestao {
    
    public boolean efetuaRegisto(HashMap <String,String> user);
     public boolean efetuaLogin(HashMap <String,String> user);
     public boolean efetuaLogout(HashMap<String,String> user);
     public boolean trataMusicas(String mensagem);
     public boolean trataPlaylist(String mensagem);
     public boolean atualizaMusicas();
}
