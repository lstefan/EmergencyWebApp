package ro.pub.cs.tcp;

public interface Activator<T> {

    public void handleMessage(T input);

}