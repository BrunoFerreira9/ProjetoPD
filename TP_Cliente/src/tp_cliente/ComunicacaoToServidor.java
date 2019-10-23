package tp_cliente;


import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

public class ComunicacaoToServidor extends Observable implements InterfaceGestao {
    
    
    //ligacao TCP com o servidor
    
    Socket socketTCP ;
    String endereco;
    int porto;
    
    public ComunicacaoToServidor(String endereco, int porto) {
        this.endereco = endereco;
        this.porto = porto;
    
    }
    
    public void inicializaTCP() throws IOException {
        try {
            socketTCP = new Socket(endereco,porto);
           
        } catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket"+e);
        }
    }
}
