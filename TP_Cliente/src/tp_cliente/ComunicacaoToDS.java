package tp_cliente;

public class ComunicacaoToDS {
    
    //ligacao UDP com o DS
    
    //recebe o ip e porto do servidor a que ligam
     String endereco;
     int porto;
     
     public ComunicacaoToDS(String endereco, int porto) {
        this.endereco = endereco;
        this.porto = porto;      
    }
}
