package tp_servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_servidor.ConstantesServer.BUFSIZE;
import static tp_servidor.ConstantesServer.portoDS;

public class ComunicacaoToDS {
    
    //ligacao UDP com o DS
    DatagramSocket socketUDP = null;
    DatagramPacket packet = null;
    InetAddress addr = null;     
    private String endereco;
    
    //mensagem a enviar
    byte[] data = new byte[128];
    private String dados = "server";
    
    //recebe o ip e porto do servidor a que ligam
    private int portoServer;
    private String ipServer;
           
     
    public ComunicacaoToDS(String endereco) {
        this.endereco = endereco;
     }
     
    public void inicializaUDP() {
        
        try{
            addr = InetAddress.getByName(endereco);
            data= dados.getBytes();
            socketUDP = new DatagramSocket();
            
            packet = new DatagramPacket( data, data.length,addr,portoDS);
            
            
           
        }catch (UnknownHostException e){
            System.err.println ("Unable to resolve host");
        } catch (SocketException ex) {
            Logger.getLogger(ComunicacaoToDS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviaMensagem() throws IOException{
         socketUDP.send(packet);
    }
    
    public void esperaResposta() throws IOException{
        byte[] recbuf = new byte[BUFSIZE]; 
        DatagramPacket receivePacket=new DatagramPacket(recbuf,BUFSIZE);
        socketUDP.receive(packet);
               
        System.out.println(new String(recbuf).toString());
    }
    
    public String getIpServer(){return ipServer;}
    public int getPortoServer(){return portoServer;}
    
     
     
}
