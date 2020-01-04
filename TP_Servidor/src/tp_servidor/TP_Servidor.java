package tp_servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; 

@SpringBootApplication
public class TP_Servidor {
    static String ipMaquinaBD;
    static LogicaServidor servidores = null;

    public static void main(String[] args){
        ThreadParaMulticast multicast = null;
        ControloLigacoes ligacoes = null;
        Boolean principal = false;
        Scanner sc = new Scanner(System.in);
        String scann;
        String ipDS; 

        if(args.length != 2){
            System.out.println("Sintaxe: java TP_Servidor ipDS ipMaquinaBD ");
            return;
        }
        ipDS = args[0];
        ipMaquinaBD = args[1];

        ServerSocket clientSocket = null;

        servidores = new LogicaServidor(ipDS,ipMaquinaBD, principal);
        clientSocket = servidores.criaNovoServidor(); 
        multicast = new ThreadParaMulticast(servidores);
        multicast.start();
        ligacoes = new ControloLigacoes(servidores);
        ligacoes.start();
        SpringApplication.run(TP_Servidor.class, args);
        do
        {
            System.out.print("\n-> ");
            scann = sc.next();
        }while(!scann.equalsIgnoreCase("sair"));
        
        try{
            servidores.terminaServidor();
            multicast.terminar();
            clientSocket.close();
            ligacoes.desliga();
        }catch (IOException ex) {
            Logger.getLogger(TP_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}