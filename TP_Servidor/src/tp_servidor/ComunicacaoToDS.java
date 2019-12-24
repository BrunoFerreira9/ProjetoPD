package tp_servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_servidor.ConstantesServer.BUFSIZE;
import static tp_servidor.ConstantesServer.ResolveMessages;
import static tp_servidor.ConstantesServer.portoDS;

public class ComunicacaoToDS {
    
    //ligacao UDP com o DS
    DatagramSocket socketUDP = null;
    DatagramPacket packet = null;
    InetAddress addr = null;     
    private String endereco;
    
    
    //mensagem a enviar
    byte[] data = new byte[128];
    private String dados;
    
    //recebe o ip e porto do servidor a que ligam
    private int portoServer;
    private String ipServer;
    private int numBD;       
    private boolean princ;
     
    public ComunicacaoToDS(String endereco) {
        this.endereco = endereco;
     }
     
    public DatagramSocket inicializaUDP(Boolean principal) throws IOException {
        String resposta ;
        try{
            dados = "tipo | Servidor ; msg | ligar";
            addr = InetAddress.getByName(endereco);
            data= dados.getBytes();
            socketUDP = new DatagramSocket();
            portoServer = socketUDP.getLocalPort();
            
            packet = new DatagramPacket( data, data.length,addr,portoDS);
            socketUDP.send(packet);
            
            byte[] recbuf = new byte[BUFSIZE]; 
            DatagramPacket receivePacket=new DatagramPacket(recbuf,BUFSIZE);
            socketUDP.receive(receivePacket);
            
            resposta = new String(receivePacket.getData(),0,receivePacket.getLength());
            System.out.println(resposta);
            HashMap <String,String> teste = ResolveMessages(resposta);
            numBD = Integer.parseInt(teste.get("numbd"));
            princ = principal = teste.get("principal").equalsIgnoreCase("sim");
            
        }catch (UnknownHostException e){
            System.err.println ("Unable to resolve host");
        } catch (SocketException ex) {
            Logger.getLogger(ComunicacaoToDS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return socketUDP;
    }
    public void terminaServidor(){
       dados = "tipo | Servidor ; msg | terminar";
        data= dados.getBytes();
        packet = new DatagramPacket( data, data.length,addr,portoDS);
        try {
            socketUDP.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(ComunicacaoToDS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getIpServer(){return ipServer;}
    public int getPortoServer(){return portoServer;}
    public int getnumBD(){return numBD;}
    public boolean getprinc(){ return princ; }
}
