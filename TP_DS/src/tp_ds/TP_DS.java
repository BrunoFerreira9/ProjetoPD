package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_DS extends Thread{
    Comunicacao com;
    private static List<Servidor> listservers = new ArrayList<>();
    private static int numClientes = 0,numbasedados = 0;
    public TP_DS(Comunicacao c){
        com = c;
    }
   
    @Override
    public void run(){
        byte []info = new byte[128];
        while(true){
            try {
                com.setPacket(new DatagramPacket(info,info.length));
                com.recebepedidos();
                com.verificapedido();
                com.enviaresposta();
            } catch (IOException ex) {
                Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static void main(String[] args){
        Comunicacao com = new Comunicacao(listservers,numClientes,numbasedados);
        try {
            com.criacomunicacao();
            new TP_DS(com).start();           
        } catch (SocketException ex) {
            Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
