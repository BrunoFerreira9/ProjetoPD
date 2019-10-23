package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_DS {

    public static void main(String[] args){
        ComunicacaoToCliente ComToCli = new ComunicacaoToCliente();
        ComunicacaoToServidor ComToSer = new ComunicacaoToServidor();
        byte []info = new byte[128];
        
        DatagramSocket sock;
        DatagramPacket pkt;
        try{
            sock = new DatagramSocket(ConstantesDS.portoDS);
            pkt = new DatagramPacket(info,info.length);
            while(true){
                sock.receive(pkt);
                
                System.out.println("Recebi "+new String(pkt.getData(),0,pkt.getLength()).toString()+" de "+pkt.getAddress()+" porto: "+pkt.getPort());
            }
        }catch(SocketException e) {
            System.out.println ("Error "+e);
        } catch (IOException ex) {
            Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void ResolveMessages(String message){
        
    }
    
}
