package tp_cliente;

public class Musica {
    
    String nome;
    String autor;
    String album;
    int ano;
    double duracao ;
    String genero;
    String ficheiro;
    boolean eliminada;
    int idUserMusica;
    
    public Musica(String nome,String autor,String album,int ano,double duracao,String genero,String ficheiro,int idUser){
        this.nome = nome;
        this.autor = autor;
        this.album = album;
        this.ano = ano;
        this.duracao = duracao;
        this.genero = genero;
        this.ficheiro = ficheiro;
        this.eliminada = false;
        this.idUserMusica = idUser;
    }
    
    //------GETS------
    public String getNome(){return nome;}
    public String getAutor(){return autor;}
    public String getAlbum(){return album;}
    public String getGenero(){return genero;}
    public String getFicheiro(){return ficheiro;}
    public int getAno(){return ano;}
    public double getDuracao(){return duracao;}
    public boolean getEliminada(){return eliminada;}
    public int getIdUserMusica(){return idUserMusica;} 

    //---------SETS---------
    public void setNome(String value){nome=value;}
    public void setAutor(String value){autor=value;}
    public void setAlbum(String value){album=value;}
    public void setGenero(String value){genero=value;}
    public void setFicheiro(String value){ ficheiro=value;}
    public void setAno(int value){ano=value;}
    public void setDuracao(double value){ duracao=value;}
    public void setEliminado(){eliminada=true;}
    
    @Override
    public String toString(){
         return "\nMusica:" + this.nome + "\n "
                + "Autor:" + this.autor + "\n"
                 + "Album:" + this.album + "\n"
                 + "Género:" + this.genero + "\n"
                 + "Ano:" + this.ano + "\n"
                  + "Duracao:" + this.duracao + "\n";
    }
    
}
