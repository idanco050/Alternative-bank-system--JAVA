package classes;

import classes.operations.Transaction;
import manager.timeline;
import resources.generated.generated.AbsLoan;
import classes.operations.loanPayment;
import manager.initializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Loan {


    public enum loanStatus {
        NEWLOAN {
            @Override
            public String toString() {
                return "New loan";
            }
        },
        PENDING {
            @Override
            public String toString() {
                return "Pending";
            }
        },
        ACTIVE {
            @Override
            public String toString() {
                return "Active";
            }
        },
        FINISHED {
            @Override
            public String toString() {
                return "Finished";
            }
        },
        RISK {
            @Override
            public String toString() {
                return "In risk";
            }
        },
    }

    private String id;
    private String category;
    private String ownerName;
    private int capital;
    private int totalMonths;
    private int monthsPerPayment;
    private double interestRate;

    private int moneyInvestedSoFar;
    private int moneyNeeded;
    private int totalPayments;
    private double totalInterest;
    private double monthlyCapital;
    private double monthlyInterest;
    private double payment;
    private int dateOfActivation = -1;
    private int dateFinished = -1;
    private double debt;
    private double interestPaid;
    private double capitalPaid;
    private double capitalRemaining;
    private double interestRemaining = 0;
    private int nextPaymentOn = -1;

    private loanStatus status;
    private Map<Customer, Integer>  involvedCustomers = new HashMap<>();
    private List<loanPayment> payList = new ArrayList<>();




    public void addCustomer(Customer c, int amount) {
        if(this.involvedCustomers.containsKey(c))
           this.involvedCustomers.put(c,this.involvedCustomers.get(c) + amount);
        else
            this.involvedCustomers.put(c, amount);

        this.moneyNeeded = this.moneyNeeded - amount;
        if(this.moneyNeeded == 0)
        {
            this.status = loanStatus.ACTIVE;
            this.dateOfActivation = timeline.getCurrentDate();
            initializer.activeLoans.add(this);
            nextPaymentOn = dateOfActivation + monthsPerPayment;
            for (Customer cus : initializer.customerList)
            {
                if (cus.getName().equals(this.ownerName))
                {
                    Customer owner = cus;
                    owner.addTransaction(new Transaction(timeline.getCurrentDate(), this.capital, owner.getBalance()));
                    owner.getTaken().add(this);
                }
            }
            this.involvedCustomers.forEach((key,val) -> key.getGiven().add(this));
            this.updateRemainAndCollectedMoney();
        }
        else
            this.status = loanStatus.PENDING;

        this.moneyInvestedSoFar = this.capital - this.moneyNeeded;

        Transaction trans = new Transaction(timeline.getCurrentDate(), -amount, c.getBalance());
        c.addTransaction(trans);

    }

    public Loan(AbsLoan l, String username) {
        ownerName = username;
        id = l.getId();
        category = l.getAbsCategory();
        capital = l.getAbsCapital();
        totalMonths = l.getAbsTotalYazTime();
        monthsPerPayment = l.getAbsPaysEveryYaz();
        interestRate = l.getAbsIntristPerPayment();
        status = loanStatus.NEWLOAN;

        moneyNeeded = capital;
        totalPayments = totalMonths / monthsPerPayment;
        totalInterest = capital * (interestRate / 100);
        monthlyCapital = capital / totalPayments;
        monthlyInterest = totalInterest / totalPayments;
        payment = monthlyInterest + monthlyCapital;
        debt = totalInterest + capital;
    }

    public Loan(String user,String id,String category,int capital, int yaz, int yazPerPayment, double interestRate)
    {
        ownerName = user;
        this.id = id;
        this.category = category;
        this.capital = capital;
        totalMonths = yaz;
        monthsPerPayment = yazPerPayment;
        this.interestRate = interestRate;
        status = loanStatus.NEWLOAN;

        moneyNeeded = capital;
        totalPayments = totalMonths / monthsPerPayment;
        totalInterest = capital * (interestRate / 100);
        monthlyCapital = capital / totalPayments;
        monthlyInterest = totalInterest / totalPayments;
        payment = monthlyInterest + monthlyCapital;
        debt = totalInterest + capital;
    }

    public Map<Customer, Integer> getInvolvedCustomers() {
        return involvedCustomers;
    }

    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCategory() {
        return category;
    }

    public int getCapital() {
        return capital;
    }

    public int getTotalMonths() {
        return totalMonths;
    }

    public int getMonthsPerPayment() {
        return monthsPerPayment;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getMoneyNeeded() {
        return moneyNeeded;
    }

    public int getTotalPayments() {
        return totalPayments;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public int getMoneyInvestedSoFar() {
        return moneyInvestedSoFar;
    }

    public double getMonthlyCapital() {
        return monthlyCapital;
    }

    public double getMonthlyInterest() {
        return monthlyInterest;
    }

    public double getPayment() {
        return payment;
    }

    public double getCapitalPaid() {
        return capitalPaid;
    }

    public int getStatusInt() {
        switch(this.status)
        {
            case NEWLOAN:
            {
                return 0;
            }
            case PENDING:
            {
                return 1;
            }
            case ACTIVE:
            {
                return 2;
            }
            case FINISHED:
            {
                return 3;
            }
            case RISK:
            {
                return 4;
            }
            default:
            {
                return -1;
            }
        }
    }
    public loanStatus getStatus(){
        return this.status;
    }

    //public Map<Customer, Integer> getInvolvedCustomers() {
    //    return involvedCustomers;
  //  }

    public void setDateOfActivation(int date) {
        this.dateOfActivation = date;
    }

    public int getDateOfActivation()
    {
        return dateOfActivation;
    }

//    public int getNextPayment(int currDate)
//    {
//        return (currDate - (currDate%monthsPerPayment) + monthsPerPayment + dateOfActivation%monthsPerPayment);
//    }

    public List<loanPayment> getPayList()
    {
        return payList;
    }

    public int getDateFinished() {
        return dateFinished;
    }


    public void payout()
    {
        String note;
            for (Customer cus : initializer.customerList)
            {
                if (cus.getName().equals(this.ownerName))
                {
                    Customer owner = cus;
                    if (payment <= owner.getBalance())
                    {
                        involvedCustomers.forEach((key, val) -> transactionPaid(key, val, true)); /////////////////////////////////
                        owner.addTransaction(new Transaction(timeline.getCurrentDate(), -payment, owner.getBalance()));
                        nextPaymentOn += monthsPerPayment;
                        note = ("Date: " + timeline.getCurrentDate() + ", Loan: " + this.id + ". Payment of " + this.payment + " successful.");
                        owner.getNotifications().add(note);
                        if (status==loanStatus.RISK)
                        {
                            if(nextPaymentOn > timeline.getCurrentDate())
                            {
                                note = ("Date: " + timeline.getCurrentDate() + ", Loan: " + this.id + " is no longer in risk! Congratulations!");
                                owner.getNotifications().add(note);
                                setStatus(loanStatus.ACTIVE);
                            }
                        }
                        debt -= payment;
                        if (debt <= 1)
                        {
                            int index = 0;
                            this.status=loanStatus.FINISHED;
                            note = ("Date: " + timeline.getCurrentDate() + ", Loan: " + this.id + " is now finished. You made it out of debt!");
                            owner.getNotifications().add(note);
                            nextPaymentOn = 0;
                           if(!(initializer.activeLoans.isEmpty()))
                            {
                                for (Loan l : initializer.activeLoans)
                                {
                                    if (l == this)
                                    {
                                        initializer.activeLoans.remove(index);
                                        break;
                                    }
                                    index++;
                                }
                            }
                        }
                        this.updateRemainAndCollectedMoney();
                    }
                    else
                    {
                        involvedCustomers.forEach((key, val) -> transactionPaid(key, val, false));
                        note = ("Date: " + timeline.getCurrentDate() + ", Loan: " + this.id + ". Payment of " + this.payment + " unsuccessful, not enough money!");
                        owner.getNotifications().add(note);
                    }
                }
            }
    }

    private void transactionPaid(Customer cus, int val, boolean paid){
        double money = payment*(val/capital);
        double capitalFraction = (val/totalPayments);
        double interstFraction = (money-capitalFraction);
        if(paid)
            cus.addTransaction(new Transaction(timeline.getCurrentDate(), money, cus.getBalance()));
        else
            status = loanStatus.RISK;

        loanPayment lp = new loanPayment(timeline.getCurrentDate(), money,interstFraction,capitalFraction,paid, cus);
        payList.add(lp);
    }
    public void updateRemainAndCollectedMoney() {
    if(status == loanStatus.ACTIVE || status == loanStatus.FINISHED) {
        for (loanPayment lp : payList) {
            if (lp.isPaid()) {
                capitalPaid = capitalPaid + lp.getCapital();
                interestPaid = interestPaid + lp.getInterest();
            }
        }
        capitalRemaining = capital - capitalPaid;
        interestRemaining = totalInterest - interestPaid;
    }
}
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public int getNextPaymentOn() {
        return nextPaymentOn;
    }

    public void setStatus(loanStatus status) {
        this.status = status;
    }

    public double getDebt() {
        return debt;
    }

    public double getCapitalRemaining() {
        return capitalRemaining;
    }
}
