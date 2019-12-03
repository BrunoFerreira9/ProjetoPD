package tp_servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadUpload extends Thread{
    File localDirectory;
    byte [] data = new byte[4000];
    FileInputStream localFileInputStream = null;
    BufferedReader in;
    OutputStream out;
    PrintStream prts;
    String fileName,localFilePath;
    Socket socket = null;
    public static boolean correr = true;

    ThreadUpload(String filename,Socket s){
        localDirectory = new File(ConstantesServer.PATHLOCATION);
        this.fileName = filename;
        socket = s;
    }
    @Override
    public void run(){
         try{
            while(correr){
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = socket.getOutputStream();
                fileName = in.readLine();
                localFilePath = new File(localDirectory+File.separator+fileName).getCanonicalPath();
                if(!localFilePath.startsWith(localDirectory.getCanonicalPath()+File.separator)){
                    System.out.println("O caminho não está correto");
                }
                localFileInputStream = new FileInputStream(localFilePath);
                int bytes =0;
                do{
                    bytes = localFileInputStream.read(data);
                    if(bytes < 0) break;
                    out.write(data,0,bytes);
                    
                }while(bytes >0);
                localFileInputStream.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadUpload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}