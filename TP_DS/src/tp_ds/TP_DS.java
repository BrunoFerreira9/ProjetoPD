package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_DS {

    public static void main(String[] args){
        HashMap <String,String> message1 = ResolveMessages("tipo | login ; username | pd_user ; password | pd_1234");
        HashMap <String,String> message2 =ResolveMessages("tipo | resposta ; sucesso | sim ; msg | Autenticação realizada com sucesso.");
        System.out.println(message1);
        System.out.println(message2);
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
                System.out.println("Recebi "+new String(pkt.getData(),0,pkt.getLength())+" de "+pkt.getAddress()+" porto: "+pkt.getPort());
            }
        }catch(SocketException e) {
            System.out.println ("Error "+e);
        } catch (IOException ex) {
            Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
        } 
       
    }
    
    public static HashMap<String,String> ResolveMessages(String message){
        StringTokenizer t,tokens = new StringTokenizer(message,";");
        String key,val;
        HashMap<String,String> messages = new HashMap<>();
        while (tokens.hasMoreElements()) {
            t = new StringTokenizer(tokens.nextElement().toString()," | ");  
            key = t.nextElement().toString();
            val = t.nextElement().toString();
            messages.put(key, val);
        }
        return messages;
    }
    
}
