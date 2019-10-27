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
    public boolean efetuaRegisto(Utilizador user) {
       
        try {
            out = new PrintWriter(socketTCP.getOutputStream());
            pedido = "tipo | registo ; nome | "+user.getNome() +
                    "; username | "+user.getUsername() +" ;password | "+ user.getPassword()+" \n";
            out.println(pedido);
            out.flush();
            reader = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            resposta = reader.readLine();
                               
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean efetuaLogin(Utilizador user) {
       
        try {
            out = new PrintWriter(socketTCP.getOutputStream());
            pedido = "tipo | login ; username | "+user.getUsername() +" ;password | "+ user.getPassword()+" \n";
            out.println(pedido);
            out.flush();
            reader = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            resposta = reader.readLine();
          
        } catch (IOException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
}
