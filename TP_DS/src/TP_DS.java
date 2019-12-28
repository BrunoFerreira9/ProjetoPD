import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TP_DS extends UnicastRemoteObject implements InterfaceServico{
    
    public static final String SERVICE_NAME = "RegistryDS";
    public static final int MAX_CHUNCK_SIZE = 10000; //bytes
    public static final int MAX_CHUNK_LENGTH = 512 ;
    
    Comunicacao com;
    private static List<Servidor> listservers = new ArrayList<>();
    private static int numClientes ,numbasedados ;
    public static boolean existeprincipal = false;

    public List<IServicoListener> listaObservers;
    
    public TP_DS() throws RemoteException{
        numClientes = 0;
        numbasedados = 0;
        listaObservers = new ArrayList<>();
    }
    
    public static void main(String[] args){
        
        Comunicacao com = new Comunicacao(listservers, numClientes, numbasedados);
        try {
            com.criacomunicacao();
            new ThreadRecebePedidos(com).start();
            new ThreadPingsParaServidores(listservers).start();
        } catch (SocketException ex) {
            Logger.getLogger(TP_DS.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            try{
                LocateRegistry.createRegistry(Registry.REGISTRY_PORT);                                                
                System.out.println("Registry lancado!");           
            }catch(RemoteException e){
                System.out.println("Registry provavelmente ja' em execucao na maquina local!");   
            }            

            /*
             * Cria o servico
             */            
            TP_DS servico = new TP_DS();
                        
            com.setRmi(servico);
            
            String registration = "rmi://localhost/"+SERVICE_NAME;            
            Naming.rebind(registration, (Remote) servico);
                               
            System.out.println("Servico " + SERVICE_NAME + " registado no registry...");
            
        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(1);
        }catch(Exception e){
            System.out.println("Erro - " + e);
            System.exit(1);   
        }

    }
        @Override
        public synchronized String obterServidoresAtivos() throws RemoteException {
            StringBuilder sb = new StringBuilder();
            sb.append("Servidores Ativos\n");
            for(int i = 0; i < listservers.size(); i++ ){
                if(listservers.get(i).isAtivo())
                    sb.append("Servidor : ").append(listservers.get(i).getIp()).append("\n");
            }
            return sb.toString();
        }

        @Override
        public synchronized void terminarServidor(String ipServidor) throws RemoteException {
            for(int i = 0; i < listservers.size(); i++ ){        
                if(listservers.get(i).isAtivo() && listservers.get(i).getIp().equals(ipServidor))                
                    listservers.get(i).setAtivo(false);
                    //Como terminar servidor?? enviar mensagem de terminar??
                        //Sim penso que seja o melhor envia por UDP para esse determinado Servidor, só que é complicado saber qual é se tiveres muitos na mesma maquina
                        // só se diferenciares pelo porto, vê isso bruno
            }
        }

        @Override
        public void adicionarListener(IServicoListener listener) throws RemoteException {
                listaObservers.add(listener);
        }

        @Override
        public void removerListener(IServicoListener listener) throws RemoteException {
                listaObservers.remove(listener);
        }

        public synchronized void notifyListeners(String alteracao) {
            for(int i = 0; i < listaObservers.size(); i++)
            {
                IServicoListener listener = listaObservers.get(i);
                try
                {                      
                    listener.alteracoesSistema(alteracao);
                }catch(RemoteException e)
                {
                    listaObservers.remove(listener);
                    i--;
                }
            }
        }
    }

