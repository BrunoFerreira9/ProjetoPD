package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_ds.ConstantesDS.ResolveMessages;

public class Comunicacao {
    DatagramSocket sock;
    DatagramPacket pkt;
    String dados,resposta = null;
    HashMap <String,String> message;
    byte []info = new byte[128];
    List<Servidor> listservers;
    private  int numClientes,numbasedados;
    
    Comunicacao(List<Servidor> listservers, int numClientes, int numbasedados) {
        this.numClientes = numClientes;
        this.numbasedados = numbasedados;
        this.listservers = listservers;
    }
    
    
    public void criacomunicacao() throws SocketException{
        sock = new DatagramSocket(ConstantesDS.portoDS);
    }
    
   
    public void setPacket(DatagramPacket p){
        pkt = p;
    }
    
    public void criaTCPDePings(HashMap<Integer, Socket> socketsPing, int idServ){
        ServerSocket ss;
        try {
            ss = new ServerSocket(ConstantesDS.portoDS);
            Socket sck = ss.accept();
            socketsPing.put(idServ, sck);
        } catch (IOException ex) {
            Logger.getLogger(Comunicacao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void recebepedidos() throws IOException{
        sock.receive(pkt);
        dados = new String(pkt.getData(),0,pkt.getLength());
        message = ResolveMessages(dados);
        System.out.println("Recebi "+dados+" de "+pkt.getAddress()+" porto: "+pkt.getPort());
    }
    
    public int verificapedido() throws SocketException{
        Servidor serv = null;
        
        switch(message.get("tipo")){
            case "Servidor":
                System.out.print("Servidor - ");
                serv = new Servidor(pkt.getAddress().getHostAddress(),pkt.getPort(),true, (listservers.isEmpty()));
                listservers.add(serv);
                resposta = "tipo | resposta ; sucesso | sim ; numbd | "+numbasedados++;
                break;
            case "Cliente":
                System.out.print("Cliente - ");
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
        
        return serv == null ? -1 : serv.getId();
    }
    
    public void enviaresposta() throws IOException{
        System.out.println(resposta);
        pkt.setData(resposta.getBytes());
        pkt.setLength(resposta.length());
        sock.send(pkt);
    }
    
}
