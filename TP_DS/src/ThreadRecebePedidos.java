

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

class ThreadRecebePedidos extends Thread {
    Comunicacao com;
    boolean terminar = false;
    
    public ThreadRecebePedidos(Comunicacao c) {
        com = c;
    }
    public void desliga()
    {
        terminar = true;
    }
   
    @Override
    public void run(){
        byte []info = new byte[ConstantesDS.BUFSIZE];
        
        while(!terminar){
            com.setPacket(new DatagramPacket(info,info.length));
            com.recebepedidos();
            com.verificapedido();
            com.enviaresposta();
        }
    }
}
