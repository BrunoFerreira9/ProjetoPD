package tp_cliente;
 
import java.util.ArrayList;
import java.util.List;
 
public class Playlist {
   
    private String nome;
    private List<Integer> listaMusicas;
    private final int idUserPlaylist;
    private static int contaPlaylist = 1;
    private final int idPlaylist;

    public Playlist(String nome, List<Integer> lista,int idUserPlaylist){
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
    public int[] getLista() {
        int size = listaMusicas.size();
        int[] musicas = new int[size];
        
        for(int i = 0; i < size; i++){
            musicas[i] = listaMusicas.get(i);
        }
        
        return musicas;
    }
    public int getUserPlaylist(){return idUserPlaylist;}
    public int getPlaylistID(){return idPlaylist;}
   
    public void setNome(String value){nome = value;}
   
    public boolean addMusica(String m){
        Integer idMusica = 0; //Implementar método de ir buscar id das músicas pelo nome ou parte do nome
        if(listaMusicas.contains(idMusica))
            return false;
       
        listaMusicas.add(idMusica);
        return true;
    }
   
    public boolean removeMusica(Musica m){
        Integer idMusica = 0;//Implementar método de ir buscar id das músicas pelo nome
        if(!listaMusicas.contains(idMusica))
            return false;
       
        listaMusicas.remove(idMusica);
        return true;
    }
   
    public String listagemMusicas(){
   
        String sb = "Músicas da sua playlist: \n";
       
        for(Integer x : listaMusicas)
        {
            sb = sb + x/*.nomeMusicaPeloID*/ + "\n";//Implementar método de ir buscar nome pelo id
        }
        
        return sb;
    }
    @Override
    public String toString(){
        return idPlaylist+","+nome;
    }
}