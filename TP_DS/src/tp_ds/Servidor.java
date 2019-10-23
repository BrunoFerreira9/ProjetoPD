package tp_ds;

public class Servidor {
    private String ip;
    private int porto;
    private boolean ativo;
    private boolean principal;
    
    Servidor(String end,int p,boolean a,boolean princ){
        ip = end;
        porto = p;
        ativo =a;
        principal = princ;
    }
}
