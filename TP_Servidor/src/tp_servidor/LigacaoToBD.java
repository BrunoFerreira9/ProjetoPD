package tp_servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tp_servidor.ConstantesServer.ResolveMessages;

public class LigacaoToBD {
        
    private String ip;
    static final String USER_BD = "tp_pd1920";
    static final String PASS_USER_BD = "pd1920";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    Connection conn_ligacao = null;
    Statement stmt = null; 
    
    public LigacaoToBD(String ip) {
        this.ip=ip;
       
    }
    
    public boolean criarLigacaoBD(int bd)
    {        
        try {
           
            Class.forName(JDBC_DRIVER);
            
            System.out.println("Ligando à base de dados ...\n");
            conn_ligacao = DriverManager.getConnection("jdbc:mysql://"+ip+"/db_pd1920_"+bd+"?useTimezone=true&serverTimezone=UTC", USER_BD, PASS_USER_BD);
            
        } catch ( SQLException ex) {
            Logger.getLogger(LigacaoToBD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LigacaoToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public String executarInsert(String query, LogicaServidor servidor)
    {
        try {
            PreparedStatement ps_query = conn_ligacao.prepareStatement(query);
            ps_query.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERRO [doInBackground]: " + e.getMessage());
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + e.toString());
            return "ERRO";
        }
        
        return "RESULTADO";
    }
    
     public String executarSelect(String query, LogicaServidor servidor)
    {
        String s_resposta = "ERRO";
        try {
            stmt = conn_ligacao.createStatement();

            s_resposta = "";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                s_resposta += rs.getString(1); //neste caso ainda só é feito um select, será atualizado conforme as necessidades
            }                                              //aparecerem ao longo do programa

        }catch(SQLException e){
            System.out.println("ERRO [doInBackground]: " + e.getMessage());
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + e.toString());
            return s_resposta;
        }
        
        return s_resposta;
    }
     
     public String executarUpdate(String query, LogicaServidor servidor)
    {
        try {
            stmt = conn_ligacao.createStatement();
            stmt.executeUpdate(query);
        }catch(SQLException e){
            System.out.println("ERRO [doInBackground]: " + e.getMessage());
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + e.toString());
            return "ERRO";
        }
        
        return "RESULTADO";
    }
    
    public String executarDelete(String query, LogicaServidor servidor)
    {
        
        try {
            stmt = conn_ligacao.createStatement();

            stmt.executeUpdate(query);
            
        }catch(SQLException e){
            System.out.println("ERRO [doInBackground]: " + e.getMessage());
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + e.toString());
             return "ERRO";
        }
         return "RESULTADO";
    }
    
    public ArrayList<Musica> getListaMusicas(LogicaServidor servidor) 
    {
        try {
            ArrayList<Musica> lista = new ArrayList<>();
            String query = "Select * from musica";
            stmt = conn_ligacao.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                String nome = rs.getString("nome");
                String autor = rs.getString("autor");
                String album = rs.getString("album");
                int ano = rs.getInt("ano");
                double duracao = rs.getDouble("duracao");
                String genero = rs.getString("genero");
                String ficheiro = rs.getString("ficheiro");
                String criadorMusica = rs.getString("criadorMusica");
                
                lista.add(new Musica(nome,autor,album,ano,duracao,genero,ficheiro,criadorMusica));
            }
            return lista;
        } catch (SQLException ex) {
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + ex.toString());
        }
        return null;
    }
    
    public ArrayList<Musica> getListaMusicasFiltro(String mensagem, LogicaServidor servidor) 
    {
        HashMap <String,String> musica = ResolveMessages(mensagem);
        
          
        String query = "Select * from musica where ";
        switch(musica.get("filtragem")){

            case "nome": query += "nome = \'" +musica.get("pesquisa") +"\'";break;
            case "autor": query += "autor = \'" +musica.get("pesquisa") +"\'";break;
            case "album": query += "album = \'" +musica.get("pesquisa") +"\'";break;
            case "ano": query += "ano = " +musica.get("pesquisa");break;
            case "duracao": query += "duracao = " +musica.get("pesquisa");break;
            case "genero": query += "genero = \'" +musica.get("pesquisa") +"\'";break;
            case "ficheiro": query += "ficheiro = \'" +musica.get("pesquisa") +"\'";break;

        }
        
        try {
            ArrayList<Musica> lista = new ArrayList<>();            
            stmt = conn_ligacao.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {                
                String nome = rs.getString("nome");
                String autor = rs.getString("autor");
                String album = rs.getString("album");
                int ano = rs.getInt("ano");
                double duracao = rs.getDouble("duracao");
                String genero = rs.getString("genero");
                String ficheiro = rs.getString("ficheiro");
                String Utilizador = rs.getString("criadorMusica");
                
                lista.add(new Musica(nome,autor,album,ano,duracao,genero,ficheiro,Utilizador));
            }
            return lista;
        } catch (SQLException ex) {
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + ex.toString());
        }
        return null;
    }
    
    public ArrayList<Playlist> getListaPlaylist(LogicaServidor servidor) 
    {
        try {

            ArrayList<Playlist> lista = new ArrayList<>();
            String query = "Select * from playlist";
            stmt = conn_ligacao.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {                
                String nome = rs.getString("nome");
                String user = rs.getString("criadorPlaylist");
                
                lista.add(new Playlist(nome,user));
            }
            return lista;
        } catch (SQLException ex) {
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + ex.toString());
        }
        return null;
    }
    
    public ArrayList<Playlist> getListaPlaylistFiltro(String mensagem, LogicaServidor servidor) 
    {
        HashMap <String,String> play = ResolveMessages(mensagem);
        try {
            ArrayList<Playlist> lista = new ArrayList<>();
            String query = "Select * from playlist where ";
            switch(play.get("filtragem")){
                case "nome": query += "nome = \'" +play.get("pesquisa") +"\'";break;
            }
            stmt = conn_ligacao.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {                
                String nome = rs.getString("nome");
                String user = rs.getString("criadorPlaylist");
                
                lista.add(new Playlist(nome,user));
            }
            return lista;
        } catch (SQLException ex) {
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + ex.toString());
        }
        return null;
    }
    
    public ArrayList<String> getListaMusicasPlaylist(String playlist, LogicaServidor servidor) 
    {
        try {
            ArrayList<String> lista = new ArrayList<>();
            String query = "Select m.nome from playlist p, musica m, musica_has_playlist mp where mp.nome = " +playlist ;
            stmt = conn_ligacao.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                String nome = rs.getString("nome");
                lista.add(nome);
            }
            return lista;
        } catch (SQLException ex) {
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + ex.toString());
        }
        return null;
    }
    
    public ArrayList<String> getListaFicheirosPlaylist(String playlist, LogicaServidor servidor) 
    {
        try {
            ArrayList<String> lista = new ArrayList<>();
            String query = "Select ficheiro from musica m , musica_has_playlist mp, playlist p where m.nome = mp.nomeMusica AND mp.nomePlaylist = p.nome AND p.nome='"+playlist+"'";
            stmt = conn_ligacao.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                String nome = rs.getString("ficheiro");
                lista.add(nome);
            }
            return lista;
        } catch (SQLException ex) {
            servidor.dissipaMensagem("tipo | excepcao ; msg | Erro na base de dados - " + ex.toString());
        }
        return null;
    }
}
