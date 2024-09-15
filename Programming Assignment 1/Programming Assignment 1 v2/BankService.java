import java.rmi.*;

public interface BankService extends Remote {
    String createAccount(String name, double initialBalance) throws RemoteException;
    String depositMoney(int accountNumber, double amount) throws RemoteException;
    String withdrawMoney(int accountNumber, double amount) throws RemoteException;
    String checkBalance(int accountNumber) throws RemoteException;
}
