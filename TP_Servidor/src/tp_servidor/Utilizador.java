
package tp_servidor;

import java.io.Serializable;

public class Utilizador implements Serializable{
   
   
    private final String username;
    private final String password;
    private final String nome;
    private  int idUtilizador;
    private boolean ativo;
   
    
    public Utilizador(String username, String password, String nome) {
        this.username = username;
        this.password = password;
        this.nome = nome;        
        ativo = false;
    }

    public int getIdUtilizador(){return idUtilizador;}
    
    public void setIdUtilizador(int val){
        idUtilizador = val;
    }

    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(){
        ativo = !ativo;
    }
    
    public String getNome() {
        return nome;
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public int getIdUser(){
        return idUtilizador;
    }
}
