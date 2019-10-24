package tp_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TP_Servidor {

    public static void main(String[] args) throws IOException {
        
        String ipMaquinaBD;
        String ipDS ;
        
         if(args.length != 2){
            System.out.println("Sintaxe: java TP_Servidor ipDS ipMaquinaBD ");
            return;
        }  
        ipDS = args[0];
        ipMaquinaBD = args[1];
        
        LogicaServidor servidores = new LogicaServidor(ipDS,ipMaquinaBD);
        BufferedReader in  = null;
        OutputStream out = null;
        
        Socket clientSocket;
        ServerSocket ss = new ServerSocket();
        
        while(true){
            System.out.print("11");
            clientSocket = ss.accept();            
            in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream())); 
            out = clientSocket.getOutputStream();
            String resp = in.readLine();
            System.out.print(resp);
        }
        
        }
    }
    

