/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp_servidor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Bruno Ferreira
 */
public class LogicaServidor extends Observable implements InterfaceGestao{

    LigacaoToBD ligacao ;
    LogicaServidor este;
     
    ComunicacaoToDS cds;  
    ComunicacaoToCliente cc;
                
    public LogicaServidor(String ipDS, String ipMaquinaBD  ) throws IOException {  
        este = this;
        cds = new ComunicacaoToDS(ipDS);
        
        cds.inicializaUDP();
        
               
        //ligacao = new LigacaoToBD(ipMaquinaBD);
        //ligacao.criarLigacaoBD();
    }
    
    
    
    @Override
    public boolean efetuaRegisto(Utilizador user) {
        String query = "Insert into Utilizador values(\'" + user.getUsername() + "\',\'" + user.getPassword() + "\', \'" + user.getNome() + ");";
        
        String resultado = ligacao.executarInsert(query);
        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Erro ao inserir o utilizador na BD\n");
            return false;
        }
        return true;
    }

    @Override
    public boolean efetuaLogin(Utilizador user) {
        String query = "Select * from Utilizador where username = \'" + user.getUsername() + "\' and password = \'" + user.getPassword() + "\';";

        String resultado = ligacao.executarSelect(query);

        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Nao existe o utilizador na BD\n");
            return false;
        }
        
        String update = "UPDATE Utilizador SET ativo = true WHERE username = \'" + user.getUsername() + "\';";

        String resultadoupdate = ligacao.executarSelect(query);

        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Erro na atualizacao BD\n");
            return false;
        }
       return true;
    }
}
