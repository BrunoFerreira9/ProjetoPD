package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_ds.ConstantesDS.ResolveMessages;

class ThreadPingsParaServidores extends Thread {
    private DatagramSocket dtsocket;
    private DatagramPacket dtpack;
    private List<Servidor> listservers;
    String dados;
    
     HashMap <String,String> message;
     
    public ThreadPingsParaServidores(List<Servidor> listservers,DatagramSocket s) {
     
        dtsocket = s;
        
        if(listservers == null)
            this.listservers = new ArrayList<>();
        this.listservers = listservers;
    }
    
    @Override
    public void run(){
        byte[] data = "ping".getBytes();
        while(true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(listservers.isEmpty())
                continue;
            
            try {
                for(Servidor s : listservers){
                    dtpack = new DatagramPacket(data, data.length, InetAddress.getByName(s.getIp()), s.getPorto());
                    dtsocket.send(dtpack);
                    
                    dtpack = new DatagramPacket(new byte[ConstantesDS.BUFSIZE], ConstantesDS.BUFSIZE);
                    dtsocket.receive(dtpack);
                    dados = new String(dtpack.getData(),0,dtpack.getLength());
                    message = ResolveMessages(dados);
                    if(message.get("tipo").equals("ping") && message.get("resposta").equals("ativo"))
                        System.out.print("Servidor " + s.getIp() + " ativo.\n");
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
