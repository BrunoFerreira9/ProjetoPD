package tp_cliente;

public class Musica {
    
    private static int contaMusicas=1;
    private String nome;
    private String autor;
    private String album;
    private int ano;
    private double duracao ;
    private String genero;
    private String ficheiro;  
    private final int idUserMusica;
    private final int idMusica;
    
    public Musica(String nome,String autor,String album,int ano,double duracao,String genero,String ficheiro,int idUser){
        this.nome = nome;
        this.autor = autor;
        this.album = album;
        this.ano = ano;
        this.duracao = duracao;
        this.genero = genero;
        this.ficheiro = ficheiro;
       
        this.idUserMusica = idUser;
        idMusica = contaMusicas++;
    }
    
    //------GETS------
    public String getNome(){return nome;}
    public String getAutor(){return autor;}
    public String getAlbum(){return album;}
    public String getGenero(){return genero;}
    public String getFicheiro(){return ficheiro;}
    public int getAno(){return ano;}
    public double getDuracao(){return duracao;}   
    public int getIdUserMusica(){return idUserMusica;} 
    public int getIdMusica() {return idMusica;}

    //---------SETS---------
    public void setNome(String value){nome=value;}
    public void setAutor(String value){autor=value;}
    public void setAlbum(String value){album=value;}
    public void setGenero(String value){genero=value;}
    public void setFicheiro(String value){ ficheiro=value;}
    public void setAno(int value){ano=value;}
    public void setDuracao(double value){ duracao=value;}
    
    
    @Override
    public String toString(){
         return this.nome + "," + this.autor + "," + this.album + "," + this.genero + "," + this.ano + "," + this.duracao;
    }
    
}
