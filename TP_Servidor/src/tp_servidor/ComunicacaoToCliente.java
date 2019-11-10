package tp_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_servidor.ConstantesServer.ResolveMessages;

public class ComunicacaoToCliente implements myObserver {
    
    //LIGACAO TCP
    Socket socketCliente ;
    String endereco;
    int porto;
    
    //ObjectInputStream in = null;
    //ObjectOutputStream out = null;
    PrintWriter pout = null;
    BufferedReader in = null;
    
    String resposta;
    String pedido;
    
    myObserver observer;
    LogicaServidor servidor = null;
    
    public ComunicacaoToCliente(Socket cliente,LogicaServidor servidor){
       
        observer = this;
        this.servidor = servidor;
        this.socketCliente = cliente;
            
        this.endereco = cliente.getInetAddress().getHostName();
        this.porto = servidor.cds.getPortoServer();
        
         try {   
            pout = new PrintWriter(socketCliente.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ComunicacaoToCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
   
    
     public void recebeInformacaoTCP() {
        
        while (true) {
          
            synchronized (in) {
            
                try {
                    pedido = in.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(ComunicacaoToCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
              System.out.println("Recebi do Cliente: "+ pedido);
              HashMap <String,String> user = ResolveMessages(pedido);

                if(user.get("tipo").equals("registo")){
                    if(servidor.efetuaRegisto(user)){
                       pout.println("tipo | resposta ; msg | sucesso");
                       pout.flush();
                    }
                }else if(user.get("tipo").equals("login")){      
                     user.put("ip", socketCliente.getLocalAddress().getHostAddress());

                    if(servidor.efetuaLogin(user)){
                          String q = "Select idUtilizador from utilizador where username = \'" + user.get("username") + "\';";
                          String id = servidor.ligacao.executarSelect(q);

                          pout.println("tipo | resposta ; msg | sucesso ; id | " + id);
                          pout.flush();

                          servidor.adicionaCliente(socketCliente);
                    }
                }else if(user.get("tipo").equals("logout")){                 
                    if(servidor.efetuaLogout(user)){

                        pout.println("tipo | resposta ; msg | sucesso\n");
                        pout.flush();
                        servidor.removeCliente(socketCliente);
                    }
                }
                else if(user.get("tipo").equals("criaMusica") || user.get("tipo").equals("editaMusica") || user.get("tipo").equals("eliminaMusica") ||user.get("tipo").equals("ouvirMusica") || user.get("tipo").equals("addMusPlaylist") ){                 
                    if(servidor.trataMusicas(pedido)){
                        pout.println("tipo | resposta ; msg | sucesso\n");
                        pout.flush();                     
                    }else{
                         pout.println("tipo | resposta ; msg | insucesso\n");
                         pout.flush(); 
                    }
                }
                else if(user.get("tipo").equals("criaPlaylist") || user.get("tipo").equals("editaPlaylist") || user.get("tipo").equals("eliminaPlaylist") ||user.get("tipo").equals("ouvirPlaylist") || user.get("tipo").equals("eliminaMusicaPlaylist") ){                 
                    if(servidor.trataPlaylist(pedido)){
                        pout.println("tipo | resposta ; msg | sucesso\n");
                        pout.flush();                     
                    }else{
                         pout.println("tipo | resposta ; msg | insucesso\n");
                         pout.flush(); 
                    }
                }
            }
       }
    }
     
     //public void 

    @Override
    public void update(myObservable s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
