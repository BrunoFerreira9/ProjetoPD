package tp_administracaormi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class TP_AdministracaoRMI extends UnicastRemoteObject implements IServicoListener{
    public static void apresentamenu(){
        
        System.out.print("---RMI--- ");        
        System.out.print("1 - Obter lista servidores ativos ");
        System.out.print("2 - Terminar Servidor ");
        System.out.print("3 - Sair ");
        
    }
    public TP_AdministracaoRMI() throws  RemoteException{}
     
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int op;
        String ip;
        if(args.length != 1){
            System.out.println("Sintaxe: java TP_AdministracaoRMI ipDS  ");
            return;
        }
        ip = args[0];
        try{
            String objectUrl = "rmi://"+ip+"/RegistryDS"; //rmiregistry on localhost

              //Cria e lanca o servico
            TP_AdministracaoRMI objeto =  new TP_AdministracaoRMI();
                        
            InterfaceServico servico = (InterfaceServico)Naming.lookup (objectUrl);;
            
            servico.adicionarListener(objeto);
            
            do{
            
                apresentamenu();
                op = in.nextInt();
                switch(op){
                
                    case 1 : System.out.println(servico.obterServidoresAtivos()); break;
                    case 2 : 
                        System.out.print("Introduza o IP do servidor:");
                        String ipTreminar = in.next();
                        servico.terminarServidor(ipTreminar);break;
                        default:break;
                }
            }while(op!=3);      
           
            
        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
        }catch(NotBoundException e){
            System.out.println("Servico remoto desconhecido - " + e);
        }catch(Exception e){
            System.out.println("Erro - " + e);
        } 
    }
    
    @Override
    public void notificaAlteracoes(String alteracao) throws RemoteException {
        System.out.println("Alteração registada: " + alteracao);
    }
}
