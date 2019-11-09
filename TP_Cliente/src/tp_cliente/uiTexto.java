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
        sb.append("Nome | "+in.nextLine()+" ;");
        System.out.println("Autor");
        sb.append("Autor | "+in.nextLine()+" ;");
        System.out.println("Album");
        sb.append("Album | "+in.nextLine()+" ;");
        System.out.println("Ano");
        sb.append("Ano | "+in.nextLine()+" ;");
        System.out.println("Duracao");
        sb.append("Duracao | "+in.nextLine()+" ;");
        System.out.println("Genero");
        sb.append("Genero | "+in.nextLine()+" ;");
        System.out.println("Ficheiro");
        sb.append("Ficheiro | "+in.nextLine()+" ;");
        
        return sb.toString();
    }
    
    
    public void apresentaMenuPlaylist(){
    
        System.out.println("----Playlists----");
        System.out.println("1 - Criar Playlist");
        System.out.println("2 - Editar Playlist");
        System.out.println("3 - Eliminar Playlist");
        System.out.println("3 - Sair");
    }
    
     public HashMap<String,String> dadosRegisto(){
        
          HashMap<String,String> registo = new HashMap<>();
          
          System.out.println("username");
          registo.put("username", in.nextLine());
          System.out.println("password");
          registo.put("password", in.nextLine());
          System.out.println("nome:");
          registo.put("nome", in.nextLine());
         
          
         return registo;
    }
     
     
     public HashMap<String,String> dadosLogin(){
        
            HashMap<String,String> login = new HashMap<>();
          
            System.out.println("username");
           login.put("username", in.nextLine());
          System.out.println("password");
           login.put("password", in.nextLine());
       
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
                                        
                                            case 1: 
                                                cs.trataMusicas(criaMusica());
                                            case 2:
                                            case 3:
                                            case 4:
                                        }
                                        
                                    }while(op3!=5);
                                    
                                case 2 :
                                    do{
                                        apresentaMenuPlaylist();
                                        op3 = in.nextInt();
                                    
                                        switch(op3){
                                        
                                            case 1: 
                                            case 2:
                                            case 3:
                                            case 4:
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
