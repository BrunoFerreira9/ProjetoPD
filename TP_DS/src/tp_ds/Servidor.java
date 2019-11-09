package tp_ds;

public class Servidor {
    private String ip;
    private int porto;
    private boolean ativo;
    private boolean principal;
    private static int contaServs = 1;
    private int idServ;
    
    Servidor(String end,int p,boolean a,boolean princ){
        ip = end;
        porto = p;
        ativo =a;
        principal = princ;
        idServ = ++contaServs;
    }
   
    public String getIp() {
        return ip;
    }
    
    public int getId(){
        return idServ;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public int getPorto() {
        return porto;
    }

    public void setPorto(int porto) {
        this.porto = porto;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }
}
