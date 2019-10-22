package tp_servidor;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class TP_Servidor {

    public static void main(String[] args) {
        
         LigacaoToBD ligacao = new LigacaoToBD("localhost");

         ligacao.criarLigacaoBD();
         
        
        
        }
    }
    

