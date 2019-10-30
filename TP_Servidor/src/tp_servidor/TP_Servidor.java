package tp_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_Servidor {

    public static void main(String[] args){
        
        String ipMaquinaBD;
        String ipDS ;
        
         if(args.length != 2){
            System.out.println("Sintaxe: java TP_Servidor ipDS ipMaquinaBD ");
            return;
        }  
        ipDS = args[0];
        ipMaquinaBD = args[1];
        
        LogicaServidor servidores = null;
        ServerSocket ss = null;
        
        
        PrintWriter pout = null;
        BufferedReader in = null;
       
       
        Socket clientSocket;
        
        try{
              servidores = new LogicaServidor(ipDS,ipMaquinaBD);
               ss = new ServerSocket(servidores.cds.getPortoServer());
        } catch (IOException ex) {
            Logger.getLogger(TP_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("ServerSocket no porto: "+ss.getLocalPort());
        while(true){
            System.out.println("Antes de aceitar o cliente!");
            try{
                clientSocket = ss.accept();            
                System.out.println("Estou à espera ....");
                System.out.println("Cliente: "+clientSocket.getInetAddress()+" no porto: "+clientSocket.getPort());
               
                pout = new PrintWriter(clientSocket.getOutputStream());
                 
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Recebi do Cliente: "+ in.readLine());
                pout.println("O servidor está te a ouvir");
                pout.flush();
                while(true){
                
                    String pedido = in.readLine();
                    
                    HashMap <String,String> message = ResolveMessages(pedido);
                   
                    
                    if(message.get("tipo").equals("registo")){
                                            
                        if(servidores.efetuaRegisto(new Utilizador(message.get("username"),message.get("password"),message.get("nome")))){
                               pout.println("sucesso");
                                pout.flush();
                        }
                    }
                    
                }
                
            } catch (IOException ex) {
                Logger.getLogger(TP_Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
      //  clientSocket.close();

    }
     public static HashMap<String,String> ResolveMessages(String message){
        StringTokenizer t,tokens = new StringTokenizer(message,";");
        String key,val;
        HashMap<String,String> messages = new HashMap<>();
        while (tokens.hasMoreElements()) {
            t = new StringTokenizer(tokens.nextElement().toString()," | ");  
            key = t.nextElement().toString();
            val = t.nextElement().toString();
            messages.put(key, val);
        }
        return messages;
    }
        
    }
    
    

