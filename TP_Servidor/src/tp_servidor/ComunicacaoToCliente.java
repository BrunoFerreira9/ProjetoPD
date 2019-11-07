package tp_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_servidor.ConstantesServer.ResolveMessages;

public class ComunicacaoToCliente implements Observer {
    
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
    
    Observer obs;
    LogicaServidor servidor = null;
    
    public ComunicacaoToCliente(Socket cliente,LogicaServidor servidor){
       
        obs = this;
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
   

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     public void recebeInformacaoTCP() throws IOException {
             System.out.println("Recebi do Cliente: "+ in.readLine());
            pout.println("O servidor está te a ouvir");
              pout.flush();
        while (true) {
          
              pedido = in.readLine();
              System.out.println("Recebi do Cliente: "+ pedido);
              HashMap <String,String> user = ResolveMessages(pedido);

             
              if(user.get("tipo").equals("registo")){
                  if(servidor.efetuaRegisto(user)){
                      String q = "Select idUtilizador from utilizador where username = \'" + user.get("username") + "\';";
                      String id = servidor.ligacao.executarSelect(q);
                     
                      pout.println("tipo | resposta ; msg | sucesso ; id | " + id);
                      pout.flush();
                  }
              }else if(user.get("tipo").equals("login")){      
                   user.put("ip", socketCliente.getLocalAddress().getHostAddress());
                  if(servidor.efetuaLogin(user)){
                        pout.println("tipo | resposta ; msg | sucesso");
                        pout.flush();
                        servidor.adicionaCliente(socketCliente);
                  }
              }else if(user.get("tipo").equals("logout")){                 
                  if(servidor.efetuaLogout(user)){
                                          
                      pout.println("tipo | resposta ; msg | sucesso");
                      pout.flush();
                      servidor.removeCliente(socketCliente);
                  }
              }
       }
    }
     
     //public void 
}
