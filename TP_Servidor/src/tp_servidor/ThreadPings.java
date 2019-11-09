package tp_servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

class ThreadPings extends Thread {
    private DatagramSocket dtsocket;
    private DatagramPacket dtpacket;
    private DatagramPacket dtpacketDestino;

    public ThreadPings(DatagramSocket dtsocket, String IpDS) {
        this.dtsocket = dtsocket;
        byte[] data = "ativo".getBytes();
        try {
            dtpacketDestino = new DatagramPacket(data, data.length, InetAddress.getByName(IpDS), ConstantesServer.portoPingsDS);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ThreadPings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        while(true){
            try {
                dtpacket = new DatagramPacket(new byte[ConstantesServer.BUFSIZE], ConstantesServer.BUFSIZE);
                dtsocket.receive(dtpacket);
                
                dtpacket = dtpacketDestino;
                dtsocket.send(dtpacket);
            } catch (IOException ex) {
                Logger.getLogger(ThreadPings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
