package tp_cliente;

public class TP_Cliente {

    public static void main(String[] args) {
        // TODO code application logic here
        
        //verificar args para receber ip do DS
        String ip = args[0];
        ComunicacaoToDS cds = new ComunicacaoToDS(ip);
        
        
    }
    
}
