

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class ThreadPingsParaServidores extends Thread {
    private DatagramSocket dtsocket;
    private DatagramPacket dtpack;
    private List<Servidor> listservers;

    public ThreadPingsParaServidores(List<Servidor> listservers) {
        try {
            dtsocket = new DatagramSocket(ConstantesDS.portoPingsDS);
            dtsocket.setSoTimeout(5000);
        } catch (SocketException ex) {
            System.out.println("Erro ao criar o socket para os pings");
        }
        
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
                    if(s.isAtivo()){
                        dtpack = new DatagramPacket(data, data.length, InetAddress.getByName(s.getIp()), s.getPorto());
                        dtsocket.send(dtpack);
                        try {
                            dtpack = new DatagramPacket(new byte[ConstantesDS.BUFSIZE], ConstantesDS.BUFSIZE);
                            dtsocket.receive(dtpack);
                            if(!TP_DS.existeprincipal){
                                s.setPrincipal(true);
                                System.out.println(s.getIp()+" passei a ser o principal!");
                                TP_DS.existeprincipal = true;
                            }
                            System.out.println("O servidor de IP " + s.getIp() + " enviou ao ping do DS: " + dtpack.getData().toString());
                        } catch (SocketTimeoutException e) {
                           System.out.println("Deixou de receber ping do servidor "+s.getIp());
                           if(s.isPrincipal()){
                               s.setPrincipal(false);
                               System.out.println(s.getIp()+" deixou de ser principal");
                               TP_DS.existeprincipal = false;
                           }
                           s.setAtivo(false);
                           continue;
                        }
                    }
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}