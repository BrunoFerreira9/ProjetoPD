
package tp_servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_servidor.ConstantesServer.ResolveMessages;


public class LogicaServidor implements InterfaceGestao, myObservable {

    LigacaoToBD ligacao ;
    LogicaServidor este;
    Thread thPings;
     
    ComunicacaoToDS cds;  
    ComunicacaoToCliente cc;
    
    ServerSocket serverSocket = null;
    Socket socketPings = null;
    List<Socket> listaClientes;
                
    public LogicaServidor(String ipDS, String ipMaquinaBD) {  
        listaClientes = new ArrayList<>();
        este = this;
        cds = new ComunicacaoToDS(ipDS);
        
        try {
            cds.inicializaUDP();
            socketPings = cds.inicializaTCPPings(ipDS);
            thPings = new ThreadPingsServidor(socketPings);
        } catch (IOException ex) {
            Logger.getLogger(LogicaServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ligacao = new LigacaoToBD(ipMaquinaBD);
        ligacao.criarLigacaoBD(cds.getnumBD());
    }
    
    public ServerSocket criaNovoServidor()
    {
        try {
            serverSocket = new ServerSocket(cds.getPortoServer());
        } catch (IOException ex) {
            Logger.getLogger(LogicaServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
         return serverSocket;
    }

    public List<Socket> obterListaClientes()
    {
        return listaClientes;
    }
    
    public void aceitaLigacoes(Thread threadCliente) {

        try {
            Socket cliente = serverSocket.accept();
                
            ComunicacaoToCliente comunicacao = new ComunicacaoToCliente(cliente, este);
            threadCliente = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        comunicacao.recebeInformacaoTCP();
                    } catch (IOException ex) {
                        Logger.getLogger(LogicaServidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            threadCliente.start();

        }catch (SocketException e)
        {
            System.out.println("O servidor vai terminar.\n");
        } catch (IOException ex) {
            Logger.getLogger(LogicaServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void adicionaCliente(Socket cliente) {
        listaClientes.add(cliente);
    }
    public void removeCliente(Socket cliente) {
        listaClientes.remove(cliente);
    }
    
    public void terminar(){
        if(listaClientes.isEmpty())
            return;
        
         for(Socket s : listaClientes)
        {
                                    
            String querySelect = "UPDATE utilizador SET ativo= 'false' WHERE ipUser="+s.getLocalAddress().getHostAddress();
            //cc.
           
            try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(LogicaServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @Override
    public boolean efetuaRegisto(HashMap <String,String> user) {
        String query = "Insert into Utilizador (username, password, nome, ativo)  values(\'"  + user.get("username") + "\',\'" + user.get("password") + "\', \'" + user.get("nome") + "\',0);";
        
        String resultado = ligacao.executarInsert(query);
        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Erro ao inserir o utilizador na BD\n");
            return false;
        }
        return true;
    }

    @Override
    public boolean efetuaLogin(HashMap <String,String> user) {
        String query = "Select * from Utilizador where username = \'" + user.get("username") + "\' and password = \'" + user.get("password") + "\' AND ativo=0;";

        String resultado = ligacao.executarSelect(query);

        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Nao existe o utilizador na BD\n");
            return false;
        }
        
        String update = "UPDATE Utilizador SET ativo = 1 , ipUser = \'"+user.get("ip")+"\' WHERE username = \'" + user.get("username") + "\';";
 
        String resultadoupdate = ligacao.executarUpdate(update);
            
        if (resultadoupdate == "ERRO" || resultadoupdate == "") {
            System.out.println("Erro na atualizacao BD\n");
            return false;
        }
       return true;
    }
    @Override
    public boolean efetuaLogout(HashMap <String,String> user) {
        String query = "Select * from Utilizador where username = \'" + user.get("username") + "\' AND ativo= 1 ;";

        String resultado = ligacao.executarSelect(query);

        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Nao existe o utilizador logado na BD\n");
            return false;
        }
        
        String update = "UPDATE Utilizador SET ativo = 0 WHERE username = \'" + user.get("username") + "\';";

          String resultadoupdate = ligacao.executarUpdate(update);

        if (resultadoupdate == "ERRO" || resultadoupdate == "") {
            System.out.println("Erro na atualizacao BD\n");
            return false;
        }
       return true;
    }

    @Override
    public void setChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean trataMusicas(String mensagem) {
          HashMap <String,String> musica = ResolveMessages(mensagem);
       
        if(musica.get("tipo").equals("criaMusica")){
          
            String query = "Select * from musicas where nome = \'" + musica.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir

                String insert = "Insert into musica (nome, autor, album, ano,genero,ficheiro,idUtilizador)  values(\'"  + musica.get("nome") + "\',\'" + 
                        musica.get("autor") + "\', \'" + musica.get("album") + "\',"+musica.get("ano")+ ","+musica.get("duracao")+",\'"+musica.get("genero")+"\', \'"+musica.get("ficheiro")+"\',"+musica.get("id")+");";
                String resultado1= ligacao.executarInsert(insert);
                 if (resultado == "ERRO" || resultado == "") { 
                     return false;
                 }
                    return true;
            }      
        }
        if(musica.get("tipo").equals("editaMusica")){
          
            String query = "Select * from musicas where nome = \'" + musica.get("nome")+"\' AND idUtilizador = "+musica.get("id");

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;            
            }    
            
             String update = "UPDATE musica SET nome = \'"+ musica.get("nome") + 
                     "\',autor = \'"+musica.get("autor") + 
                     "\', album = \'" + musica.get("album") + 
                     "\', ano = "+musica.get("ano")+ 
                     ",duracao = "+musica.get("duracao")+
                     ",genero = \'"+musica.get("genero")+
                     "\', ficheiro = \'"+musica.get("ficheiro")+"\' WHERE idUtilizador = " + musica.get("id") + ";";

            String resultadoupdate = ligacao.executarUpdate(update);

            if (resultadoupdate == "ERRO" || resultadoupdate == "") {
                System.out.println("Erro na atualizacao da musica na BD\n");
                return false;
            }
            return true;
            
        }
        if(musica.get("tipo").equals("eliminaMusica")){
          
            String query = "Select * from musicas where nome = \'" + musica.get("nome")+"\' AND idUtilizador = "+musica.get("id");

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;            
            }  
            String delete = "Delete from musicas where nome = \'" + musica.get("nome") + "\'";

            String resultadodelete = ligacao.executarDelete(delete);

            if (resultadodelete == "ERRO" || resultadodelete == "") { //se nao existir
                return false;            
            }  
            
        }
        if(musica.get("tipo").equals("ouvirMusica")){
          
            String query = "Select * from musicas where nome = \'" + musica.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir

                String insert = "Insert into musica (nome, autor, album, ano,genero,ficheiro,idUtilizador)  values(\'"  + musica.get("nome") + "\',\'" + 
                        musica.get("autor") + "\', \'" + musica.get("album") + "\',"+musica.get("ano")+ ","+musica.get("duracao")+",\'"+musica.get("genero")+"\', \'"+musica.get("ficheiro")+"\',"+musica.get("id")+");";
                String resultado1= ligacao.executarInsert(insert);
                 if (resultado == "ERRO" || resultado == "") { 
                     return false;
                 }
                    return true;
            }      
        }
        if(musica.get("tipo").equals("addMusPlaylist")){
          
            String query = "Select * from musicas where nome = \'" + musica.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir

                String insert = "Insert into musica (nome, autor, album, ano,genero,ficheiro,idUtilizador)  values(\'"  + musica.get("nome") + "\',\'" + 
                        musica.get("autor") + "\', \'" + musica.get("album") + "\',"+musica.get("ano")+ ","+musica.get("duracao")+",\'"+musica.get("genero")+"\', \'"+musica.get("ficheiro")+"\',"+musica.get("id")+");";
                String resultado1= ligacao.executarInsert(insert);
                 if (resultado == "ERRO" || resultado == "") { 
                     return false;
                 }
                    return true;
            }      
        }
        
        
        
        
       return false;
    }

    @Override
    public boolean trataPlaylist(String mensagem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean atualizaMusicas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
