package tp_cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_cliente.ConstantesCliente.ResolveMessages;

public class ComunicacaoToServidor implements InterfaceGestao, myObservable {
    
    private String userLogado;
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
    List<String> listamusicas = new ArrayList<String>();
    List<String> listaplaylist = new ArrayList<String>();
    
    public ComunicacaoToServidor(String endereco, int porto) {
            this.endereco = endereco;
            this.porto = porto;
    }
    
    public void inicializaTCP() {
        try {
            socketTCP = new Socket(endereco,porto);
            reader  = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            out = new PrintWriter(socketTCP.getOutputStream());
          
        } catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket"+e);
        }
    }
    
    public void restabeleceLigacao(String ip, int porto)  {
        try {
            socketTCP = new Socket(ip,porto);
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
        
        try {
            socketTCP.close();
            out.close();
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(ComunicacaoToServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                userLogado = user.get("username");
                logado = true;
                tratainformacao();
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
                return false;
    }
    
    @Override
    public boolean efetuaLogout(HashMap <String,String> user) {
        pedido = "tipo | logout ; username | "+user.get("username");
        out.println(pedido);
        out.flush();
        return true;
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
        mensagem += "utilizador | " + userLogado;
        System.out.println(mensagem);
        HashMap <String,String> pedido = ResolveMessages(mensagem);
       
        out.println(mensagem);
        out.flush();
        return true;
    }

    @Override
    public boolean trataPlaylist(String mensagem) {
        mensagem += "utilizador | " + userLogado;
        HashMap <String,String> pedido = ResolveMessages(mensagem);
       
        out.println(mensagem);
        out.flush();
        return true;
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
                        //System.out.println("Recebi do Servidor: "+ pedido);
                        HashMap <String,String> info = ResolveMessages(pedido);
                        switch(info.get("tipo"))
                        {
                            case "upload": 
                                Thread upmusica = new ThreadUpload(info.get("ficheiro"),endereco,Integer.parseInt(info.get("porto")));
                                upmusica.start();
                                break;
                            case "download":
                                Thread downmusica = new ThreadDownload(info.get("ficheiro"),socketTCP,info.get("ouvirMusica"));
                                downmusica.start();
                                break;
                            case "atualizamusicas":
                                msg = ConstantesCliente.ATUALIZAMUSICAS;
                                setChanged();
                                notifyObservers();
                                break;
                            case "atualizaplaylists":
                                msg = ConstantesCliente.ATUALIZAPLAYLISTS;
                                setChanged();
                                notifyObservers();
                                break;
                            case "listamusicas":
                                listamusicas.clear();
                                for(int i = 0; i< Integer.parseInt(info.get("tamanho")); i++)
                                {
                                    listamusicas.add(info.get("musica"+i));
                                }
                                break;
                            case "listamusicasfiltro":
                                System.out.println("-------- Pesquisa ---------");
                                for(int i = 0; i< Integer.parseInt(info.get("tamanho")); i++)
                                {
                                    System.out.println(info.get("musica"+i));
                                }
                                break;
                            case "listaplaylists":
                                listaplaylist.clear();
                                for(int i = 0; i< Integer.parseInt(info.get("tamanho")); i++)
                                {
                                   listaplaylist.add(info.get("playlist"+i));
                                }
                                break;
                            case "listaplaylistsfiltro":
                                System.out.println("-------- Pesquisa ---------");
                                for(int i = 0; i< Integer.parseInt(info.get("tamanho")); i++)
                                {
                                    System.out.println(info.get("playlist"+i));
                                }
                                break;
                            case "logout":
                                if(info.get("msg").equals("sucesso"))
                                    logado = false;
                            break;
                            case "terminaservidor": 
                                System.out.println("O servidor terminou, vou desligar!!");
                                socketTCP.close();
                                logado = false;
                                msg = ConstantesCliente.TERMINASERVIDOR;
                                setChanged();
                                notifyObservers();
                                break;
                        }
                    } catch (IOException ex) {   
                            return;
                            //msg = ConstantesCliente.DESLIGOU;
                            //setChanged();
                            //notifyObservers();    
                            
                        //continue;
                       // Logger.getLogger(ComunicacaoToServidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }

            
        });
        recebepedidos.start();
    }
 
}
