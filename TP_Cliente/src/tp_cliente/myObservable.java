package tp_cliente;

public interface myObservable {
    public void setChanged();
    public void notifyObservers();
    public void addObserver(myObserver obs);
    public void removeObserver(myObserver obs);
}
