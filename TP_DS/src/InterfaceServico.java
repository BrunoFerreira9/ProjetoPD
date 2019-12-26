

import java.rmi.Remote;
import java.util.ArrayList;

public interface InterfaceServico extends Remote{
    
    public String obterServidoresAtivos() throws java.rmi.RemoteException;
    public void terminarServidor(String ipServidor) throws java.rmi.RemoteException;
    public void adicionarListener() throws java.rmi.RemoteException;
    public void removerListener() throws java.rmi.RemoteException;
    
}
