package tp_servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LigacaoToBD {
        
    static final String NOME_BD = "db_pd1920";
    String ip;
    static final String USER_BD = "tp_pd1920";
    static final String PASS_USER_BD = "pd1920";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    Connection conn_ligacao = null;
    Statement stmt = null; 
    
    public LigacaoToBD(String ip) {
        this.ip=ip;
    }
    
    public boolean criarLigacaoBD()
    {
        try {
            Class.forName(JDBC_DRIVER);
            
            System.out.println("Ligando à base de dados ...\n");
            conn_ligacao = DriverManager.getConnection("jdbc:mysql://"+ip+"/db_pd1920", USER_BD, PASS_USER_BD);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LigacaoToBD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(LigacaoToBD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
                s_resposta += rs.getString(1) + "-" + rs.getString(2); //neste caso ainda só é feito um select, será atualizado conforme as necessidades
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
    
    public void executarDelete(String query)
    {
        
        try {
            stmt = conn_ligacao.createStatement();

            stmt.executeUpdate(query);
            
        }catch(SQLException e){
            System.out.println("ERRO [doInBackground]: " + e.getMessage());
        }
        
    }
}
