package tp_cliente;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


public class TP_Cliente  implements Observer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
       
        String ipDS = args[0];
        
        ComunicacaoToDS cds = new ComunicacaoToDS(ipDS);
        
        cds.inicializaUDP();
        System.out.println("ip :"+cds.getIpServer()+" porto :"+cds.getPortoServer());           
        ComunicacaoToServidor cs = new ComunicacaoToServidor(cds.getIpServer(),cds.getPortoServer());
        
        cs.inicializaTCP();
        Utilizador xpto = new Utilizador("Bruno","123","Bruno F");
        //cs.efetuaLogin(xpto);
        
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    
}
