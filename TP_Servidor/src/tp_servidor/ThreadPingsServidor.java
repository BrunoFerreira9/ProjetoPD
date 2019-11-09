/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author jmcam
 */
public class ThreadPingsServidor extends Thread {
    private Socket sck;
    private PrintStream serverout;
    private BufferedReader serverin;

    public ThreadPingsServidor(Socket sck) {
        this.sck = sck;
        
        try {
            serverout = new PrintStream(sck.getOutputStream());
            serverin = new BufferedReader(new InputStreamReader(sck.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Erro no direcionamento das stream do socket");
        }
    }
    
    @Override
    public void run(){
        while(true){
            try {
                if(serverin.readLine().compareToIgnoreCase("ping") != 0){
                    System.out.println("Erro no ping recebido!");
                }
                else
                    serverout.print("ativo");
            } catch (IOException ex) {
                Logger.getLogger(ThreadPingsServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
