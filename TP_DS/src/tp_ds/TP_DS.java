package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_DS {

    public static void main(String[] args){
        
        HashMap <String,String> message1 = ResolveMessages("tipo | login ; username | pd_user ; password | pd_1234");
        HashMap <String,String> message2 =ResolveMessages("tipo | resposta ; sucesso | sim ; msg | Autenticação realizada com sucesso.");
        ComunicacaoToCliente ComToCli = new ComunicacaoToCliente();
        ComunicacaoToServidor ComToSer = new ComunicacaoToServidor();
        byte []info = new byte[128];
        String dados,resposta = null;
        HashMap <String,String> message;
        List<Servidor> listservers = new ArrayList<>();
        int numClientes = 0,numbasedados = 0;
        DatagramSocket sock;
        DatagramPacket pkt;
        
        try{
            sock = new DatagramSocket(ConstantesDS.portoDS);
            pkt = new DatagramPacket(info,info.length);
            while(true){
                sock.receive(pkt);
                dados = new String(pkt.getData(),0,pkt.getLength());
                message = ResolveMessages(dados);
                System.out.println("Recebi "+dados+" de "+pkt.getAddress()+" porto: "+pkt.getPort());
                switch(message.get("tipo")){
                    case "Servidor":
                                    System.out.print("Servidor");
                                    listservers.add(new Servidor(pkt.getAddress().getHostAddress(),pkt.getPort(),true, (listservers.isEmpty()))); 
                                    resposta = "tipo | resposta ; sucesso | sim ; numbd | "+numbasedados++;
                                  
                        break; 
                    case "Cliente":
                                    System.out.print("Cliente");
                                    if(numClientes < listservers.size()){
                                        Servidor aux = listservers.get(listservers.size()-1);
                                        resposta = "tipo | resposta ; sucesso | sim ; ip | "+aux.getIp()+" ; porto | "+aux.getPorto();
                                        numClientes++;
                                    }
                                    else{
                                        resposta = "tipo | resposta ; sucesso | não ; msg | Nenhum servidor disponivel";    
                                    }
                                   
                        break;
                }
                System.out.println(resposta);
                pkt.setData(resposta.getBytes());
                pkt.setLength(resposta.length());
                sock.send(pkt);
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
