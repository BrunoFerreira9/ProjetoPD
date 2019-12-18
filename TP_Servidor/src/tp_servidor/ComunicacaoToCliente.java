package tp_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_servidor.ConstantesServer.ATUALIZAMUSICAS;
import static tp_servidor.ConstantesServer.ATUALIZAPLAYLISTS;
import static tp_servidor.ConstantesServer.ResolveMessages;

public class ComunicacaoToCliente implements myObserver {
    
    //LIGACAO TCP
    private Socket socketCliente;
    private DatagramSocket socketEnviaMulticast;
    private DatagramPacket packetMulticast;
    private String endereco;
    private int porto;
    
    //ObjectInputStream in = null;
    //ObjectOutputStream out = null;
    private PrintWriter pout = null;
    private BufferedReader in = null;
    
    private String resposta;
   private  String pedido;
    
    private myObserver observer;
    private LogicaServidor servidor = null;
    
    private boolean terminar = false;
    private boolean terminaServidor = false;
    
    public ComunicacaoToCliente(Socket cliente, LogicaServidor servidor){
       
        observer = this;
        this.servidor = servidor;
        this.socketCliente = cliente;
            
        this.endereco = cliente.getInetAddress().getHostName();
        this.porto = servidor.getCds().getPortoServer();
        
         try {
            socketEnviaMulticast = new DatagramSocket();
            pout = new PrintWriter(socketCliente.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ComunicacaoToCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getPorto() {
        return porto;
    }

    public void setPorto(int porto) {
        this.porto = porto;
    }

    public LogicaServidor getServidor() {
        return servidor;
    }

    public void setServidor(LogicaServidor servidor) {
        this.servidor = servidor;
    }

    public boolean isTerminar() {
        return terminar;
    }

    public void setTerminar(boolean terminar) {
        this.terminar = terminar;
    }

    public boolean isTerminaServidor() {
        return terminaServidor;
    }

    public void setTerminaServidor() {
        this.terminaServidor = true;
    }
    
    public void recebeInformacaoTCP() {
       
        while (!terminaServidor) {
            
            if (terminar) {
                try {
                    in.close();
                    pout.close();
                    socketCliente.close();
                    return;
                } catch (IOException ex) {
                    Logger.getLogger(ComunicacaoToCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            try {

                pedido = in.readLine();

            } catch (IOException ex) {
                Logger.getLogger(ComunicacaoToCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            HashMap <String,String> user = ResolveMessages(pedido);
            System.out.println("Recebi do Cliente: "+ pedido);
            try {
                trataPedido(user);
            } catch (IOException ex) {
                Logger.getLogger(ComunicacaoToCliente.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Envia pedido de mudança para o multicast
            byte[] data = pedido.getBytes();
            try {
                packetMulticast = new DatagramPacket(data, data.length, InetAddress.getByName(ConstantesServer.IPMULTICAST), ConstantesServer.portoMulticast);
                socketEnviaMulticast.send(packetMulticast);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ComunicacaoToCliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ComunicacaoToCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            
       }
       pout.println("tipo | servidor ; msg | terminou");
       pout.flush();
        
    }
    
    //Adicionei para testar
    public void trataPedido(HashMap<String, String> user) throws IOException {
        
        switch (user.get("tipo")) {
            case "termina":
                terminar = true; break;
            case "registo":
                if(servidor.efetuaRegisto(user)){
                    pout.println("tipo | resposta ; msg | sucesso");
                    pout.flush();
                }   
                break;
            case "login":
                user.put("ip", socketCliente.getLocalAddress().getHostAddress());
                if(servidor.efetuaLogin(user)){
                    String q = "Select idUtilizador from utilizador where username = \'" + user.get("username") + "\';";
                    String id = servidor.getLigacao().executarSelect(q);
                    
                    pout.println("tipo | resposta ; msg | sucesso ; id | " + id);
                    pout.flush();

                    servidor.adicionaCliente(socketCliente);
                    servidor.addObserver(this);
                }  
                break;
            case "logout":
                if(servidor.efetuaLogout(user)){
                    pout.println("tipo | resposta ; msg | sucesso");
                    pout.flush();
                    servidor.removeCliente(socketCliente);
                    servidor.removeObserver(observer);
                } 
                else{
                    pout.println("tipo | resposta ; msg | insucesso");
                    pout.flush();
                } 
                break;
            case "criaMusica":if(servidor.trataMusicas(pedido)){
                    pout.println("tipo | resposta ; msg | sucesso");
                    pout.flush();
                    Thread downMusica = new ThreadDownload(user.get("ficheiro"),socketCliente);
                    downMusica.start();
                }else{
                    pout.println("tipo | resposta ; msg | insucesso");
                    pout.flush();
                }  
            break;
            case "editaMusica":
                if(servidor.trataMusicas(pedido)){
                    pout.println("tipo | resposta ; msg | sucesso");
                    pout.flush();
                }else{
                    pout.println("tipo | resposta ; msg | insucesso");
                    pout.flush();
                }  
                break;
            case "eliminaMusica":
                if(servidor.trataMusicas(pedido)){
                    pout.println("tipo | resposta ; msg | sucesso");
                    pout.flush();
                }else{
                    pout.println("tipo | resposta ; msg | insucesso");
                    pout.flush();
                }   break;
            case "ouvirMusica":
                if(servidor.trataMusicas(pedido)){
                    pout.println("tipo | download ; msg | sucesso ; ficheiro | "+servidor.resposta+" ; ouvirMusica | sim");
                    pout.flush();
                    break;
                }else{
                    pout.println("tipo | resposta ; msg | insucesso");
                    pout.flush();
                }   break;
            case "upload": 
                Thread upmusica = new ThreadUpload(user.get("ficheiro"),endereco,Integer.parseInt(user.get("porto")));
                upmusica.start();
                break;
                
            case "addMusPlaylist":
                if(servidor.trataMusicas(pedido)){
                    pout.println("tipo | resposta ; msg | sucesso");
                    pout.flush();
                }else{
                    pout.println("tipo | resposta ; msg | insucesso");
                    pout.flush();
                }   break;
            case "listaMusicas":
                break;
            case "listaPlaylists": break;
            case "criaPlaylist":
            case "editaPlaylist":
            case "eliminaPlaylist":
            case "ouvirPlaylist":
            case "eliminaMusicaPlaylist":
                if(servidor.trataPlaylist(pedido)){
                    pout.println("tipo | resposta ; msg | sucesso");
                    pout.flush();
                }else{
                    pout.println("tipo | resposta ; msg | insucesso");
                    pout.flush();
                }   break;
            default:
                break;
        }
    }

    @Override
    public void update(int msg) {
        String mensagem = "";
        switch(msg){
            case ATUALIZAMUSICAS:
                mensagem = "tipo | atualizamusicas";
                break;
            case ATUALIZAPLAYLISTS:
                mensagem = "tipo | atualizaplaylists";
                break;
        }
        pout.println(mensagem);
        pout.flush();
    }
}
