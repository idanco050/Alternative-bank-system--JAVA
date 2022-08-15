package classes.operations;

public class Transaction {

    private int date;
    private double amount;
    private double balanceBefore;
    public double balanceAfter;
    public Transaction(int theDate, double theAmount, double ThebalanceBefore)
    {
        date = theDate;
        amount = theAmount;
        balanceBefore = ThebalanceBefore;
        balanceAfter = balanceBefore + amount;
    }
    public String toString()
    {
        return "point of time: " + date + " " + "amount: " + amount + " " + "balance before the transaction: " +
                balanceBefore + " " + "balance after the transaction: " + balanceAfter;

    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public double getBalanceBefore() {
        return balanceBefore;
    }

    public int getDate() {
        return date;
    }

}
