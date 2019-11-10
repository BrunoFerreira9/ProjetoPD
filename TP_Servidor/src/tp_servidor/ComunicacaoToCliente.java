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
            try {

                pedido = in.readLine();

            } catch (IOException ex) {
                Logger.getLogger(ComunicacaoToCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Recebi do Cliente: "+ pedido);
            HashMap <String,String> user = ResolveMessages(pedido);

            switch (user.get("tipo")) {
                case "registo":
                    if(servidor.efetuaRegisto(user)){
                        pout.println("tipo | resposta ; msg | sucesso");
                        pout.flush();
                    }   break;
                case "login":
                    user.put("ip", socketCliente.getLocalAddress().getHostAddress());
                    if(servidor.efetuaLogin(user)){
                        String q = "Select idUtilizador from utilizador where username = \'" + user.get("username") + "\';";
                        String id = servidor.ligacao.executarSelect(q);

                        pout.println("tipo | resposta ; msg | sucesso ; id | " + id);
                        pout.flush();

                        servidor.adicionaCliente(socketCliente);
                    }   break;
                case "logout":
                    if(servidor.efetuaLogout(user)){

                        pout.println("tipo | resposta ; msg | sucesso\n");
                        pout.flush();
                        servidor.removeCliente(socketCliente);
                    }   break;
                case "criaMusica":
                case "editaMusica":
                case "eliminaMusica":
                case "ouvirMusica":
                case "addMusPlaylist":
                    if(servidor.trataMusicas(pedido)){
                        pout.println("tipo | resposta ; msg | sucesso\n");
                        pout.flush();
                    }else{
                        pout.println("tipo | resposta ; msg | insucesso\n");
                        pout.flush();
                    }   break;
                case "criaPlaylist":
                case "editaPlaylist":
                case "eliminaPlaylist":
                case "ouvirPlaylist":
                case "eliminaMusicaPlaylist":
                    if(servidor.trataPlaylist(pedido)){
                        pout.println("tipo | resposta ; msg | sucesso\n");
                        pout.flush();
                    }else{
                        pout.println("tipo | resposta ; msg | insucesso\n");
                        pout.flush();
                    }   break;
                default:
                    break;
            } 
       }
    }
     
     //public void 

    @Override
    public void update(myObservable s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
