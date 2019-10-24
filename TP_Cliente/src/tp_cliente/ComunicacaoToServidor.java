package tp_cliente;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComunicacaoToServidor extends Observable implements InterfaceGestao {
    
    
    //ligacao TCP com o servidor
    
    Socket socketTCP ;
    String endereco;
    int porto;
    
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    
    String resposta;
    String pedido;
    
    public ComunicacaoToServidor(String endereco, int porto) {
        this.endereco = endereco;
        this.porto = porto;
    
    }
    
    public void inicializaTCP() throws IOException, ClassNotFoundException {
        try {
            socketTCP = new Socket(endereco,porto);
           
             in = new ObjectInputStream(socketTCP.getInputStream());
                
            // CONNECT A PRINT STREAM 
             out = new ObjectOutputStream(socketTCP.getOutputStream());
            String msg = "Cliente a ligar";   
            out.writeObject(msg);
            out.flush();
                
            System.out.print((String)in.readObject());
                
        } catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket"+e);
        }
    }
    
    @Override
    public boolean efetuaRegisto(Utilizador user) {
       
        try {          

            out = new ObjectOutputStream(socketTCP.getOutputStream());

            pedido = "tipo | registo ; nome | "+user.getNome() +
                    "; username | "+user.getUsername() +" ;password | "+ user.getPassword()+" \n";
            out.writeObject(pedido);
            out.flush();
            out.reset();

            in = new ObjectInputStream(socketTCP.getInputStream());
           
            resposta = (String) in.readObject();
                               
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ComunicacaoToServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    @Override
    public boolean efetuaLogin(Utilizador user) {
       
        try {          

            out = new ObjectOutputStream(socketTCP.getOutputStream());

            pedido = "tipo | login ; username | "+user.getUsername() +" ;password | "+ user.getPassword()+" \n";
            out.writeObject(pedido);
            out.flush();
            out.reset();

            in = new ObjectInputStream(socketTCP.getInputStream());
         
            resposta = (String) in.readObject();
          
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ComunicacaoToServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
}
