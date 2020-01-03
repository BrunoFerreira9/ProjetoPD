package tp_servidor;

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
import static tp_servidor.ConstantesServer.BUFSIZE;
import static tp_servidor.ConstantesServer.ResolveMessages;
import static tp_servidor.ConstantesServer.portoDS;

public class ComunicacaoToDS implements myObserver,myObservable {
    
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
     
    boolean changed = false;
    List<myObserver> observers = new ArrayList<>();
    int msg;
    
    private LogicaServidor log;
    
    public ComunicacaoToDS(String endereco,LogicaServidor logica) {
        this.endereco = endereco;
        log = logica;
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
            HashMap <String,String> teste = ResolveMessages(resposta);
            numBD = Integer.parseInt(teste.get("numbd"));
            princ = principal = teste.get("principal").equalsIgnoreCase("sim");
            tratainformacao();
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
    
    public String getIpServer(){return endereco;}
    public int getPortoServer(){return portoServer;}
    public int getnumBD(){return numBD;}
    public boolean getprinc(){ return princ; }

    
    
    public void tratainformacao(){
        Thread recebepedidos; 
        recebepedidos = new Thread(new Runnable() {
            @Override
            public void run() {
                String pedidos;
                DatagramPacket p;
                boolean termina = true;
                while(termina){
                    try {
                        byte[] recbuf = new byte[BUFSIZE];
                        p=new DatagramPacket(recbuf,BUFSIZE);
                        socketUDP.receive(p);
                        pedidos = new String(p.getData(),0,p.getLength());
                        if(pedidos.equals("ping")){
                            socketUDP.send(new DatagramPacket("ativo".getBytes(),"ativo".length(),addr,ConstantesServer.portoPingsDS));
                        }
                        if(pedidos.equals("tipo | Servidor ; msg | terminar")){
                            System.out.println("Vou terminar...");
                            termina = false;
                            socketUDP.send(new DatagramPacket("tipo | Servidor ; msg | terminar".getBytes(),"tipo | Servidor ; msg | terminar".length(),addr,portoDS));
                            log.terminar();
                        }
                        if(!pedidos.equals("ping") && !pedidos.equals("tipo | Servidor ; msg | terminar")){
                            System.out.println(pedidos);
                            log.executapedidossincronizacao(pedidos);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ComunicacaoToDS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        recebepedidos.start();
    }
    
    @Override
    public void update(int msg) {
        /*  String mensagem = "";
        switch(msg){
            case ATUALIZAMUSICAS:
                mensagem = "tipo | atualizamusicas";
                break;
            case ATUALIZAPLAYLISTS:
                mensagem = "tipo | atualizaplaylists";
                break;
        }
        pout.println(mensagem);
        pout.flush();*/
    }

    @Override
    public void setChanged() {
        changed = true;
    }

    @Override
    public void notifyObservers() {
        observers.forEach((obs) -> {
            obs.update(msg);
        });
        changed = false;
    }

    @Override
    public void addObserver(myObserver obs) {
        observers.add(obs);
    }

    @Override
    public void removeObserver(myObserver obs) {
        observers.remove(obs);
    }
}
