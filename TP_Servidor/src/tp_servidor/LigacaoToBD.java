package tp_servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public String executarInsert(String query)
    {
        try {
            PreparedStatement ps_query = conn_ligacao.prepareStatement(query);
            ps_query.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERRO [doInBackground]: " + e.getMessage());
            return "ERRO";
        }
        
        return "RESULTADO";
    }
    
     public String executarSelect(String query)
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
               return s_resposta;
        }
        
        return s_resposta;
    }
     
     public String executarUpdate(String query)
    {
        try {
            stmt = conn_ligacao.createStatement();
            stmt.executeUpdate(query);

        }catch(SQLException e){
            System.out.println("ERRO [doInBackground]: " + e.getMessage());
               return "ERRO";
        }
        
        return "RESULTADO";
    }
    
    public String executarDelete(String query)
    {
        
        try {
            stmt = conn_ligacao.createStatement();

            stmt.executeUpdate(query);
            
        }catch(SQLException e){
            System.out.println("ERRO [doInBackground]: " + e.getMessage());
             return "ERRO";
        }
         return "RESULTADO";
    }
    
    public ArrayList<Musica> getListaMusicas() 
    {
        try {
            ArrayList<Musica> lista = new ArrayList<>();
            String query = "Select * from musica";
            stmt = conn_ligacao.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                int i = rs.getInt("idMusica");
                String nome = rs.getString("nome");
                String autor = rs.getString("autor");
                String album = rs.getString("album");
                int ano = rs.getInt("ano");
                double duracao = rs.getDouble("duracao");
                String genero = rs.getString("genero");
                String ficheiro = rs.getString("ficheiro");
                int idUtilizador = rs.getInt("idUtilizador");
                
                lista.add(new Musica(i,nome,autor,album,ano,duracao,genero,ficheiro,idUtilizador));
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(LigacaoToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<Playlist> getListaPlaylist() 
    {
        try {
            ArrayList<Playlist> lista = new ArrayList<>();
            String query = "Select * from playlist";
            stmt = conn_ligacao.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                int i = rs.getInt("idPlaylist");
                String nome = rs.getString("nome");
                int idUtilizador = rs.getInt("idUtilizador");
                
                lista.add(new Playlist(i,nome,idUtilizador));
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(LigacaoToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<String> getListaMusicasPlaylist(String playlist) 
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
            Logger.getLogger(LigacaoToBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
