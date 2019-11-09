package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_DS {
    private Comunicacao com;
    private static List<Servidor> listservers = new ArrayList<>();
    private static HashMap<Integer, Socket> socketsPing = new HashMap<>();
    private static int numClientes = 0,numbasedados = 0;
    
    public static void main(String[] args){
        Comunicacao com = new Comunicacao(listservers, numClientes, numbasedados);
        try {
            com.criacomunicacao();
            new ThreadPedido(com, socketsPing).start();
            new ThreadPings((List<Socket>)socketsPing.values()).start();
            
        } catch (SocketException ex) {
            Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
