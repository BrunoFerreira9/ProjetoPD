package tp_cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComunicacaoToServidor extends Observable implements InterfaceGestao {
    
    private int idUser;
    //ligacao TCP com o servidor
    
    Socket socketTCP ;
    String endereco;
    int porto;
    
    BufferedReader reader = null;
    PrintWriter out = null;
    
    String resposta;
    String pedido;
    
    public ComunicacaoToServidor(String endereco, int porto) {
        this.endereco = endereco;
        this.porto = porto;
    
    }
    
    public void inicializaTCP() throws IOException, ClassNotFoundException {
        try {
            socketTCP = new Socket(endereco,porto);
            reader  = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            out = new PrintWriter(socketTCP.getOutputStream());
            String msg = "Cliente a ligar";   
            out.println(msg);
            out.flush();
            System.out.println("Resultado: "+ reader.readLine());
        } catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket"+e);
        }
    }
    
    @Override
    public boolean efetuaRegisto(HashMap <String,String> user) {
       
        try {
            out = new PrintWriter(socketTCP.getOutputStream());
            pedido = "tipo | registo ; username | "+user.get("username") +" ; password | "+ user.get("password")+" ; nome | "+user.get("nome") + "\n";
            out.println(pedido);
            out.flush();
            reader = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            resposta = reader.readLine();
            
             HashMap <String,String> message = ResolveMessages(pedido);
            
               if(message.get("tipo").equals("resposta") && message.get("sucesso").equals("sim")){
                    idUser = Integer.parseInt(message.get("id").toString());
               }
                        
                               
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean efetuaLogin(HashMap <String,String> user) {
       
        try {
            out = new PrintWriter(socketTCP.getOutputStream());
            pedido = "tipo | login ; username | "+user.get("username") +" ;password | "+ user.get("password")+" \n";
            out.println(pedido);
            out.flush();
            reader = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            resposta = reader.readLine();
            
             HashMap <String,String> message = ResolveMessages(pedido);
            
               if(message.get("tipo").equals("resposta") && message.get("sucesso").equals("sim")){
                    System.out.println("Estou logado!!");
               }
            
          
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    @Override
    public boolean trataPedido(String mensagem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean atualizaMusicas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
