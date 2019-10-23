package tp_servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ComunicacaoToCliente implements Observer {
    
    //LIGACAO TCP
     Socket socketTCP ;
    String endereco;
    int porto;
    
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    
    String resposta;
    String pedido;
    
    public ComunicacaoToCliente(){
    
    
    }
     public void inicializaTCP() throws IOException {
        try {
            socketTCP = new Socket(endereco,porto);
           
        } catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket"+e);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
