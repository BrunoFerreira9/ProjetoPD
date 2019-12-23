
package tp_servidor;

import java.io.IOException;
import java.net.DatagramSocket;
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

    private LigacaoToBD ligacao ;
    private LogicaServidor este;
     
    private ComunicacaoToDS cds;
    
    private ComunicacaoToCliente cc;
    private Thread pings;
    
    private DatagramSocket dtsocket;
    private ServerSocket serverSocket = null;
    private List<Socket> listaClientes;
    String resposta;
    boolean changed = false;
    List<myObserver> observers = new ArrayList<>();
    int msg;
                
    public LogicaServidor(String ipDS, String ipMaquinaBD, Boolean principal) {  
        listaClientes = new ArrayList<>();
        este = this;
        cds = new ComunicacaoToDS(ipDS);
        
        try {
            dtsocket = cds.inicializaUDP(principal);
            pings = new ThreadPings(dtsocket,ipDS);
            pings.start();
        } catch (IOException ex) {
            Logger.getLogger(LogicaServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
               
        ligacao = new LigacaoToBD(ipMaquinaBD);
        ligacao.criarLigacaoBD(cds.getnumBD());
    }

    public LigacaoToBD getLigacao() {
        return ligacao;
    }
    
    public ServerSocket criaNovoServidor(){
       
        try {
            serverSocket = new ServerSocket(cds.getPortoServer());
           
        } catch (IOException ex) {
            Logger.getLogger(LogicaServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
         return serverSocket;
    }

    public ComunicacaoToDS getCds() {
        return cds;
    }

    public ComunicacaoToCliente getCc() {
        return cc;
    }
    public List<Socket> obterListaClientes(){
        return listaClientes;
    }
    
    public void terminaServidor(){
        cds.terminaServidor();
    }
    public void aceitaLigacoes(Thread threadCliente) {

        try {
            Socket cliente = serverSocket.accept();
                
            ComunicacaoToCliente comunicacao = new ComunicacaoToCliente(cliente, este);
            threadCliente = new Thread(new Runnable() {
                @Override
                public void run() {
                    comunicacao.recebeInformacaoTCP();
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
        
        cc.setTerminaServidor();
        //String msg = "tipo | servidor ; msg | terminar";
        // setChanged();
        // notifyObservers(msg);
        
         for(Socket s : listaClientes)
        {
            String queryUpdate = "UPDATE utilizador SET ativo= 0 WHERE ipUser="+s.getLocalAddress().getHostAddress();
            
            ligacao.executarUpdate(queryUpdate);
                                     
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

    public ArrayList<Musica> getListaMusicas(){
        return ligacao.getListaMusicas();
    }
    public ArrayList<Playlist> getListaPlaylist(){
        return ligacao.getListaPlaylist();
    }
    @Override
    public boolean trataMusicas(String mensagem) {
        HashMap <String,String> musica = ResolveMessages(mensagem);
       
        if(musica.get("tipo").equals("criaMusica")){
          
            String query = "Select * from musica where nome = \'" + musica.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir

                String insert = "Insert into musica (nome, autor, album,ano,duracao,genero,ficheiro,idUtilizador)  values(\'"  + musica.get("nome") + "\',\'" + 
                        musica.get("autor") + "\', \'" + musica.get("album") + "\',"+musica.get("ano")+ ","+musica.get("duracao")+",\'"+musica.get("genero")+"\', \'"+musica.get("ficheiro")+"\',"+musica.get("id")+");";
                
                
                String resultado1= ligacao.executarInsert(insert);
                if (resultado1 == "ERRO" || resultado1 == "") { 
                     return false;
                }
                msg = ConstantesServer.ATUALIZAMUSICAS;
                setChanged();
                notifyObservers();
                return true;
            } 
            return false;
        }
        else if(musica.get("tipo").equals("editaMusica")){
          
            String query = "Select * from musica where idMusica = '" + musica.get("idMusica")+"' AND idUtilizador = "+musica.get("id");

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
                     "\', ficheiro = \'"+musica.get("ficheiro")+"\' WHERE idMusica = " + musica.get("idMusica")+" AND idUtilizador = "+musica.get("id");

            String resultadoupdate = ligacao.executarUpdate(update);

            if (resultadoupdate == "ERRO" || resultadoupdate == "") {
                System.out.println("Erro na atualizacao da musica na BD");
                return false;
            }
            msg = ConstantesServer.ATUALIZAMUSICAS;
            setChanged();
            notifyObservers();
            return true;
        }
        else if(musica.get("tipo").equals("eliminaMusica")){
          
            String query = "Select idMusica from musica where nome = \'" + musica.get("nome")+"\' AND idUtilizador = "+musica.get("id");
            
            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;            
            }
            
            String deletePlaylist = "Delete from musica_has_playlist where idMusica = " + resultado ;
            
            ligacao.executarDelete(deletePlaylist);
            
            String delete = "Delete from musica where idMusica = " +resultado ;
            ligacao.executarDelete(delete);
            msg = ConstantesServer.ATUALIZAMUSICAS;
            setChanged();
            notifyObservers();
            return true;
            
        }
        else if(musica.get("tipo").equals("ouvirMusica")){
          
            String query = "Select ficheiro from musica where nome = \'" + musica.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;
            }      
            resposta = resultado;
            return true;
            //TRANSFERIR O FICHEIRO PARA O CLIENTE
        }
        else if(musica.get("tipo").equals("addMusPlaylist")){
          
            String query = "Select idMusica from musica where nome = \'" + musica.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);
            if (resultado == "ERRO" || resultado == "")  //se nao existir
                     return false;
            
            String queryPlay = "Select idPlaylist from playlist where idUtilizador = " + musica.get("id")+" and nome = "+musica.get("nomePlaylist");

            String resultadoqueryPlay = "" ;
            resultadoqueryPlay = ligacao.executarSelect(queryPlay);

            if (resultadoqueryPlay == "ERRO" || resultadoqueryPlay == "")  //se nao existir
                     return false;
                
            String add = "INSERT INTO musica_has_playlist(idMusica, idPlaylist) VALUES (" +  resultado +"," +  resultadoqueryPlay +")";
            String resultadoadd = ligacao.executarInsert(add);
            
            if (resultadoadd == "ERRO" || resultadoadd == "")  //se nao existir
                     return false;
            msg = ConstantesServer.ATUALIZAMUSICAS;
            setChanged();
            notifyObservers();
            return true;
                        
        }else if(musica.get("tipo").equals("filtro")){
          
            String query = "Select * from musicas where ";
            switch(musica.get("filtro")){
            
                case "nome": query += "nome = \'" +musica.get("pesquisa") +"\'";break;
                case "autor": query += "autor = \'" +musica.get("pesquisa") +"\'";break;
                case "album": query += "album = \'" +musica.get("pesquisa") +"\'";break;
                case "ano": query += "ano = " +musica.get("pesquisa");break;
                case "duracao": query += "duracao = " +musica.get("pesquisa");break;
                case "genero": query += "genero = \'" +musica.get("pesquisa") +"\'";break;
            
            }
            
            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "")  //se nao existir
                     return false;
            msg = ConstantesServer.ATUALIZAMUSICAS;
            setChanged();
            notifyObservers();          
            return true;
        }
        return false;
    }
    @Override
    public boolean trataPlaylist(String mensagem) {
       HashMap <String,String> playlist = ResolveMessages(mensagem);
       
        if(playlist.get("tipo").equals("criaPlaylist")){
          
            String query = "Select * from playlist where nome = \'" + playlist.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);
            System.out.print(query);
            if (resultado == "ERRO" || resultado == "") { //se nao existir

                String insert = "Insert into playlist (nome, idUtilizador)  values(\'"  + playlist.get("nome") + "\'," + playlist.get("id")+");";
                System.out.print(insert);
                String resultado1= ligacao.executarInsert(insert);
                 if (resultado1 == "ERRO" || resultado1 == "") { 
                     return false;
                 }
                msg = ConstantesServer.ATUALIZAPLAYLISTS;
                setChanged();
                notifyObservers();
                return true;
            } 
            return false;
        }
        else if(playlist.get("tipo").equals("editaPlaylist")){
          
            String query = "Select id from playlist where nome = " + playlist.get("nomePlaylist")+" AND idUtilizador = "+playlist.get("id");

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;            
            }    
            
             String update = "UPDATE playlist SET nome = \'"+ playlist.get("nome") + "\' where idPlaylist = " + resultado +" AND idUtilizador = "+playlist.get("id");

            String resultadoupdate = ligacao.executarUpdate(update);

            if (resultadoupdate == "ERRO" || resultadoupdate == "") {
                System.out.println("Erro na atualizacao da playlist na BD\n");
                return false;
            }
            msg = ConstantesServer.ATUALIZAPLAYLISTS;
            setChanged();
            notifyObservers();
            return true;
            
        }
        else if(playlist.get("tipo").equals("eliminaPlaylist")){
          
            String query = "Select id from playlist where nome = " + playlist.get("nome")+" AND idUtilizador = "+playlist.get("id");

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;            
            }
            
            String deletePlaylist = "Delete from musica_has_playlist where idPlaylist = \'" + resultado + "\'";
            ligacao.executarDelete(deletePlaylist);
            
            String delete = "Delete from playlist where idPlaylist = " + resultado ;
            String resultadodelete = ligacao.executarDelete(delete);
            if (resultadodelete == "ERRO" || resultadodelete == "") { //se nao existir
                return false;            
            }  
            msg = ConstantesServer.ATUALIZAPLAYLISTS;
            setChanged();
            notifyObservers();
            return true;
            
        }
        else if(playlist.get("tipo").equals("ouvirPlaylist")){
          
            String query = "Select ficheiro from musica m , musica_has_playlist mp, playlist p where m.idMusica = mp.idMusica AND mp.idPlaylist = p.idPlaylist AND p.nome="+playlist.get("nome");

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;
            }      
            
            //TRANSFERIR OS FICHEIROS PARA O CLIENTE
            msg = ConstantesServer.ATUALIZAPLAYLISTS;
            setChanged();
            notifyObservers();
            return true;
        }
        else if(playlist.get("tipo").equals("eliminaMusicaPlaylist")){
          
            String query = "Select * from musica m , musica_has_playlist mp, playlist p where m.idMusica = mp.idMusica AND mp.idPlaylist = p.idPlaylist AND m.nome="+playlist.get("nomeMusica") + "and p.nome = "+playlist.get("nomePlaylist");

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;
            }      
            
            String queryMusica = "Select * from musica  where nome='"+playlist.get("nomeMusica")+"'";

            String resultadoMusica = ligacao.executarSelect(queryMusica);
            
             String queryPlaylist = "Select * from playlist  where nome='"+playlist.get("nomePlaylist")+"'";

            String resultadoPlaylist = ligacao.executarSelect(queryPlaylist);
            String deletePlaylist = "Delete from musica_has_playlist where idMusica = " + resultadoMusica + " AND idPlaylist = "+resultadoPlaylist;
            ligacao.executarDelete(deletePlaylist);
            msg = ConstantesServer.ATUALIZAPLAYLISTS;
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    } 

    @Override
    public boolean atualizaMusicas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void setChanged() {
        changed = true;
    }

    @Override
    public void notifyObservers() {
        observers.forEach((obs) -> {
            obs.update(msg);
        });
        changed = false;
    }

    @Override
    public void addObserver(myObserver obs) {
        observers.add(obs);
    }

    @Override
    public void removeObserver(myObserver obs) {
        observers.remove(obs);
    }
}
