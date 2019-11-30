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
        sb.append("tipo | criaMusica ; ");
        System.out.println("Nome:");
        sb.append("nome | "+in.next()+" ; ");
        System.out.println("Autor");
        sb.append("autor | "+in.next()+" ; ");
        System.out.println("Album");
        sb.append("album | "+in.next()+" ; ");
        System.out.println("Ano");
        sb.append("ano | "+in.nextInt()+" ; ");
        System.out.println("Duracao");
        sb.append("duracao | "+in.nextDouble()+" ; ");
        System.out.println("Genero");
        sb.append("genero | "+in.next()+" ; ");
        System.out.println("Ficheiro");
        sb.append("ficheiro | "+in.next()+" ; ");
        
        return sb.toString();
    }
    
    public String editaMusica(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | editaMusica ; ");
        System.out.println("ID musica a editar:");
        sb.append("idMusica | "+in.nextInt()+" ; ");
        System.out.println("Nome:");
        sb.append("nome | "+in.next()+" ; ");
        System.out.println("Autor");
        sb.append("autor | "+in.next()+" ; ");
        System.out.println("Album");
        sb.append("album | "+in.next()+" ; ");
        System.out.println("Ano");
        sb.append("ano | "+in.nextInt()+" ; ");
        System.out.println("Duracao");
        sb.append("duracao | "+in.nextDouble()+" ; ");
        System.out.println("Genero");
        sb.append("genero | "+in.next()+" ; ");
        System.out.println("Ficheiro");
        sb.append("ficheiro | "+in.next()+" ; ");
        
        return sb.toString();
    }
    
     public String eliminaMusica(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | eliminaMusica ; ");
        System.out.println("ID musica a eliminar:");
        sb.append("idMusica | "+in.nextInt()+" ; ");
        
        return sb.toString();
    }
     public String addMusicaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | addMusPlaylist ; ");
        System.out.println("ID musica a adicionar:");
        sb.append("idMusica | "+in.nextInt()+" ; ");
        System.out.println("ID Playlist :");
        sb.append("idPlaylist | "+in.nextInt()+" ; ");
        
        return sb.toString();
    }
    
     
     
    
    public void apresentaMenuPlaylist(){
    
        System.out.println("----Playlists----");
        System.out.println("1 - Criar Playlist");
        System.out.println("2 - Editar Playlist");
        System.out.println("3 - Eliminar Playlist");
        System.out.println("4 - Remover Musica");
        System.out.println("5 - Ouvir Playlist");
        System.out.println("6 - Sair");
    }
    
    public String criaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | criaPlaylist ; ");
        System.out.println("Nome:");
        sb.append("nome | "+in.next()+" ; ");
               
        return sb.toString();
    }
    
    public String editaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | editaPlaylist ; ");
        System.out.println("ID Playlist a editar:");
        sb.append("idPlaylist | "+in.nextInt()+" ; ");
        System.out.println("Nome:");
        sb.append("nome | "+in.next()+" ; ");
        
        return sb.toString();
    }
    
     public String eliminaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | eliminaPlaylist ; ");
        System.out.println("ID Playlist a eliminar:");
        sb.append("idPlaylist | "+in.nextInt()+" ; ");
        
        return sb.toString();
    }
     
     public String eliminaMusicaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | eliminaMusicaPlaylist ; "); 
        System.out.println("ID Playlist:");
        sb.append("idPlaylist | "+in.nextInt()+" ; ");
        System.out.println("ID Musica a retirar:");
        sb.append("idMusica | "+in.nextInt()+" ; ");
        
        return sb.toString();
    }
     
      public String ouvirPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | ouvirPlaylist ; "); 
        System.out.println("ID Playlist:");
        sb.append("idPlaylist | "+in.nextInt()+" ; ");
        
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
    boolean logado=false;
    boolean existeSocket=true;


    public void run() throws Exception 
    {
        try{
            cds = new ComunicacaoToDS(ipDS);
            if(cds.inicializaUDP()){             
                cs = new ComunicacaoToServidor(cds.getIpServer(),cds.getPortoServer());
                cs.inicializaTCP();}
            else
                 existeSocket=false;
        }catch(IOException e){
             Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TP_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(existeSocket){
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

                            logado=true;                      
                            do{
                                apresentaMenuSecundario();
                                op2 = in.nextInt();

                                switch(op2){
                                    case 1 : 

                                        do{
                                            apresentaMenuMusicas();
                                            op3 = in.nextInt();

                                            switch(op3){

                                                case 1:if(!cs.trataMusicas(criaMusica()))
                                                        System.out.println("Operação não realizada!");                                                
                                                    break;
                                                case 2:if(!cs.trataMusicas(editaMusica()))
                                                        System.out.println("Operação não realizada!");  
                                                        ;break;
                                                case 3:if(!cs.trataMusicas(eliminaMusica()))
                                                        System.out.println("Operação não realizada!");  
                                                        break;
                                                case 4:if(!cs.trataMusicas(addMusicaPlaylist()))
                                                        System.out.println("Operação não realizada!");  
                                                        break;                                           
                                                default : break;
                                            }

                                        }while(op3!=5);
                                        break;
                                    case 2 :
                                        do{
                                            apresentaMenuPlaylist();
                                            op3 = in.nextInt();

                                            switch(op3){

                                                case 1:if(!cs.trataPlaylist(criaPlaylist()))
                                                        System.out.println("Operação não realizada!");
                                                break;
                                                case 2:if(!cs.trataPlaylist(editaPlaylist()))
                                                        System.out.println("Operação não realizada!");
                                                break;
                                                case 3:if(!cs.trataPlaylist(eliminaPlaylist()))
                                                        System.out.println("Operação não realizada!");
                                                break;
                                                case 4:if(!cs.trataPlaylist(eliminaMusicaPlaylist()))
                                                        System.out.println("Operação não realizada!");
                                                break;
                                                case 5:if(!cs.trataPlaylist(ouvirPlaylist()))
                                                        System.out.println("Operação não realizada!");
                                                break;

                                                default : break;
                                            }

                                        }while(op3!=5);
                                        break;
                                    default:
                                         if(logado){
                                            if(cs.efetuaLogout(user))
                                                System.out.println("Logout com sucesso!"); 
                                        }
                                        break;
                                }
                            }while(op2!=3);
                   }
                    case 3:
                        cs.terminarCliente();
                }
            }while(op!=3);
        }
    }

    @Override
    public void update(int msg) {
        switch(msg){
            case ConstantesCliente.ATUALIZAMUSICAS: break;
            case ConstantesCliente.ATUALIZAPLAYLISTS: break;
        }
    }

}
