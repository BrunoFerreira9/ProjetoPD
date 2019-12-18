package tp_cliente;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import static tp_cliente.ConstantesCliente.ResolveMessages;

public class ComunicacaoToServidor implements InterfaceGestao, myObservable {
    
    private int idUser;
    //ligacao TCP com o servidor
    
    Socket socketTCP ;
    String endereco;
    int porto;
    
    BufferedReader reader = null;
    PrintWriter out = null;
    
    String resposta;
    String pedido;
    
    boolean changed = false;
    List<myObserver> observers = new ArrayList<>();
    int msg;
    boolean logado = false; 
    
    public ComunicacaoToServidor(String endereco, int porto) {
            this.endereco = endereco;
            this.porto = porto;
        
    
    }
    
    public void inicializaTCP() throws IOException, ClassNotFoundException {
        try {
            socketTCP = new Socket(endereco,porto);    
            reader  = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            out = new PrintWriter(socketTCP.getOutputStream());
          
        } catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket"+e);
        }
    }
    
    @Override
    public boolean efetuaRegisto(HashMap <String,String> user) {
       
        try {
                      
            pedido = "tipo | registo ; username | "+user.get("username") +" ; password | "+ user.get("password")+" ; nome | "+user.get("nome");
            out.println(pedido);
            out.flush();
            resposta = reader.readLine();
            System.out.print(resposta);
                
                               
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public void terminarCliente(){
    
        pedido = "tipo | termina ";
        out.println(pedido);
        out.flush();
    }

    @Override
    public boolean efetuaLogin(HashMap <String,String> user) {
       
        try {
          
            pedido = "tipo | login ; username | "+user.get("username") +" ; password | "+ user.get("password");
            out.println(pedido);
            out.flush();
         
            resposta = reader.readLine();
            HashMap <String,String> message = ResolveMessages(resposta);
            
            if(message.get("tipo").equals("resposta") && message.get("msg").equals("sucesso")){
                idUser = Integer.parseInt(message.get("id").toString());
                logado = true;
                tratainformacao();
            }
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    @Override
    public boolean efetuaLogout(HashMap <String,String> user) {
       
        try {
            pedido = "tipo | logout ; username | "+user.get("username");
            out.println(pedido);
            out.flush();
         
            resposta = reader.readLine();
            
            HashMap <String,String> message = ResolveMessages(resposta);
            if(message.get("tipo").equals("resposta") && message.get("msg").equals("sucesso")){
                logado = false; 
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }
    
    public boolean sucesso() throws IOException{
        resposta = reader.readLine();
        HashMap <String,String> message = ResolveMessages(resposta);
        if(message.get("tipo").equals("resposta") && message.get("msg").equals("sucesso")){
             return true;
        }
        return false;
    }

    @Override
    public boolean trataMusicas(String mensagem){
     
        mensagem += "id | " + idUser;
        HashMap <String,String> pedido = ResolveMessages(mensagem);
        switch(pedido.get("tipo")){
            case "criaMusica" :
                out.println(mensagem);
                out.flush();
                return true;
            case "editaMusica":
                out.println(mensagem);
                out.flush();
                return true;
            case "eliminaMusica":
                out.println(mensagem);
                out.flush();
                return true;
            case "ouvirMusica":
                out.println(mensagem);
                out.flush();
                return true;
            case "addMusPlaylist":
                out.println(mensagem);
                out.flush();
                return true;
            case "listaMusicas":
                out.println(mensagem);
                out.flush();
                return true;
            default:
                return false;
                
        }
    }

    @Override
    public boolean trataPlaylist(String mensagem) {
        mensagem += "id | " + idUser;
        HashMap <String,String> pedido = ResolveMessages(mensagem);
        switch(pedido.get("tipo")){
            case "criaPlaylist":
                out.println(mensagem);
                out.flush();
                return true;
            case "editaPlaylist":
                out.println(mensagem);
                out.flush();
                return true;
            case "eliminaPlaylist":
                out.println(mensagem);
                out.flush();
                return true;
            case "ouvirPlaylist":
                out.println(mensagem);
                out.flush();
                return true;
            case "eliminaMusicaPlaylist":
                out.println(mensagem);
                out.flush();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean atualizaMusicas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                String pedido;
                while(logado){
                    try {
                        pedido = reader.readLine();
                        System.out.println("Recebi do Servidor: "+ pedido);
                        HashMap <String,String> info = ResolveMessages(pedido);
                        switch(info.get("tipo"))
                        {
                            case "upload": 
                                Thread upmusica = new ThreadUpload(info.get("ficheiro"),endereco,Integer.parseInt(info.get("porto")));
                                upmusica.start();
                                break;
                            case "download":
                                Thread downmusica = new ThreadDownload(info.get("ficheiro"),socketTCP);
                                downmusica.start();
                                break;
                            case "atualizamusicas":
                                msg = ConstantesCliente.ATUALIZAMUSICAS;
                                break;
                            case "atualizaplaylists":
                                msg = ConstantesCliente.ATUALIZAPLAYLISTS;
                                break;
                            
                        }
                        setChanged();
                        notifyObservers();
                    } catch (IOException ex) {
                        Logger.getLogger(ComunicacaoToServidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }

            
        });
        recebepedidos.start();
    }
    /*private void ouvirmusica(String ficheiro) throws IOException {
        try {
            InputStream musica = new FileInputStream(new File(ConstantesCliente.PATHLOCATION+"\\"+ficheiro));
            AudioStream audios = new AudioStream(musica);
            AudioPlayer.player.start(audios);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ComunicacaoToServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
