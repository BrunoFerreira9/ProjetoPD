package tp_cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_cliente.ComunicacaoToDS.ResolveMessages;

public class ComunicacaoToServidor implements InterfaceGestao,myObservable {
    
    private int idUser;
    //ligacao TCP com o servidor
    
    Socket socketTCP ;
    String endereco;
    int porto;
    
    BufferedReader reader = null;
    PrintWriter out = null;
    
    String resposta;
    String pedido;
    
    myObservable subject =null;
    
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
             System.out.print(resposta);
             HashMap <String,String> message = ResolveMessages(resposta);
            
               if(message.get("tipo").equals("resposta") && message.get("msg").equals("sucesso")){
                    System.out.println("Estou logado!!");
                    idUser = Integer.parseInt(message.get("id").toString());
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
     
        try {
            mensagem += "id | " + idUser;
            HashMap <String,String> pedido = ResolveMessages(mensagem);
            switch(pedido.get("tipo")){
                
                case "criaMusica" :
                    out.println(mensagem);
                    out.flush();
                    return sucesso();
                     
                case "editaMusica": 
                    out.println(mensagem);
                    out.flush(); 
                    return sucesso();
                case "eliminaMusica": 
                    out.println(mensagem);
                    out.flush();  
                    return sucesso();
                case "ouvirMusica": 
                    out.println(mensagem);
                    out.flush();
                    return sucesso();
                case "addMusPlaylist":
                    out.println(mensagem);
                    out.flush(); 
                    return sucesso();
                default:return false;
                
            }
        } catch (IOException ex) {
            Logger.getLogger(ComunicacaoToServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
         return false;
    }

    
     @Override
    public boolean trataPlaylist(String mensagem) {
        try {
             mensagem += "id | " + idUser;
            HashMap <String,String> pedido = ResolveMessages(mensagem);
            
            switch(pedido.get("tipo")){
                case "criaPlaylist":
                    out.println(mensagem);
                    out.flush(); 
                    return sucesso();
                case "editaPlaylist": 
                    out.println(mensagem);
                    out.flush(); 
                    return sucesso();
                case "eliminaPlaylist":
                    out.println(mensagem);
                    out.flush();
                    return sucesso();
                case "ouvirPlaylist":
                    out.println(mensagem);
                    out.flush(); 
                    return sucesso();
                case "eliminaMusicaPlaylist":
                    out.println(mensagem);
                    out.flush(); 
                    return sucesso();
                default:
                    return false;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ComunicacaoToServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean atualizaMusicas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
