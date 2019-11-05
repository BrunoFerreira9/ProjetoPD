package tp_cliente;

import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TP_Cliente  implements Observer {

    public static void apresentaMenuInicial(){
    
        System.out.println("----MENU----");
        System.out.println("1 - Registo");
        System.out.println("2 - Login");
        System.out.println("3 - Sair");
    }
    
    public static void apresentaMenuSecundario(){
    
        System.out.println("----MENU----");
        System.out.println("1 - Musicas");
        System.out.println("2 - Playlists");
        System.out.println("3 - Sair");
    }
    public static void apresentaMenuMusicas(){
    
        System.out.println("----Musicas----");
        System.out.println("1 - Criar Musica");
        System.out.println("2 - Editar Musica");
        System.out.println("3 - Eliminar Musica");
        System.out.println("4 - Adicionar musica a playlist");
        System.out.println("5 - Sair");
    }
    public static void apresentaMenuPlaylist(){
    
        System.out.println("----Playlists----");
        System.out.println("1 - Musicas");
        System.out.println("2 - Playlists");
        System.out.println("3 - Sair");
    }
    
     public static HashMap<String,String> dadosRegisto(){
        
           HashMap<String,String> registo = new HashMap<>();
        
          Scanner in = new Scanner(System.in);
          
          System.out.println("username");
          registo.put("username", in.nextLine());
          System.out.println("password");
          registo.put("password", in.nextLine());
          System.out.println("nome:");
          registo.put("nome", in.nextLine());
         return registo;
    }
     public static HashMap<String,String> dadosLogin(){
        
            HashMap<String,String> login = new HashMap<>();
          Scanner in = new Scanner(System.in);
          
          System.out.println("username");
           login.put("username", in.nextLine());
          System.out.println("password");
           login.put("password", in.nextLine());
       
          return login;
    }
    
    public static void main(String[] args) {
       
        int op;
        
        String ipDS = args[0];
        Scanner in = new Scanner(System.in);
        ComunicacaoToServidor cs = null;
        ComunicacaoToDS cds = null;
               
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
        
        do{
            apresentaMenuInicial();
            op = in.nextInt();

            switch(op){

                case 1:                       
                        cs.efetuaRegisto(dadosRegisto()); break;
                case 2:
                        cs.efetuaLogin(dadosLogin());break;
                case 3:break;

            }
        }while(op!=3);
        
        
        //cs.efetuaLogin(xpto);
        
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
   
    
}
