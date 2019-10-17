package tp_servidor;
 
import java.util.ArrayList;
import java.util.List;
 
public class Playlist {
   
    private String nome;
    private List<Musica> listaMusicas;   
    private int idUserPlaylist;
    private static int contaPlaylist = 1;
    private int idPlaylist;

    public Playlist(String nome, List<Musica> lista,int idUserPlaylist){
        this.nome=nome;
        listaMusicas=lista;        
        this.idUserPlaylist=idUserPlaylist;
        idPlaylist=contaPlaylist++;
    }
    public Playlist(String nome,int idUserPlaylist){
        this.nome=nome;
        listaMusicas=new ArrayList<>();        
        this.idUserPlaylist=idUserPlaylist;
        idPlaylist=contaPlaylist++;
    }
   
    public String getNome(){return nome;}
    public List<Musica> getLista(){return listaMusicas;}
   
    public int getUserPlaylist(){return idUserPlaylist;}
   
    public void setNome(String value){nome = value;}
   
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