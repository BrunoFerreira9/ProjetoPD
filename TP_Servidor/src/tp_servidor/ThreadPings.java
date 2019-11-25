package tp_servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_servidor.ConstantesServer.portoDS;

class ThreadPings extends Thread {
    private DatagramSocket dtsocket;
    private DatagramPacket dtpacket;
    private String IpDS;

    public ThreadPings( DatagramSocket s, String IpDS) throws SocketException {
        this.dtsocket = s;
        this.IpDS=IpDS;
       
    }
    
    @Override
    public void run(){
        while(true){
            try {
                try {
                    Thread.sleep(ConstantesServer.PINGTIME);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadPings.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                byte[] data = "ativo".getBytes();
                
                dtpacket = new DatagramPacket(data, data.length,InetAddress.getByName(IpDS),portoDS);
                dtsocket.send(dtpacket);
            } catch (IOException ex) {
                Logger.getLogger(ThreadPings.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
