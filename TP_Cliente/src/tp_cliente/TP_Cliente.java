package tp_cliente;

import java.io.IOException;
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
    
     public static Utilizador dadosUser(){
        
          String nome, username, password;
          Scanner in = new Scanner(System.in);
          
          System.out.println("username");
          username= in.nextLine();
          System.out.println("password");
          password= in.nextLine();
          System.out.println("nome:");
          nome = in.nextLine();
       
          return new Utilizador(username,password,nome);
    }
    
    public static void main(String[] args) {
       
        int op;
        
        String ipDS = args[0];
        Scanner in = new Scanner(System.in);
        ComunicacaoToServidor cs = null;
        ComunicacaoToDS cds = null;
        Utilizador user;
       
        try{
             cds = new ComunicacaoToDS(ipDS);
            cds.inicializaUDP();
            //System.out.println("ip :"+cds.getIpServer()+" porto :"+cds.getPortoServer());           
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
                     user = dadosUser();
                     
                     cs.efetuaRegisto(user);
                case 2:
                     user = dadosUser();
                    cs.efetuaLogin(user);
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
