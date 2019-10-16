package tp_cliente;


import java.util.Observable;

public class ComunicacaoToServidor extends Observable {
    
    
    //ligacao TCP com o servidor
    String endereco;
    int porto;
    
    public ComunicacaoToServidor(String endereco, int porto) {
        this.endereco = endereco;
        this.porto = porto;
    
    }
}
