package tp_servidor;


public class ControloLigacoes extends Thread
{
    LogicaServidor servidores;
    boolean terminar = false;
    Thread threadCliente;
    
    public ControloLigacoes(LogicaServidor servidores) {
        this.servidores = servidores;
    }
    
    public void desliga()
    {
        terminar = true;
    }

    @Override
    public void run() {
            while(!terminar)
                servidores.aceitaLigacoes(threadCliente);
            
            servidores.terminar();
            
            System.exit(0);
            
    }

}