

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

class ThreadRecebePedidos extends Thread {
    Comunicacao com;

    public ThreadRecebePedidos(Comunicacao c) {
        com = c;
    }
   
    @Override
    public void run(){
        byte []info = new byte[ConstantesDS.BUFSIZE];
        
        while(true){
            try {
                com.setPacket(new DatagramPacket(info,info.length));
                com.recebepedidos();
                com.verificapedido();
                com.enviaresposta();
            } catch (IOException ex) {
                Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
