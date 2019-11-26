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
    private DatagramSocket dtsocketPing;
    private DatagramPacket dtpack;
    private List<Servidor> listservers;
    String dados;
    
    HashMap <String,String> message;
     
    public ThreadPingsParaServidores(List<Servidor> listservers) {
     
        try {
            dtsocketPing = new DatagramSocket(ConstantesDS.portoPingsDS);
        } catch (SocketException ex) {
            Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(listservers == null)
            this.listservers = new ArrayList<>();
        this.listservers = listservers;
    }
    
    @Override
    public void run(){
        String resposta = "";
        
        boolean existePrimario = true;
        
        try {
            dtsocketPing.setSoTimeout(ConstantesDS.PINGTIME);
        } catch (SocketException ex) {
            Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
        }

        while(!dtsocketPing.isClosed()){
            try {
                Thread.sleep(ConstantesDS.PINGTIME);
                dtpack = new DatagramPacket(new byte[ConstantesDS.BUFSIZE],ConstantesDS.BUFSIZE);
                dtsocketPing.receive(dtpack);
                resposta = new String(dtpack.getData(),0,dtpack.getLength());
                for(Servidor s : listservers){
                    if(dtpack.getAddress().getHostName().equals(s.getIp())){
                        if(resposta.equalsIgnoreCase("ativo")){ 
                            System.out.print("Servidor " + s.getIp() + " ativo.\n");
                            s.setAtivo(true);
                        }
                        else s.setAtivo(false);
                    }
                }
            } catch (InterruptedException | IOException ex) {
                continue;
            }
        }
    }
    
    
    
}
