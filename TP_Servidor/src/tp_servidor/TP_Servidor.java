package tp_servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_Servidor {

    public static void main(String[] args){
        Boolean principal = false;
        Scanner sc = new Scanner(System.in);
        String scann;
        String ipMaquinaBD;
        String ipDS; 

        if(args.length != 2){
            System.out.println("Sintaxe: java TP_Servidor ipDS ipMaquinaBD ");
            return;
        }
        ipDS = args[0];
        ipMaquinaBD = args[1];

        LogicaServidor servidores = null;
        ServerSocket clientSocket = null;

        servidores = new LogicaServidor(ipDS,ipMaquinaBD, principal);
        clientSocket = servidores.criaNovoServidor(); 
        ThreadParaMulticast multicast = new ThreadParaMulticast(servidores);
        multicast.start();
        ControloLigacoes ligacoes = new ControloLigacoes(servidores);
        ligacoes.start();
        
        
        do
        {
            System.out.print("\n-> ");
            scann = sc.next();
        }while(!scann.equalsIgnoreCase("sair"));

        try{
            servidores.terminaServidor();
            clientSocket.close();

            ligacoes.desliga();
        }catch (IOException ex) {
            Logger.getLogger(TP_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}