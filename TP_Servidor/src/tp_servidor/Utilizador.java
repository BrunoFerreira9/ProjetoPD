
package tp_servidor;

import java.io.Serializable;

public class Utilizador implements Serializable{
   
    static int contaUsers = 1;
    private final String username;
    private final String password;
    private final String nome;
    private final int idUtilizador;
    private boolean ativo;
   
    
    
    public Utilizador(String username, String password, String nome) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        idUtilizador = contaUsers++;
        ativo = false;
    }

    Utilizador(String get, String get0, String get1, String get2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
