package tp_servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_servidor.ConstantesServer.ResolveMessages;

public class ThreadParaMulticast extends Thread {
    private int numeroServidores;
    private MulticastSocket mtsock;
    private DatagramPacket dtpack;
    private String myIP;
    private ComunicacaoToCliente comunicacaoCliente;
    private boolean terminar = false;
    private boolean principal;
    private final String novo = "novo";
    private final String sair = "sair";
    private final String atualizado = "atualizado";
    
    public ThreadParaMulticast(ComunicacaoToCliente comunicacao, Boolean principal){
        comunicacaoCliente = comunicacao;
        this.principal = principal;
        
        try {
            mtsock = new MulticastSocket(ConstantesServer.portoMulticast);
            InetAddress group = InetAddress.getByName(ConstantesServer.IPMULTICAST);
            mtsock.joinGroup(group);
            myIP = InetAddress.getLocalHost().getHostAddress();
            mtsock.setSoTimeout(10000);
        } catch (IOException ex) {
            Logger.getLogger(ThreadParaMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void terminar(){
        terminar = true;
        mtsock.close(); //Confirmar se pode ser assim
    }
    
    @Override
    public void run(){
        byte[] data = novo.getBytes();
        
        if(!principal){
            //Os servidores não principais dão informação que chegaram ao multicast
            try {
                dtpack = new DatagramPacket(data, data.length, InetAddress.getByName(ConstantesServer.IPMULTICAST), ConstantesServer.portoMulticast);
                mtsock.send(dtpack);
            } catch (IOException ex) {
                Logger.getLogger(ThreadParaMulticast.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        while(!terminar){
            dtpack = new DatagramPacket(new byte[ConstantesServer.BUFSIZE], ConstantesServer.BUFSIZE);
            try {
                mtsock.receive(dtpack);
                
                String pedido = new String(dtpack.getData(), 0, dtpack.getLength());
                System.out.println(pedido);
                
                if(pedido.equalsIgnoreCase(novo)){
                    numeroServidores++;
                    continue;
                }
                
                if(pedido.equalsIgnoreCase(sair)){
                    numeroServidores--;
                    continue;
                }
                
                
                if(dtpack.getAddress().getHostAddress().equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()) && dtpack.getPort() == comunicacaoCliente.getServidor().getCds().getPortoServer()+2)
                {
                    int aux  = numeroServidores; //Menos o servidor que enviou;
                    do {
                        for(int i = 0; i < aux; i++){
                            try{
                                dtpack = new DatagramPacket(new byte[ConstantesServer.BUFSIZE], ConstantesServer.BUFSIZE);
                                mtsock.receive(dtpack);
                                pedido = new String(dtpack.getData(), 0, dtpack.getLength());
                                if(pedido.equalsIgnoreCase(atualizado))
                                    aux--;
                                else
                                    System.out.println("Um dos servidores respondeu mal");
                            }catch(SocketTimeoutException ex){
                                System.out.println("Um servidor não respondeu 'a atualizacao...\nA reenviar informação!");
                                data = pedido.getBytes();
                                dtpack = new DatagramPacket(data, data.length, InetAddress.getByName(ConstantesServer.IPMULTICAST), ConstantesServer.portoMulticast);
                                mtsock.send(dtpack); //Caso haja timeout após 10 segundos volta a enviar os dados
                                break;
                            }
                        }
                    }while(aux != 0);
                }
                else{
                    if(pedido.equalsIgnoreCase(atualizado))
                        continue;
                    pedido += " ; multicast | sim ; ip | "+dtpack.getAddress().getHostAddress()+" ; porto | "+dtpack.getPort();
                    HashMap <String,String> user = ResolveMessages(pedido);
                    comunicacaoCliente.trataPedido(user);
                    
                    data = atualizado.getBytes();
                    dtpack = new DatagramPacket(data, data.length, InetAddress.getByName(ConstantesServer.IPMULTICAST), ConstantesServer.portoMulticast);
                    mtsock.send(dtpack);
                }                               
            } catch(SocketTimeoutException ex){
                continue; // O Netbeans é chato mas fica aqui isto pq não é ele o programador.
            } catch (IOException ex) {
                Logger.getLogger(ThreadParaMulticast.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
     
        data = sair.getBytes();
        
        try {
            dtpack = new DatagramPacket(data, data.length, InetAddress.getByName(ConstantesServer.IPMULTICAST), ConstantesServer.portoMulticast);
            mtsock.send(dtpack);
        } catch (IOException ex) {
            Logger.getLogger(ThreadParaMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
