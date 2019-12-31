package tp_cliente;

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
import static tp_cliente.ConstantesCliente.BUFSIZE;
import static tp_cliente.ConstantesCliente.ResolveMessages;
import static tp_cliente.ConstantesCliente.portoDS;

public class ComunicacaoToDS implements myObserver,myObservable{
    
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
    
    boolean changed = false;
    List<myObserver> observers = new ArrayList<>();
    int msg;
           
     
    public ComunicacaoToDS(String endereco) {
        this.endereco = endereco;
     }
     
    public boolean inicializaUDP() throws IOException {
        String resposta;
        HashMap <String,String> message;
        try{
            addr = InetAddress.getByName(endereco);
            
            socketUDP = new DatagramSocket();
            dados = "tipo | Cliente ; msg | ligar";
            data= dados.getBytes();
            packet = new DatagramPacket( data, data.length,addr,portoDS);
            
            socketUDP.send(packet);
            
            byte[] recbuf = new byte[BUFSIZE]; 
            DatagramPacket receivePacket=new DatagramPacket(recbuf,BUFSIZE);
            socketUDP.receive(receivePacket);
            resposta = new String(receivePacket.getData(),0,receivePacket.getLength());
            
            message = ResolveMessages(resposta);
            switch(message.get("tipo")){
                case "resposta":
                    if(message.get("sucesso").equals("nao")){
                         System.out.println("Não existe servidores ativos");
                          return false;
                    }else if(message.get("sucesso").equals("sim")){
                        ipServer = message.get("ip");
                        portoServer = Integer.parseInt(message.get("porto"));                      
                       
                        return true;
                    }
                    
                    break;
            }
            
           tratainformacao();
        }catch (UnknownHostException e){
            System.err.println ("Unable to resolve host");
        } catch (SocketException ex) {
            Logger.getLogger(ComunicacaoToDS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
   
    public void enviaPergunta(){
        String resposta;
        HashMap <String,String> message;
        
        String dados2 = "tipo | Cliente ; msg | desligou";
        
        packet = new DatagramPacket( dados2.getBytes(), dados2.getBytes().length,addr,portoDS);
            
        try {
            socketUDP.send(packet);
        } catch (IOException ex) {
            Logger.getLogger(ComunicacaoToDS.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        byte[] recbuf = new byte[BUFSIZE]; 
        DatagramPacket receivePacket=new DatagramPacket(recbuf,BUFSIZE);
        try {
            socketUDP.receive(receivePacket);
        } catch (IOException ex) {
            Logger.getLogger(ComunicacaoToDS.class.getName()).log(Level.SEVERE, null, ex);
        }
        resposta = new String(receivePacket.getData(),0,receivePacket.getLength());

        message = ResolveMessages(resposta);
        switch(message.get("tipo")){
            case "resposta":
                if(message.get("msg").equals("novaLigacao")){                    
                    ipServer = message.get("ip");
                    portoServer = Integer.parseInt(message.get("porto"));

                    System.out.println("passei aqui");
                    msg = ConstantesCliente.MUDASERVIDOR;
                    setChanged();
                    notifyObservers();
                   
                }
                break;
        }
    }
    
    public String getIpServer(){return ipServer;}
    public int getPortoServer(){return portoServer;}
    
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
    
    public void tratainformacao(){
        Thread recebepedidos; 
        recebepedidos = new Thread(new Runnable() {
            @Override
            public void run() {
                 HashMap <String,String> message;
                String pedidos;
                DatagramPacket p;
                boolean termina = true;
                while(termina){
                    System.out.println("ola");
                    try {
                        byte[] recbuf = new byte[BUFSIZE];
                        p=new DatagramPacket(recbuf,BUFSIZE);
                        socketUDP.receive(p);
                        pedidos = new String(p.getData(),0,p.getLength());    
                        
                        message = ResolveMessages(pedidos);
                        
                        switch(message.get("msg")){
                            case "novaLigacao":  
                                System.out.println("aqqui  " + message);
                                ipServer = message.get("ip");
                                portoServer = Integer.parseInt(message.get("porto"));
                                msg = ConstantesCliente.MUDASERVIDOR;
                                setChanged();
                                notifyObservers();                              
                        }                        
                    } catch (IOException ex) {
                        Logger.getLogger(ComunicacaoToDS.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        recebepedidos.start();
    }
}
