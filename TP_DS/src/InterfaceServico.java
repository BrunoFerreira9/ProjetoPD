import java.rmi.Remote;

public interface InterfaceServico extends Remote{
    
    public String obterServidoresAtivos() throws java.rmi.RemoteException;//Lista de Servidores
    public void terminarServidor(String ipServidor) throws java.rmi.RemoteException;
    public void adicionarListener() throws java.rmi.RemoteException;
    public void removerListener() throws java.rmi.RemoteException;
    
}
