
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.ir.ContinueNode;

public class Comunicacao implements myObserver,myObservable{
    DatagramSocket sock;
    DatagramPacket pkt;
    String dados,resposta = null;
    HashMap <String,String> message;
    byte []info = new byte[128];
    List<Servidor> listservers;
    private  int numClientes,numbasedados;
    
    boolean changed = false;
    List<myObserver> observers = new ArrayList<>();
    int msg;
    
    TP_DS rmi;
    
    Comunicacao(List<Servidor> listservers, int numClientes, int numbasedados) {
        this.numClientes = numClientes;
        this.numbasedados = numbasedados;
        this.listservers = listservers;
    }

    public TP_DS getRmi() {
        return rmi;
    }

    public void setRmi(TP_DS rmi) {
        this.rmi = rmi;
    }
    
    public void criacomunicacao() throws SocketException{
        sock = new DatagramSocket(ConstantesDS.portoDS);
    }

    public DatagramSocket getSock() {
        return sock;
    }
    
    public void enviamensagem() throws IOException{
        sock.send(pkt);
    }
   
    public void setPacket(DatagramPacket p){
        pkt = p;
    }
    
    public synchronized void recebepedidos() {
        try {
            sock.receive(pkt);
            dados = new String(pkt.getData(),0,pkt.getLength());
            message = ConstantesDS.ResolveMessages(dados);
        } catch (IOException ex) {
            sock.close();
        }
        
    }
    public void terminaDS(){
        sock.close();        
    }
    
    public void verificapedido() {
        try {        
        switch(message.get("tipo")){
            case "Servidor":
                if(message.get("msg").equals("terminar")){
                    for(Servidor server : listservers){
                        if(server.getIp().equals(pkt.getAddress().getHostAddress())){
                            server.setAtivo(false);
                            if(server.isPrincipal()){
                                server.setPrincipal(false);
                                TP_DS.existeprincipal = false;
                                rmi.notifyListeners("Servidores ativos mudaram!");
                            }
                        }
                    }
                    /*if(!terminaServidor(pkt.getAddress().getHostAddress())){
                        byte[] data = "sair".getBytes();
                        try {
                            DatagramPacket dtgram = new DatagramPacket(data, data.length, InetAddress.getByName(ConstantesDS.IPMULTICAST), ConstantesDS.portoMulticast);
                            sock.send(dtgram);
                        } catch (Exception ex) {
                            Logger.getLogger(Comunicacao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else{/*Implementar se o servidor é principal como funciona com o multicast...}*/
                }
                else{
                    boolean existe = false;
                    Servidor aux = new Servidor(pkt.getAddress().getHostAddress(),pkt.getPort(),true, (listservers.isEmpty()));
                    Servidor atual = null;
                    for(Servidor s: listservers){
                        if(s.getIp().equals(aux.getIp()) && !s.isAtivo()){
                            existe = true;
                            atual = s;
                        }
                    }
                            
                    if(!existe){
                        int bd  = numbasedados++;
                        aux.setBD(bd);
                        listservers.add(aux);
                        if(listservers.size() == 1){
                            listservers.get(0).setPrincipal(true);
                            TP_DS.existeprincipal = true;
                            resposta = "tipo | resposta ; sucesso | sim ; numbd | " + bd+ " ; principal | sim";
                        }
                        else
                            resposta = "tipo | resposta ; sucesso | sim ; numbd | " + bd + " ; principal | nao";
                    }
                    else{                        
                        aux.setBD(atual.getBD());
                        listservers.add(aux);
                        listservers.remove(atual);
                        resposta = "tipo | resposta ; sucesso | sim ; numbd | " + atual.getBD() + " ; principal | nao";
                    }
                    rmi.notifyListeners("Servidores ativos mudaram!");
                }
                break;
            case "Cliente":
                Servidor aux= null;
                if(message.get("msg").equals("desligou")){
                    Servidor servidor = roundRobin();
                    
                    resposta = "tipo | resposta ; msg | novaLigacao ; ip | "+servidor.getIp()+" ; porto | " +servidor.getPorto() ;                                              
                                         
                }else{
                                       
                    if(listservers.isEmpty()){
                        resposta = "tipo | resposta ; sucesso | nao ; msg | Nao existe servidores";
                        break;
                    }
                    if(listservers.size()==1 && listservers.get(0).isAtivo()){
                        aux = listservers.get(0);
                        listservers.get(0).setnClientes(1);
                         listservers.get(0).adicionaUtilizador(pkt.getAddress().getHostAddress(),pkt.getPort());

                    }else{ //FAZER ROUND ROBIN PARA SABER QUAL O SERVIDOR A ATRIBUIR!!!!
                        Servidor servidor = roundRobin();
                        for(Servidor s : listservers){
                            if(s.getIp().equals(servidor.getIp())){
                                s.setnClientes(1);
                                s.adicionaUtilizador(pkt.getAddress().getHostAddress(),pkt.getPort());
                                aux = s;                            
                            }
                        }
                    }
                }
                
                    rmi.notifyListeners("Novo Cliente!");     
                    resposta = "tipo | resposta ; sucesso | sim ; ip | "+aux.getIp()+" ; porto | "+aux.getPorto();
                    numClientes++;
                
            break;
        }
            
        } catch (Exception e) {
        }
    }
    
    public Servidor roundRobin(){
    
        int pos = 0;
        List <Servidor> aux = getlistativos();
        for(int i = 1; i<aux.size();i++){
            if(aux.get(i).getnClientes() <= aux.get(pos).getnClientes()){
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
    
    
    public synchronized void enviaresposta(){
        
        try {
            pkt.setData(resposta.getBytes());
            pkt.setLength(resposta.length());
            sock.send(pkt);
        } catch (IOException ex) {
           return;
        }
    }
    
    public boolean terminaServidor(String ip){
        for(Servidor server : listservers){
            if(server.getIp().equals(ip)){
                server.setAtivo(false);
                return server.isPrincipal();
            }
        }
        
        return false;
    }

    @Override
    public void update(int msg) {
      /*  String mensagem = "";
        switch(msg){
            case ATUALIZAMUSICAS:
                mensagem = "tipo | atualizamusicas";
                break;
            case ATUALIZAPLAYLISTS:
                mensagem = "tipo | atualizaplaylists";
                break;
        }
        pout.println(mensagem);
        pout.flush();*/
    }

    @Override
    public void setChanged() {
        changed = true;
    }

    @Override
    public void notifyObservers() {
        observers.forEach((obs) -> {
            obs.update(msg);
        });
        changed = false;
    }

    @Override
    public void addObserver(myObserver obs) {
        observers.add(obs);
    }

    @Override
    public void removeObserver(myObserver obs) {
        observers.remove(obs);
    }
}
