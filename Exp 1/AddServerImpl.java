import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AddServerImpl extends UnicastRemoteObject implements AddServerIntf {
    public AddServerImpl() throws RemoteException {
        super();
    }

    public double add(double d1, double d2) throws RemoteException {
        return d1 + d2;
    }

    public static void main(String args[]) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            AddServerImpl obj = new AddServerImpl();
            registry.bind("AddServer", obj);
            System.out.println("AddServer is ready.");
        } catch (Exception e) {
            System.err.println("AddServer exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
