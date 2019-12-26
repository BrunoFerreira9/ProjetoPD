package tp_servidor;
 
public class Playlist {
   
    private String nome;    
    private String criadorPlaylist;
    
    public Playlist(String nome,String criadorPlaylist){
        this.nome=nome;
        this.criadorPlaylist=criadorPlaylist;
    }
   
    public String getNome(){return nome;}       
    public void setNome(String value){nome = value;}
    public String getCriadorPlaylist() {return criadorPlaylist;}
    
    @Override
    public String toString(){
        return nome;
    }
}