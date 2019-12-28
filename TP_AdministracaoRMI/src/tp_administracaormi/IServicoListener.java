package tp_administracaormi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServicoListener extends Remote{
    public void notificaAlteracoes(String alteracao) throws RemoteException;
}
