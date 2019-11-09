/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmcam
 */
public class ThreadPedido extends Thread {
    private Comunicacao com;
    private HashMap<Integer, Socket> socketsPing;
    
    public ThreadPedido(Comunicacao c, HashMap<Integer, Socket> socketsPing){
        com = c;
        this.socketsPing = socketsPing;
    }
   
    @Override
    public void run(){
        byte []info = new byte[128];
        while(true){
            try {
                com.setPacket(new DatagramPacket(info,info.length));
                com.recebepedidos();
                int idserv = com.verificapedido();
                com.enviaresposta();
                com.criaTCPDePings(socketsPing, idserv);
            } catch (IOException ex) {
                Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
