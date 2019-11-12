package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_DS extends Thread{
    Comunicacao com;
    private static List<Servidor> listservers = new ArrayList<>();
    private static int numClientes = 0,numbasedados = 0;
    
    public static void main(String[] args){
        DatagramSocket dtsocket = null;
        try {
            dtsocket = new DatagramSocket(ConstantesDS.portoPingsDS);
        } catch (SocketException ex) {
            Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(dtsocket == null){
            System.out.println("Erro na criacao do socket para os pings!");
            return;
        }
        
        Comunicacao com = new Comunicacao(listservers, numClientes, numbasedados);
        try {
            com.criacomunicacao();
            new ThreadRecebePedidos(com).start();
            //new ThreadPingsParaServidores(listservers,com.getSock()).start();
        } catch (SocketException ex) {
            Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
