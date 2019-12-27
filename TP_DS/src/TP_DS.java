import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_DS/* extends java.rmi.server.UnicastRemoteObject implements InterfaceServico*/{
/*    
    public static final String SERVICE_NAME = "RegistryDS";
    public static final int MAX_CHUNCK_SIZE = 10000; //bytes
    public static final int MAX_CHUNK_LENGTH = 512 ;*/
    
    Comunicacao com;
    private static List<Servidor> listservers = new ArrayList<>();
    private static int numClientes ,numbasedados ;
   /* 
    public TP_DS() throws RemoteException{
        listservers = new ArrayList<>();
        numClientes = 0;
        numbasedados = 0;
    }*/
    
    public static void main(String[] args){
        
        Comunicacao com = new Comunicacao(listservers, numClientes, numbasedados);
        try {
            com.criacomunicacao();
            new ThreadRecebePedidos(com).start();
            new ThreadPingsParaServidores(listservers).start();
        } catch (SocketException ex) {
            Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*
        * Lanca o rmiregistry localmente no porto TCP por omissao (1099).
        */
      /*  try{
            try{
                LocateRegistry.getRegistry("localhost");
                                
            }catch(RemoteException e){
                System.out.println("Registry provavelmente ja' em execucao na maquina local!");   
            }            

            /*
             * Cria o servico
             */            
        /*    TP_DS servico = new TP_DS();
            
            System.out.println("Servico ServicoDS criado e em execucao ("+servico.getRef().remoteToString()+"...");
            
            String registration = "rmi://localhost/"+SERVICE_NAME;
            
            Naming.rebind(registration, (Remote) servico);
                               
            System.out.println("Servico " + SERVICE_NAME + " registado no registry...");
            
        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(Exception e){
            System.out.println("Erro - " + e);
            System.exit(1);   }*/

  /*  @Override
    public synchronized String obterServidoresAtivos() throws RemoteException {
       
        StringBuilder sb = new StringBuilder();
        sb.append("Servidores Ativos\n");
        for(int i = 0; i < listservers.size(); i++ ){        
            if(listservers.get(i).isAtivo())
                sb.append("Servidor : "+ listservers.get(i).getIp()+ "\n");
        }
        return sb.toString();
    }

    @Override
    public synchronized void terminarServidor(String ipServidor) throws RemoteException {
        for(int i = 0; i < listservers.size(); i++ ){        
            if(listservers.get(i).isAtivo() && listservers.get(i).getIp().equals(ipServidor))                
                listservers.get(i).setAtivo(false);//Como terminar servidor??                   
        }
    }

    @Override
    public synchronized void adicionarListener() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void removerListener() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
}