package tp_cliente;

import java.io.IOException;


public class TP_Cliente {

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        //verificar args para receber ip do DS
        String ipDS = args[0];
        
        ComunicacaoToDS cds = new ComunicacaoToDS(ipDS);
        
        cds.inicializaUDP();
        
        cds.enviaMensagem();
        cds.esperaResposta();
        
        ComunicacaoToServidor cs = new ComunicacaoToServidor(cds.getIpServer(),cds.getPortoServer());
        
        cs.inicializaTCP();
        
    }
    
}
