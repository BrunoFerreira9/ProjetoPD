package tp_cliente;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class uiTexto implements myObserver{
    
    myObservable subject;
    String ipDS ;
    Scanner in = new Scanner(System.in);
    
    ComunicacaoToServidor cs = null;
    ComunicacaoToDS cds = null;
        
    public uiTexto(String ip) 
    {
        this.ipDS = ip; 
    }
    
  public void apresentaMenuInicial(){
    
        System.out.println("----MENU----");
        System.out.println("1 - Registo");
        System.out.println("2 - Login");
        System.out.println("3 - Sair");
    }
    
    public void apresentaMenuSecundario(){
    
        System.out.println("----MENU----");
        System.out.println("1 - Musicas");
        System.out.println("2 - Playlists");
        System.out.println("3 - Sair");
    }
    public void apresentaMenuMusicas(){
    
        System.out.println("----Musicas----");
        System.out.println("1 - Criar Musica");
        System.out.println("2 - Editar Musica");
        System.out.println("3 - Eliminar Musica");
        System.out.println("4 - Adicionar musica a playlist");
        System.out.println("5 - Sair");
    }
    
    public String criaMusica(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | criaMusica ;");
        System.out.println("Nome:");
        sb.append("nome | "+in.nextLine()+" ;");
        System.out.println("Autor");
        sb.append("autor | "+in.nextLine()+" ;");
        System.out.println("Album");
        sb.append("album | "+in.nextLine()+" ;");
        System.out.println("Ano");
        sb.append("ano | "+in.nextInt()+" ;");
        System.out.println("Duracao");
        sb.append("duracao | "+in.nextDouble()+" ;");
        System.out.println("Genero");
        sb.append("genero | "+in.nextLine()+" ;");
        System.out.println("Ficheiro");
        sb.append("ficheiro | "+in.nextLine()+" ;");
        
        return sb.toString();
    }
    
    public String editaMusica(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | editaMusica ;");
        System.out.println("ID musica a editar:");
        sb.append("idMusica | "+in.nextInt()+" ;");
        System.out.println("Nome:");
        sb.append("nome | "+in.nextLine()+" ;");
        System.out.println("Autor");
        sb.append("autor | "+in.nextLine()+" ;");
        System.out.println("Album");
        sb.append("album | "+in.nextLine()+" ;");
        System.out.println("Ano");
        sb.append("ano | "+in.nextInt()+" ;");
        System.out.println("Duracao");
        sb.append("duracao | "+in.nextDouble()+" ;");
        System.out.println("Genero");
        sb.append("genero | "+in.nextLine()+" ;");
        System.out.println("Ficheiro");
        sb.append("ficheiro | "+in.nextLine()+" ;");
        
        return sb.toString();
    }
    
     public String eliminaMusica(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | eliminaMusica ;");
        System.out.println("ID musica a eliminar:");
        sb.append("idMusica | "+in.nextInt()+" ;");
        
        return sb.toString();
    }
     public String addMusicaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | addMusPlaylist ;");
        System.out.println("ID musica a adicionar:");
        sb.append("idMusica | "+in.nextInt()+" ;");
        
        return sb.toString();
    }
    
     
     
    
    public void apresentaMenuPlaylist(){
    
        System.out.println("----Playlists----");
        System.out.println("1 - Criar Playlist");
        System.out.println("2 - Editar Playlist");
        System.out.println("3 - Eliminar Playlist");
        System.out.println("4 - Remover Musica");
        System.out.println("5 - Sair");
    }
    
    public String criaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | criaPlaylist ;");
        System.out.println("Nome:");
        sb.append("nome | "+in.nextLine()+" ;");
               
        return sb.toString();
    }
    
    public String editaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | editaPlaylist ;");
        System.out.println("ID Playlist a editar:");
        sb.append("idPlaylist | "+in.nextInt()+" ;");
        System.out.println("Nome:");
        sb.append("nome | "+in.nextLine()+" ;");
        
        return sb.toString();
    }
    
     public String eliminaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | eliminaPlaylist ;");
        System.out.println("ID Playlist a eliminar:");
        sb.append("idPlaylist | "+in.nextInt()+" ;");
        
        return sb.toString();
    }
     
     public String eliminaMusicaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | eliminaMusicaPlaylist ;");       
        System.out.println("ID Musica a retirar:");
        sb.append("idMusica | "+in.nextInt()+" ;");
        
        return sb.toString();
    }
    
    
    
     public HashMap<String,String> dadosRegisto(){
        
          HashMap<String,String> registo = new HashMap<>();
          
          System.out.println("username");
          registo.put("username", in.next());
          System.out.println("password");
          registo.put("password", in.next());
          System.out.println("nome:");
          registo.put("nome", in.next());
         
          
         return registo;
    }
     
     
    public HashMap<String,String> dadosLogin(){
        
        HashMap<String,String> login = new HashMap<>();
          
        System.out.println("username");
        login.put("username", in.next());
        System.out.println("password");
        login.put("password", in.next());
       
        return login;
    }
    
    int op,op2,op3,op4;
    
    public void run() throws Exception 
    {
        try{
            cds = new ComunicacaoToDS(ipDS);
            cds.inicializaUDP();
            cs = new ComunicacaoToServidor(cds.getIpServer(),cds.getPortoServer());
            cs.inicializaTCP();
        }catch(IOException e){
             Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        HashMap<String,String> user = new HashMap<>();
        do{
            apresentaMenuInicial();
            op = in.nextInt();

            switch(op){

                case 1:                       
                        cs.efetuaRegisto(dadosRegisto()); break;
                case 2:
                        user = dadosLogin();
                        if(cs.efetuaLogin(user)){
                        
                                                  
                        do{
                            apresentaMenuSecundario();
                            op2 = in.nextInt();
                            
                            switch(op2){
                                case 1 : 
                                    
                                    do{
                                        apresentaMenuMusicas();
                                        op3 = in.nextInt();
                                    
                                        switch(op3){
                                        
                                            case 1:cs.trataMusicas(criaMusica());break;
                                            case 2:cs.trataMusicas(editaMusica());break;
                                            case 3:cs.trataMusicas(eliminaMusica());break;
                                            case 4:cs.trataMusicas(addMusicaPlaylist());break;
                                        }
                                        
                                    }while(op3!=5);
                                    
                                case 2 :
                                    do{
                                        apresentaMenuPlaylist();
                                        op3 = in.nextInt();
                                    
                                        switch(op3){
                                        
                                            case 1:cs.trataPlaylist(criaPlaylist());break;
                                            case 2:cs.trataPlaylist(editaPlaylist());break;
                                            case 3:cs.trataPlaylist(eliminaPlaylist());break;
                                            case 4:cs.trataPlaylist(eliminaMusicaPlaylist());break;
                                        }
                                        
                                    }while(op3!=5);
                            }
                        }while(op2!=3);
                        
                        
                        };break;
                case 3:
                    //mandar mensagem de logout
                    if(user!=null)
                        cs.efetuaLogout(user);
                    break;

            }
        }while(op!=3);
       
    }

    @Override
    public void update(myObservable s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
