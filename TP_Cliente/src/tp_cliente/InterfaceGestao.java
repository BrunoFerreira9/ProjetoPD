package tp_cliente;

public interface InterfaceGestao {
     public boolean efetuaRegisto(Utilizador user);
     public boolean efetuaLogin(Utilizador user);
     public boolean trataPedido(String mensagem);
     public boolean atualizaMusicas();
     
    
}
