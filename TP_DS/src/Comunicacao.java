
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
    
    Comunicacao(List<Servidor> listservers, int numClientes, int numbasedados) {
        this.numClientes = numClientes;
        this.numbasedados = numbasedados;
        this.listservers = listservers;
    }
    
    
    public void criacomunicacao() throws SocketException{
        sock = new DatagramSocket(ConstantesDS.portoDS);
    }

    public DatagramSocket getSock() {
        return sock;
    }
    
   
    public void setPacket(DatagramPacket p){
        pkt = p;
    }
    
    public void recebepedidos() throws IOException{
        sock.receive(pkt);
        dados = new String(pkt.getData(),0,pkt.getLength());
        System.out.println(dados);
        message = ConstantesDS.ResolveMessages(dados);
        System.out.println("Recebi "+dados+" de "+pkt.getAddress()+" porto: "+pkt.getPort());
    }
    
    public void verificapedido() throws SocketException{
        switch(message.get("tipo")){
            case "Servidor":
                if(message.get("msg").equals("terminar")){
                    if(!terminaServidor(pkt.getAddress().getHostAddress())){
                        byte[] data = "sair".getBytes();
                        try {
                            DatagramPacket dtgram = new DatagramPacket(data, data.length, InetAddress.getByName(ConstantesDS.IPMULTICAST), ConstantesDS.portoMulticast);
                            sock.send(dtgram);
                        } catch (Exception ex) {
                            Logger.getLogger(Comunicacao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else{/*Implementar se o servidor é principal como funciona com o multicast...*/}
                }
                else{
                    boolean existe = false;
                
                    Servidor aux = new Servidor(pkt.getAddress().getHostAddress(),pkt.getPort(),true, (listservers.isEmpty()));
                    Servidor atual = null;
                    for(Servidor s: listservers){
                        if(s.getIp().equals(aux.getIp())){
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
                            resposta = "tipo | resposta ; sucesso | sim ; numbd | " + bd+ " ; principal | sim";
                        }
                        else
                            resposta = "tipo | resposta ; sucesso | sim ; numbd | " + bd + " ; principal | nao";
                    }
                    else{                        
                        atual.setAtivo(true);
                        resposta = "tipo | resposta ; sucesso | sim ; numbd | " + atual.getBD() + " ; principal | nao";
                    }
                }
                break;
            case "Cliente":
                Servidor aux;
                if(listservers.size()==0){
                    resposta = "tipo | resposta ; sucesso | nao ; msg | Nao existe servidores";
                    break;
                }
                if(listservers.size()==1){
                    aux = listservers.get(0);
                    listservers.get(0).setnClientes(1);
                    
                }else{ //FAZER ROUND ROBIN PARA SABER QUAL O SERVIDOR A ATRIBUIR!!!!
                    int servidor = roundRobin();
                    aux = listservers.get(servidor);
                    listservers.get(servidor).setnClientes(1);
                }
                    
                resposta = "tipo | resposta ; sucesso | sim ; ip | "+aux.getIp()+" ; porto | "+aux.getPorto();
                numClientes++;
               
            break;
        }
    }
    
    public int roundRobin(){
    
        int pos = 0;
        
        for(int i = 1; i<listservers.size();i++){
            if(listservers.get(i).getnClientes()<listservers.get(pos).getnClientes())
                pos=i;
        }
        
        return pos;
    }
    
    public void enviaresposta() throws IOException{
        pkt.setData(resposta.getBytes());
        pkt.setLength(resposta.length());
        sock.send(pkt);
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