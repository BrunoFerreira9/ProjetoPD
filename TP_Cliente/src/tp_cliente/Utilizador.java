
package tp_cliente;

import java.io.Serializable;

public class Utilizador implements Serializable{
   
    static int contaUsers = 1;
    String username;
    String password;
    String nome;
    int idUtilizador;

    public Utilizador(String username, String password, String nome) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        idUtilizador = contaUsers++;
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
    public int getIdUser(){return idUtilizador;}
}
