
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
    String idUser;
                
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
        
        msg = ConstantesServer.TERMINASERVIDOR;
        setChanged();
        notifyObservers();
            
        for(Socket s : listaClientes)
        {
            String queryUpdate = "UPDATE utilizador SET ativo = 0 WHERE ipUser = '"+s.getLocalAddress().getHostAddress()+"'";
            
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
        
        String query = "Select * from Utilizador where username = \'" + user.get("username") + "\'";

        String resultado = ligacao.executarSelect(query);        
    
        if (resultado == "ERRO" || resultado == "") {
            //System.out.println("Nao existe o utilizador na BD\n");
             String queryRegisto = "Insert into Utilizador (username, password, nome, ativo)  values(\'"  + user.get("username") + "\',\'" + user.get("password") + "\', \'" + user.get("nome") + "\',0);";
        
             System.out.println(queryRegisto);
            String resultadoRegisto = ligacao.executarInsert(queryRegisto);
            if (resultadoRegisto == "ERRO" || resultadoRegisto == "") {
                System.out.println("Erro ao inserir o utilizador na BD\n");
                return false;
            }
            return true;
        }              
        return false;
    }

   
    @Override
    public boolean efetuaLogin(HashMap <String,String> user) {
        String query = "Select username from Utilizador where username = \'" + user.get("username") + "\' and password = \'" + user.get("password") + "\' AND ativo=0;";

        
        String resultado = ligacao.executarSelect(query);

        System.out.println(resultado);
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
        String query = "Select username from Utilizador where username = \'" + user.get("username") + "\' AND ativo = 1 ;";

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
    
    public ArrayList<Musica> getListaMusicasFiltro(String msg){
        return ligacao.getListaMusicasFiltro(msg);
    }
    public ArrayList<Playlist> getListaPlaylistFiltro(String msg){
        return ligacao.getListaPlaylistFiltro(msg);
    }
    
    @Override
    public boolean trataMusicas(HashMap <String,String> musica) {
      
        if(musica.get("tipo").equals("criaMusica")){
          
            String query = "Select * from musica where nome = \'" + musica.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir

                String insert = "Insert into musica (nome, autor, album,ano,duracao,genero,ficheiro,criadorMusica)  values(\'"  + musica.get("nome") + "\',\'" + 
                        musica.get("autor") + "\', \'" + musica.get("album") + "\',"+musica.get("ano")+ ","+musica.get("duracao")+",\'"+musica.get("genero")+"\', \'"+musica.get("ficheiro")+"\',\'"+musica.get("utilizador")+"\');";
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
            
           /* if(getUtilizador(musica)!="")
                idUser = getUtilizador(musica);*/
           
            String query = "Select * from musica where nome = \'" + musica.get("musicaEditar")+"\' AND criadorMusica = \'"+musica.get("utilizador")+"\'";

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
                     "\', ficheiro = \'"+musica.get("ficheiro")+"\' WHERE nome = \'" + musica.get("musicaEditar")+"\' AND criadorMusica = \'"+musica.get("utilizador")+"\'";

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
                                 
            String query = "Select nome from musica where nome = \'" + musica.get("nome")+"\' AND criadorMusica = \'"+musica.get("utilizador")+"\'";
            
            System.out.println(query);
            String resultado = ligacao.executarSelect(query);
            System.out.println(resultado);
            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;            
            }
            
            String deletePlaylist = "Delete from musica_has_playlist where nomeMusica = '" + resultado+"'" ;
            
            ligacao.executarDelete(deletePlaylist);
            
            String delete = "Delete from musica where nome = '" +resultado+"'" ;
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
          
            String query = "Select nome from musica where nome = \'" + musica.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);
            if (resultado == "ERRO" || resultado == "")  //se nao existir
                     return false;
            
            String queryPlay = "Select nome from playlist where criadorPlaylist = '" + musica.get("utilizador")+"' and nome = '"+musica.get("nomePlaylist")+"'";

            String resultadoqueryPlay = "" ;
            resultadoqueryPlay = ligacao.executarSelect(queryPlay);
            if (resultadoqueryPlay == "ERRO" || resultadoqueryPlay == "")  //se nao existir
                     return false;
                
            String add = "INSERT INTO musica_has_playlist(nomeMusica, nomePlaylist) VALUES ('" +  resultado +"','" +  resultadoqueryPlay +"')";
            String resultadoadd = ligacao.executarInsert(add);
            
            if (resultadoadd == "ERRO" || resultadoadd == "")  //se nao existir
                     return false;
            msg = ConstantesServer.ATUALIZAMUSICAS;
            setChanged();
            notifyObservers();
            return true;
                        
        }
        return false;
    }
    
    @Override
    public boolean trataPlaylist(HashMap <String,String> playlist) {
       
        if(playlist.get("tipo").equals("criaPlaylist")){
          
            String query = "Select * from playlist where nome = \'" + playlist.get("nome")+"\'";

            String resultado = ligacao.executarSelect(query);
            //System.out.print(query);
            if (resultado == "ERRO" || resultado == "") { //se nao existir

                String insert = "Insert into playlist (nome, criadorPlaylist)  values(\'"  + playlist.get("nome") + "\','" + playlist.get("utilizador")+"');";
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
          
            String query = "Select nome from playlist where nome = '" + playlist.get("nomePlaylist")+"' AND criadorPlaylist = '"+playlist.get("utilizador")+"'";

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;            
            }    
            
             String update = "UPDATE playlist SET nome = \'"+ playlist.get("nome") + "\' where nome = '" + resultado +"' AND criadorPlaylist = '"+playlist.get("utilizador")+"'";

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
          
            String query = "Select nome from playlist where nome = '" + playlist.get("nome")+"'  AND criadorPlaylist = '"+playlist.get("utilizador")+"'";

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;            
            }
            
            String deletePlaylist = "Delete from musica_has_playlist where nomePlaylist = \'" + resultado + "\'";
            ligacao.executarDelete(deletePlaylist);
            
            String delete = "Delete from playlist where nome = '" + resultado+"'" ;
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
            ArrayList<String> listaficheiros = ligacao.getListaFicheirosPlaylist(playlist.get("nome"));
            resposta = listaficheiros.toString().substring(1, listaficheiros.toString().length()-1);
            //TRANSFERIR OS FICHEIROS PARA O CLIENTE
            msg = ConstantesServer.ATUALIZAPLAYLISTS;
            setChanged();
            notifyObservers();
            return true;
        }
        else if(playlist.get("tipo").equals("eliminaMusicaPlaylist")){
          
            String query = "Select m.nome from musica m , musica_has_playlist mp, playlist p where mp.nomeMusica = m.nome AND mp.nomePlaylist=p.nome AND m.nome='"+playlist.get("nomeMusica") + "' and p.nome = '"+playlist.get("nomePlaylist")+"'";

            String resultado = ligacao.executarSelect(query);

            if (resultado == "ERRO" || resultado == "") { //se nao existir
                return false;
            }      
                       
            String deletePlaylist = "Delete from musica_has_playlist where nomeMusica = '" + playlist.get("nomeMusica") + "' AND nomePlaylist = '"+playlist.get("nomePlaylist")+"'";
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
    public String getUtilizador(HashMap <String,String> musica ){
        String queryUser = "Select id from utilizador where username = '" + musica.get("utilizador")+"'";

            String idUser = ligacao.executarSelect(queryUser);

            if (idUser == "ERRO" || idUser == "") { //se nao existir
                return " ";            
            }
            return idUser;
    }
}
