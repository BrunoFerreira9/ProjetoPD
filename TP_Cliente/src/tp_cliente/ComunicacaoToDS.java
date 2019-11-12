package tp_cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_cliente.ConstantesCliente.BUFSIZE;
import static tp_cliente.ConstantesCliente.ResolveMessages;
import static tp_cliente.ConstantesCliente.portoDS;

public class ComunicacaoToDS {
    
    //ligacao UDP com o DS
    DatagramSocket socketUDP = null;
    DatagramPacket packet = null;
    InetAddress addr = null;     
    private String endereco;
    
    //mensagem a enviar
    byte[] data = new byte[128];
    private String dados = "tipo | Cliente";
    
    //recebe o ip e porto do servidor a que ligam
    private int portoServer;
    private String ipServer;
           
     
    public ComunicacaoToDS(String endereco) {
        this.endereco = endereco;
     }
     
    public void inicializaUDP() throws IOException {
        String resposta;
        HashMap <String,String> message;
        try{
            addr = InetAddress.getByName(endereco);
            data= dados.getBytes();
            socketUDP = new DatagramSocket();
            
            packet = new DatagramPacket( data, data.length,addr,portoDS);
            
            socketUDP.send(packet);
            
            byte[] recbuf = new byte[BUFSIZE]; 
            DatagramPacket receivePacket=new DatagramPacket(recbuf,BUFSIZE);
            socketUDP.receive(receivePacket);
            resposta = new String(receivePacket.getData(),0,receivePacket.getLength());
            
            System.out.println(resposta);
            
            
            message = ResolveMessages(resposta);
            switch(message.get("tipo")){
                case "resposta":
                                if(message.containsValue("sim")){
                                    ipServer = message.get("ip");
                                    portoServer = Integer.parseInt(message.get("porto"));
                                }
                                else{
                                    System.out.println(message.get("msg"));
                                }
                                break;
            }
            
           
        }catch (UnknownHostException e){
            System.err.println ("Unable to resolve host");
        } catch (SocketException ex) {
            Logger.getLogger(ComunicacaoToDS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
    public String getIpServer(){return ipServer;}
    public int getPortoServer(){return portoServer;}
    
     
}
