package tp_ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    public void recebepedidos() throws IOException{
        sock.receive(pkt);
        dados = new String(pkt.getData(),0,pkt.getLength());
        message = ResolveMessages(dados);
        System.out.println("Recebi "+dados+" de "+pkt.getAddress()+" porto: "+pkt.getPort());
    }
    
    public void verificapedido() throws SocketException{
        switch(message.get("tipo")){
            case "Servidor":
                System.out.print("Servidor - ");
                listservers.add(new Servidor(pkt.getAddress().getHostAddress(),pkt.getPort(),true, (listservers.isEmpty())));
                resposta = "tipo | resposta ; sucesso | sim ; numbd | "+numbasedados++;
                break;
            case "Cliente":
                System.out.print("Cliente - ");
                
                    //FAZER ROUND ROBIN PARA SABER QUAL O SERVIDOR A ATRIBUIR!!!!
                    Servidor aux = listservers.get(listservers.size()-1);
                    resposta = "tipo | resposta ; sucesso | sim ; ip | "+aux.getIp()+" ; porto | "+aux.getPorto();
                    numClientes++;
               
            break;
        }
    }
    
    public void enviaresposta() throws IOException{
        System.out.println(resposta);
        pkt.setData(resposta.getBytes());
        pkt.setLength(resposta.length());
        sock.send(pkt);
    }
    
    
    
}
