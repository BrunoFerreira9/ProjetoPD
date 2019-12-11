package tp_servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadDownload extends Thread{
    File localDirectory;
    String fileName, localFilePath = null;
    Socket socket = null;
    FileOutputStream localFileOutputStream = null;
    InputStream bfr;
    PrintWriter prts;
    int nbytes;
    byte []bufer = new byte[4000];
    
    ThreadDownload(String filename,Socket s){
        localDirectory = new File(ConstantesServer.PATHLOCATION);
        this.fileName = filename;
        socket = s;
    }
    
    boolean verificaDiretoria(){
        if(!localDirectory.exists()){
            System.out.println("A directoria " + localDirectory + " nao existe!");
            return false;
        }
         
        if(!localDirectory.isDirectory()){
            System.out.println("O caminho " + localDirectory + " nao se refere a uma directoria!");
            return false;
        }
         
        if(!localDirectory.canWrite()){
            System.out.println("Sem permissoes de escrita na directoria " + localDirectory);
            return false;
        }
        return true;
    }
    @Override
    public void run(){
        if(verificaDiretoria()){
            try{
                localFilePath = localDirectory.getCanonicalPath()+File.separator+fileName;
                localFileOutputStream = new FileOutputStream(localFilePath);
                System.out.println("Ficheiro " + localFilePath + " criado.");
            }catch(IOException e){
                if(localFilePath == null){
                    System.out.println("Ocorreu a excepcao {" + e +"} ao obter o caminho canonico para o ficheiro local!");   
                }else{
                    System.out.println("Ocorreu a excepcao {" + e +"} ao tentar criar o ficheiro " + localFilePath + "!");
                }
                return;
            }
            try{
                bfr = socket.getInputStream();
                prts = new PrintWriter(socket.getOutputStream());
                prts.println("tipo | upload ; ficheiro | "+ fileName); 
                prts.flush();
                do{
                    nbytes = bfr.read(bufer);
                    //|| new String(bufer,0,bufer.length).equals("fim")
                    if(nbytes < 0) break;
                    localFileOutputStream.write(bufer, 0, nbytes);
                    System.out.println("ler ficheiro");
                }while(nbytes > 0);
                System.out.println("Transferencia concluida do ficheiro "+fileName+".");
                localFileOutputStream.close();

            }catch(UnknownHostException e){
                 System.out.println("Destino desconhecido:\n\t"+e);
            }catch(IOException e){
                System.out.println("Ocorreu um erro no acesso ao socket ou ao ficheiro local " + localFilePath +":\n\t"+e);
            }
        }   
    }
}

