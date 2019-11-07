package tp_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_Servidor {

    public static void main(String[] args){
        
        Scanner sc = new Scanner(System.in);
        String scann;
        String ipMaquinaBD;
        String ipDS ;

        if(args.length != 2){
            System.out.println("Sintaxe: java TP_Servidor ipDS ipMaquinaBD ");
            return;
        }
        ipDS = args[0];
        ipMaquinaBD = args[1];

        LogicaServidor servidores = null;
        ServerSocket clientSocket = null;

        try{
            servidores = new LogicaServidor(ipDS,ipMaquinaBD);
            // ss = new ServerSocket(servidores.cds.getPortoServer());
            clientSocket = servidores.criaNovoServidor();
        } catch (IOException ex) {
            Logger.getLogger(TP_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } 

        ControloLigacoes ligacoes = new ControloLigacoes(servidores);
        ligacoes.start();

        do
        {
            System.out.print("\n-> ");

            scann = sc.next();
        }while(!scann.equalsIgnoreCase("sair"));

        try{
            clientSocket.close();

            ligacoes.desliga();
        }catch (IOException ex) {
            Logger.getLogger(TP_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
    
    

