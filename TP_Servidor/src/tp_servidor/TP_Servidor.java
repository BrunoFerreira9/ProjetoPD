package tp_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
       
        Socket clientSocket;
        ServerSocket ss = new ServerSocket(servidores.cds.getPortoServer());
        
        System.out.println("Antes de aceitar o cliente!");
        clientSocket = ss.accept();            
        System.out.println("Estou à espera ....");
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());   
        out.flush();
        System.out.print(in.readObject().toString());
        clientSocket.close();

    }
        
    }
    
    

