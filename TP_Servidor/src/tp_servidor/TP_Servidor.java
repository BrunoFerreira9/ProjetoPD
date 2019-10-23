package tp_servidor;

import java.io.IOException;

public class TP_Servidor {

    public static void main(String[] args) throws IOException {
        
        String ipMaquinaBD;
        String ipDS ;
        
         if(args.length != 2){
            System.out.println("Sintaxe: java TP_Servidor ipDS ipMaquinaBD ");
            return;
        }  
        ipDS = args[0];
        ipMaquinaBD = args[1];
        
        
         
        ComunicacaoToDS cds = new ComunicacaoToDS(ipDS);
        
        cds.inicializaUDP();
        
        cds.enviaMensagem();
        cds.esperaResposta();
        
        
        LigacaoToBD ligacao = new LigacaoToBD("ipMaquinaBD");
        ligacao.criarLigacaoBD();
        
        }
    }
    

