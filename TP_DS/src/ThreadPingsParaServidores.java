

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

class ThreadPingsParaServidores extends Thread {
    private DatagramSocket dtsocket;
    private DatagramPacket dtpack;
    private List<Servidor> listservers;
    boolean terminar = false;
    public ThreadPingsParaServidores(List<Servidor> listservers) {
        try {
            dtsocket = new DatagramSocket(ConstantesDS.portoPingsDS);
            dtsocket.setSoTimeout(5000);
        } catch (SocketException ex) {
            System.out.println("Erro ao criar o socket para os pings");
        }
        
        if(listservers == null)
            this.listservers = new ArrayList<>();
        this.listservers = listservers;
    }
    public void desliga()
    {
        terminar = true;
    }
    @Override
    public void run(){
        byte[] data = "ping".getBytes();
        
        while(!terminar){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(listservers.isEmpty())
                continue;
            
            try {
                for(Servidor s : listservers){
                    if(s.isAtivo()){
                        dtpack = new DatagramPacket(data, data.length, InetAddress.getByName(s.getIp()), s.getPorto());
                        dtsocket.send(dtpack);
                        try {
                            dtpack = new DatagramPacket(new byte[ConstantesDS.BUFSIZE], ConstantesDS.BUFSIZE);
                            dtsocket.receive(dtpack);
                            if(!TP_DS.existeprincipal){
                                s.setPrincipal(true);
                                System.out.println(s.getIp()+" passei a ser o principal!");
                                TP_DS.existeprincipal = true;
                            }
                            System.out.println("O servidor de IP " + s.getIp() + " e porto "+ s.getPorto()+"  enviou ao ping do DS: " + dtpack.getData().toString());
                        } catch (SocketTimeoutException e) {
                           System.out.println("Deixou de receber ping do servidor "+s.getIp());
                           if(s.isPrincipal()){
                               s.setPrincipal(false);
                               System.out.println(s.getIp()+" deixou de ser principal");
                               TP_DS.existeprincipal = false;
                           }
                           
                           s.setAtivo(false);
                           String dados;
 
                          
                           Set<String> chaves = s.getListaUtilizadores().keySet();
                            System.out.println(chaves);
                           for(String chave : chaves){
                                Servidor servidor = roundRobin();
                                System.out.println("-- " + servidor.getPorto());
                                dados = "tipo | Servidor ; msg | novaLigacao ; ip | "+servidor.getIp()+" ; porto | " +servidor.getPorto() ;
                                 byte[] novaData =  dados.getBytes();
                                 System.out.println("-1- " + chave);
                                 System.out.println("-2- " + s.getListaUtilizadores().get(chave));
                                dtpack = new DatagramPacket(novaData, novaData.length, InetAddress.getByName(chave), s.getListaUtilizadores().get(chave));
                                dtsocket.send(dtpack);
                           }                           
                           
                           continue;
                        }
                    }
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ThreadPingsParaServidores.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
     public Servidor roundRobin(){
    
        int pos = 0;
        List <Servidor> aux = getlistativos();
        for(int i = 1; i<aux.size();i++){
            if(aux.get(i).getnClientes()<aux.get(pos).getnClientes()){
                pos = i;
            }
        }
        
        return aux.get(pos);
    }
    
    public List<Servidor> getlistativos() {
        List<Servidor> aux = new ArrayList<>();
        for(Servidor s : listservers)
        {
            if(s.isAtivo())aux.add(s);
        }
        return aux;
    }
}