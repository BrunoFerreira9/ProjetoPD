package tp_cliente;

import java.util.HashMap;

public interface InterfaceGestao {
     public boolean efetuaRegisto(HashMap<String,String> user);
     public boolean efetuaLogin(HashMap<String,String> user);
     public boolean efetuaLogout(HashMap<String,String> user);
     public boolean trataPedido(String mensagem);
     public boolean atualizaMusicas();
     
    
}
