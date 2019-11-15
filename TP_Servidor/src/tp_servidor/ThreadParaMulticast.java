package tp_servidor;

import java.net.MulticastSocket;

public class ThreadParaMulticast extends Thread {
    MulticastSocket mtsock;
    
    public ThreadParaMulticast(MulticastSocket mtsock){
        this.mtsock = mtsock;
    }
    
    @Override
    public void run(){
        
    }
    
}
