
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


public class LogicaServidor extends Observable implements InterfaceGestao{

    LigacaoToBD ligacao ;
    LogicaServidor este;
     
    ComunicacaoToDS cds;  
    ComunicacaoToCliente cc;
    
     ServerSocket serverSocket = null;
     List<Socket> listaClientes;
                
    public LogicaServidor(String ipDS, String ipMaquinaBD  ) throws IOException {  
        listaClientes = new ArrayList<>();
        este = this;
        cds = new ComunicacaoToDS(ipDS);
        
        cds.inicializaUDP();
        
               
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
        String query = "Insert into Utilizador (username, password, nome, ativo)  values(\'"  + user.get("username") + "\',\'" + user.get("password") + "\', \'" + user.get("nome") + "\',false);";
        
        String resultado = ligacao.executarInsert(query);
        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Erro ao inserir o utilizador na BD\n");
            return false;
        }
        return true;
    }

    @Override
    public boolean efetuaLogin(HashMap <String,String> user) {
        String query = "Select * from Utilizador where username = \'" + user.get("username") + "\' and password = \'" + user.get("password") + "\' AND ativo=false;";

        String resultado = ligacao.executarSelect(query);

        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Nao existe o utilizador na BD\n");
            return false;
        }
        
        String update = "UPDATE Utilizador SET ativo = true and ipUser = \'"+user.get("ip")+"\' WHERE username = \'" + user.get("username") + "\';";

        String resultadoupdate = ligacao.executarSelect(query);
            
        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Erro na atualizacao BD\n");
            return false;
        }
       return true;
    }
    @Override
    public boolean efetuaLogout(HashMap <String,String> user) {
        String query = "Select * from Utilizador where username = \'" + user.get("username") + "\' AND ativo=true;";

        String resultado = ligacao.executarSelect(query);

        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Nao existe o utilizador logado na BD\n");
            return false;
        }
        
        String update = "UPDATE Utilizador SET ativo = false WHERE username = \'" + user.get("username") + "\';";

        String resultadoupdate = ligacao.executarSelect(query);

        if (resultado == "ERRO" || resultado == "") {
            System.out.println("Erro na atualizacao BD\n");
            return false;
        }
       return true;
    }
}
