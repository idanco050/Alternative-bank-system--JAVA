package classes;

public class cCustomer {

    private String name;
    private double balance;

    public cCustomer(Customer customer){
        this.name = customer.getName();
        balance = customer.getBalance();
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }


}
