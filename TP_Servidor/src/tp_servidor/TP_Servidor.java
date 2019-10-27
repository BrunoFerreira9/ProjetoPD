package tp_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TP_Servidor {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        String ipMaquinaBD;
        String ipDS ;
        
         if(args.length != 2){
            System.out.println("Sintaxe: java TP_Servidor ipDS ipMaquinaBD ");
            return;
        }  
        ipDS = args[0];
        ipMaquinaBD = args[1];
        
        LogicaServidor servidores = new LogicaServidor(ipDS,ipMaquinaBD);
        BufferedReader in = null;
        OutputStream out = null;
        PrintWriter pout = null;
       
        Socket clientSocket;
        ServerSocket ss = new ServerSocket(servidores.cds.getPortoServer());
        System.out.println("ServerSocket no porto: "+ss.getLocalPort());
        while(true){
            System.out.println("Antes de aceitar o cliente!");
            clientSocket = ss.accept();            
            System.out.println("Estou à espera ....");
            System.out.println("Cliente: "+clientSocket.getInetAddress()+" no porto: "+clientSocket.getPort());
            out = clientSocket.getOutputStream(); 
            pout = new PrintWriter(out);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Recebi do Cliente: "+ in.readLine());
            pout.println("O servidor está te a ouvir");
            pout.flush();
        }
      //  clientSocket.close();

    }
        
    }
    
    

