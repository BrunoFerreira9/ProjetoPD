package tp_cliente;

import java.io.IOException;

public class TP_Cliente {
    public static void main(String[] args) throws Exception {
        uiTexto textUI = new uiTexto(args[0]);
        textUI.run(); 
    }
   
}