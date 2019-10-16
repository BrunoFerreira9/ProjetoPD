
package tp_cliente;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    
    String nome;
    List<Musica> listaMusicas;
    boolean eliminada;
     int idUserPlaylist;
     
    public Playlist(String nome, List<Musica> lista,int idUserPlaylist){
        this.nome=nome;
        listaMusicas=lista;
        eliminada=false;
        this.idUserPlaylist=idUserPlaylist;
    }
    public Playlist(String nome,int idUserPlaylist){
        this.nome=nome;
        listaMusicas=new ArrayList<>();
        eliminada=false;
        this.idUserPlaylist=idUserPlaylist;
    }
    
    public String getNome(){return nome;}
    public List<Musica> getLista(){return listaMusicas;}
    public boolean getEliminada(){return eliminada;}
    public int getUserPlaylist(){return idUserPlaylist;}
    
    public void setNome(String value){nome = value;}
    public void setEliminado(){eliminada=true;}
    public boolean addMusica(Musica m){
        if(listaMusicas.contains(m))
            return false;
        
        listaMusicas.add(m);
        return true;
    }
    
    public boolean removeMusica(Musica m){
        if(!listaMusicas.contains(m))
            return false;
        
        listaMusicas.remove(m);
        return true;
    }
    
    public String listagemMusicas(){
    
        StringBuilder sb = new StringBuilder();
        
        for(Musica x : listaMusicas)
        {
            sb.append(x.toString());
        }
        return sb.toString();
    }
}
