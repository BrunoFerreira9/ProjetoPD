package tp_servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

class ThreadPings extends Thread {
    private DatagramSocket dtsocket;
    private DatagramPacket dtpacket;
    private DatagramPacket dtpacketDestino;
    private String IpDS;

    public ThreadPings( DatagramSocket s, String IpDS) throws SocketException {
        this.dtsocket = s;
        this.IpDS=IpDS;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                dtpacket = new DatagramPacket(new byte[ConstantesServer.BUFSIZE], ConstantesServer.BUFSIZE);
                dtsocket.receive(dtpacket);
                
                String dados = new String(dtpacket.getData(),0,dtpacket.getLength());
                
                System.out.print(dados);
                if(dados.equals("ping")){ 
                    byte[] data = "ativo".getBytes();
                    dtpacket.setData(data);
                    dtpacket.setLength(data.length);
                    dtsocket.send(dtpacket);
                }
            } catch (IOException ex) {
                Logger.getLogger(ThreadPings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
