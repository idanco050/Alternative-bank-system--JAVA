package classes.operations;

import classes.Customer;

import java.nio.file.ClosedWatchServiceException;

public class loanPayment {
    private boolean paid;
    private int date;
    private double total;
    private double interest;
    private double capital;
    Customer receiver;

    public loanPayment(int date, double total, double interest, double capital, boolean paid, Customer receiver)
    {
        this.date = date;
        this.total = total;
        this.interest = interest;
        this.capital = capital;
        this.paid = paid;
        this.receiver = receiver;
    }


    @Override
    public String toString() {
        return "Payment{" +
                ", date= " + date +
                ", total= " + total +
                ", interest= " + interest +
                ", capital= " + capital +
                ", paid=" + paid +
                '}';
    }

    public double getCapital() {
        return capital;
    }

    public double getInterest() {
        return interest;
    }

    public boolean isPaid() {
        return paid;
    }
}
