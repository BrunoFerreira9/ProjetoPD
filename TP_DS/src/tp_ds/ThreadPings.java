package tp_ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPings extends Thread {
    private List<Socket> listservers;
    private List<PrintStream> serversout;
    private List<BufferedReader> serversin;

    ThreadPings(List<Socket> listServers) {
        listservers = listServers;
        serversout = new ArrayList<>();
        serversin = new ArrayList<>();
        
        trataMudancaSockets();
    }
    
    @Override
    public void run(){
        
        while(true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadPings.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(listservers.size() != serversout.size()){
                trataMudancaSockets();
            }
            else {
                for(int i = 0; i < serversout.size(); i++){
                    serversout.get(i).print("ping");
                    try {
                        if(serversin.get(i).readLine().compareToIgnoreCase("ativo") == 0){
                            System.out.println("Servidor de IP " + listservers.get(i).getInetAddress() + " está ativo");
                        }else
                            System.out.println("O ping ao servidor de IP " + listservers.get(i).getInetAddress() + " deu erro");
                    } catch (IOException ex) {
                        Logger.getLogger(ThreadPings.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    private void trataMudancaSockets(){
        serversout.clear();
        serversin.clear();
        
        for(Socket sck : listservers){
            try {
                serversout.add(new PrintStream(sck.getOutputStream()));
                serversin.add(new BufferedReader(new InputStreamReader(sck.getInputStream())));
            } catch (IOException ex) {
                Logger.getLogger(ThreadPings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}