package tp_cliente;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class uiTexto implements myObserver{
    
  
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
        System.out.println("4 - Ouvir Musica");
        System.out.println("5 - Adicionar musica a playlist");
        System.out.println("6 - Filtrar");
        System.out.println("7 - Sair");
    }
    
    public void filtroMusicas(){
    
        System.out.println("----Musicas----");
        System.out.println("1 - Nome");
        System.out.println("2 - Autor");
        System.out.println("3 - Album");
        System.out.println("4 - Ano");
        System.out.println("5 - Duracao");
        System.out.println("6 - Genero");
        System.out.println("7 - Ficheiro");
        System.out.println("8 - Sair");
    }
    
     public String filtroMusicasTexto(int op){
     
        StringBuilder sb = new StringBuilder();
     
         sb.append("tipo | filtro ; ");
        switch(op){
        
            case 1:  sb.append("filtragem | nome ; ");
                    System.out.println("Nome:");
                    sb.append("pesquisa | "+in.nextLine()+" ; ");break;
            case 2: sb.append("filtragem | autor ; ");
                    System.out.println("Autor");
                    sb.append("pesquisa | "+in.nextLine()+" ; ");break;
            case 3:sb.append("filtragem | album ; ");
                    System.out.println("Album");
                    sb.append("pesquisa | "+in.nextLine()+" ; ");break;
            case 4:sb.append("filtragem | ano ; ");
                    System.out.println("Ano");
                    sb.append("pesquisa | "+in.nextInt()+" ; ");break;
            case 5:sb.append("filtragem | duracao ; ");
                    System.out.println("Duracao");
                    sb.append("pesquisa | "+in.nextDouble()+" ; ");break;
            case 6:sb.append("filtragem | genero ; ");
                    System.out.println("Genero");
                    sb.append("pesquisa | "+in.nextLine()+" ; ");break;
           case 7:     sb.append("filtragem | ficheiro ; ");                   
                    System.out.println("Ficheiro");
                    sb.append("pesquisa | "+in.nextLine()+" ; ");break;        
        }
     
        return sb.toString();
     }
    
    public void apresentaListaMusicas(){
        cs.trataMusicas("tipo | listaMusicas;");
        if(cs.listamusicas.size() > 0){
            System.out.println("-------- Lista Musicas --------");
            System.out.println("Musica,Autor,Album,Genero,Ano,Duracao");
            for(int i=0;i<cs.listamusicas.size();i++)
                System.out.println(cs.listamusicas.get(i));
        }
    }
    public void apresentaListaPlaylists(){
        cs.trataPlaylist("tipo | listaPlaylists;");
        if(cs.listaplaylist.size() > 0){
            System.out.println("-------- Lista PlayLists --------");
            System.out.println("Nome");
            for(int i=0;i<cs.listaplaylist.size();i++)
                System.out.println(cs.listaplaylist.get(i));
        }
    }
    
    public String criaMusica(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | criaMusica ; ");
        System.out.println("Nome:");
        sb.append("nome | "+in.nextLine()+" ; ");      
        in.nextLine(); 
        System.out.println("Autor");
        sb.append("autor | "+in.nextLine()+" ; ");
        in.nextLine(); 
        System.out.println("Album");
        sb.append("album | "+in.nextLine()+" ; ");
        in.nextLine(); 
        System.out.println("Ano");
        sb.append("ano | "+in.nextInt()+" ; ");
        in.nextLine(); 
        System.out.println("Duracao");
        sb.append("duracao | "+in.nextDouble()+" ; ");
        in.nextLine(); 
        System.out.println("Genero");
        sb.append("genero | "+in.nextLine()+" ; ");
        in.nextLine(); 
        System.out.println("Ficheiro");
        sb.append("ficheiro | "+in.nextLine()+" ; ");
        in.nextLine(); 
        
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
        System.out.println("Musica a eliminar:");
        sb.append("nome | "+in.next()+" ; ");
        
        return sb.toString();
    }
     public String addMusicaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | addMusPlaylist ; ");
        System.out.println("musica a adicionar:");
        sb.append("nome | "+in.next()+" ; ");
        System.out.println("Playlist :");
        sb.append("nomePlaylist | "+in.next()+" ; ");
        
        return sb.toString();
    }
    
     
     
    
    public void apresentaMenuPlaylist(){
    
        System.out.println("----Playlists----");
        System.out.println("1 - Criar Playlist");
        System.out.println("2 - Editar Playlist");
        System.out.println("3 - Eliminar Playlist");
        System.out.println("4 - Remover Musica");
        System.out.println("5 - Ouvir Playlist");
        System.out.println("6 - Pesquisar Playlist");
        System.out.println("7 - Sair");
    }
    
    public String filtroPlaylist(){
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | filtroPlaylist ; ");
        sb.append("filtragem | nome ; ");
        System.out.println("Nome:");
        sb.append("pesquisa | "+in.next()+" ; ");
        
        return sb.toString();
   
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
        System.out.println("Playlist a editar:");
        sb.append("nomePlaylist | "+in.next()+" ; ");
        System.out.println("Nome:");
        sb.append("nome | "+in.next()+" ; ");
        
        return sb.toString();
    }
    
     public String eliminaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | eliminaPlaylist ; ");
        System.out.println("Playlist a eliminar:");
        sb.append("nome | "+in.next()+" ; ");
        
        return sb.toString();
    }
     
     public String eliminaMusicaPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | eliminaMusicaPlaylist ; "); 
        System.out.println("Playlist:");
        sb.append("nomePlaylist | "+in.next()+" ; ");
        System.out.println("Musica a retirar:");
        sb.append("nomeMusica | "+in.next()+" ; ");
        
        return sb.toString();
    }
     
      public String ouvirPlaylist(){
               
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | ouvirPlaylist ; "); 
        System.out.println("Playlist:");
        sb.append("nome | "+in.next()+" ; ");
        
        return sb.toString();
    }
      
    private String ouvirMusica() {
        StringBuilder sb = new StringBuilder();
        sb.append("tipo | ouvirMusica ; "); 
        System.out.println("Nome da Musica:");
        sb.append("nome | "+in.next()+" ; ");
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
    
    int op,op2,op3,op4, op5;
    boolean logado=false;
    boolean existeSocket=true;


    public void run() throws Exception 
    {
        try{
            cds = new ComunicacaoToDS(ipDS);
            if(cds.inicializaUDP()){             
                cs = new ComunicacaoToServidor(cds.getIpServer(),cds.getPortoServer());
                cs.inicializaTCP();
                cs.addObserver(this);
            }
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
                            apresentaListaMusicas();
                            apresentaListaPlaylists();
                            logado=true;                      
                            do{
                                apresentaMenuSecundario();
                                op2 = in.nextInt();
                                    
                                switch(op2){
                                    case 1 : 

                                        do{
                                            apresentaListaMusicas();
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
                                                case 4:if(!cs.trataMusicas(ouvirMusica()))
                                                        System.out.println("Operação não realizada!");  
                                                        break;                                           
                                                case 5:if(!cs.trataMusicas(addMusicaPlaylist()))
                                                        System.out.println("Operação não realizada!");  
                                                        break;   
                                                case 6:
                                                     
                                                    do { 
                                                        apresentaListaMusicas();
                                                        filtroMusicas();
                                                        op5 = in.nextInt();
                                                        if(op5!=8){
                                                            if(!cs.trataMusicas(filtroMusicasTexto(op5))){
                                                                System.out.println("Operação não realizada!");  
                                                            }
                                                        }
                                                    } while (op5!=8);break;
                                                default : break;
                                            }

                                        }while(op3!=7);
                                        break;
                                    case 2 :
                                        do{
                                            apresentaListaPlaylists();
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
                                                case 6:if(!cs.trataPlaylist(filtroPlaylist()))
                                                        System.out.println("Operação não realizada!");
                                                break;

                                                default : break;
                                            }

                                        }while(op3!=7);
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
            case ConstantesCliente.ATUALIZAMUSICAS: apresentaListaMusicas(); break;
            case ConstantesCliente.ATUALIZAPLAYLISTS: apresentaListaPlaylists(); break;
        }
    }

    

}
