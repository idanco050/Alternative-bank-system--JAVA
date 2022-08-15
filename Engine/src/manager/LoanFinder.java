package manager;

import classes.Customer;
import classes.Loan;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class LoanFinder {


    public static List<Loan> findLoans(int amount, List<String> categories, double minInterest, int minMonths, Customer theCustomer, int maxOpenLoans, int maxShare)  {
        Customer owner = null;
        List<Loan> loans = new ArrayList<>();
        for (Loan loan : initializer.loanList) {
            if (loan.getStatus() == Loan.loanStatus.NEWLOAN || loan.getStatus() == Loan.loanStatus.PENDING) {
                if (!(loan.getOwnerName().equals(theCustomer.getName()))) {
                    if (minInterest == -1 | (minInterest != -1 & minInterest <= loan.getMonthlyInterest())) {
                        if (minMonths == -1 || (minMonths != -1 & minMonths <= loan.getTotalMonths())) {
                            if (((amount) / (loan.getCapital())) * 100 <= maxShare | maxShare == -1) {
                                for (Customer c : initializer.customerList) {
                                    if (c.getName().equals(loan.getOwnerName())) {
                                        owner = c;
                                        break;
                                    }
                                }
                                if (owner != null) {
                                    if (owner.getTaken().size() <= maxOpenLoans | maxOpenLoans == -1) {
                                        if (categories.size() > 0) {
                                            for (String str : categories) {
                                                if (loan.getCategory().equals(str)) {
                                                    loans.add(loan);
                                                }
                                            }
                                        } else
                                            loans.add(loan);

                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        return loans;
    }


    public static void addLoans(List<Loan> loans, Customer theCustomer, int amount) {
        int lowestInvestment = -1;

        for (Loan l : loans) {
            if (lowestInvestment == -1)
                lowestInvestment = l.getMoneyNeeded();
            if (lowestInvestment > l.getMoneyNeeded())
                lowestInvestment = l.getMoneyNeeded();
        }

        if (!loans.isEmpty()) {
            int money = min(lowestInvestment, amount / loans.size());
            for (Loan l : loans)
                l.addCustomer(theCustomer, money);
        }
    }
}

